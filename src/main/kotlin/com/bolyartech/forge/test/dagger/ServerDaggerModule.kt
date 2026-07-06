package com.bolyartech.forge.test.dagger


import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.handler.RouteHandler
import com.bolyartech.forge.server.misc.MimeTypeResolver
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.misc.VelocityTemplateEngineFactory
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.response.builders.BadRequestHtmlResponseBuilder
import com.bolyartech.forge.server.response.builders.HtmlResponseBuilder
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.misc.MyServerConfiguration
import dagger.Module
import dagger.Provides
import java.nio.file.FileSystem
import javax.inject.Qualifier

@Module
class ServerDaggerModule(
    private val fileSystem: FileSystem,
    private val forgeConfig: ForgeServer.ConfigurationPack,
    private val myConf: MyServerConfiguration
) {


    @Provides
    fun provideFileSystem(): FileSystem {
        return fileSystem
    }

    @Provides
    @StaticFilesDir
    fun provideStaticFilesDir(): String {
        return forgeConfig.forgeServerConfiguration.staticFilesDir
    }

    @Provides
    fun provideConfigurationPack(): ForgeServer.ConfigurationPack {
        return forgeConfig
    }

    @Provides
    fun provideMimeTypeResolver(): MimeTypeResolver {
        return MimeTypeResolverImpl()
    }

    @Provides
    fun providePublicTemplateEngineFactory(): TemplateEngineFactory {
        val map =
            mapOf("event_handler.include.class" to "org.apache.velocity.app.event.implement.IncludeRelativePath")
        return VelocityTemplateEngineFactory("/templates/modules/main/", map)
    }

    @Provides
    fun provideMyServerConfiguration(): MyServerConfiguration {
        return myConf
    }

    @Provides
    @UploadsDir
    fun provideUploadsDir(): String {
        return forgeConfig.forgeServerConfiguration.uploadsDirectory
    }

    @Provides
    @DownloadsDir
    fun provideDownloadsDir(): String {
        return forgeConfig.forgeServerConfiguration.downloadsDirectory
    }

    @Provides
    @NotFoundHandler
    fun provideNotFoundHandler(@NotFoundBuilder b: HtmlResponseBuilder): RouteHandler {
        return object : RouteHandler {
            override fun handle(ctx: RequestContext): Response {
                return b.build()
            }
        }
    }

    @Provides
    @InternalServerErrorHandler
    fun provideInternalServerErrorHandler(@InternalServerErrorBuilder b: HtmlResponseBuilder): RouteHandler {
        return object : RouteHandler {
            override fun handle(ctx: RequestContext): Response {
                return b.build()
            }
        }
    }

    @Provides
    @NotFoundBuilder
    fun provideNotFoundBuilder(tplef: TemplateEngineFactory): HtmlResponseBuilder {
        val tple = tplef.createNew()
        tple.assign("_subdir", "_http_errors")
        tple.assign("_page", "http_404")
        return HtmlResponseBuilder(404).body(tple.render("template_http_error.vm"))
    }

    @Provides
    @InternalServerErrorBuilder
    fun provideInternalServerErrorBuilder(tplef: TemplateEngineFactory): HtmlResponseBuilder {
        val tple = tplef.createNew()
        tple.assign("_subdir", "_http_errors")
        tple.assign("_page", "http_500")
        return HtmlResponseBuilder(500).body(tple.render("template_http_error.vm"))
    }

    @Provides
    @BadRequestBuilder
    fun provideBadRequestBuilder(tplef: TemplateEngineFactory): HtmlResponseBuilder {
        val tple = tplef.createNew()
        tple.assign("_subdir", "_http_errors")
        tple.assign("_page", "http_400")
        return BadRequestHtmlResponseBuilder(tple.render("template_http_error.vm"))
    }

    @Provides
    @PaymentRequiredErrorBuilder
    fun providePaymentRequiredErrorBuilder(tplef: TemplateEngineFactory): HtmlResponseBuilder {
        val tple = tplef.createNew()
        tple.assign("_subdir", "_http_errors")
        tple.assign("_page", "http_402")
        return HtmlResponseBuilder(402).body(tple.render("template_http_error.vm"))
    }
}


@Module
abstract class ServerDaggerModuleBinds

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class StaticFilesDir

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NotFoundHandler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InternalServerErrorHandler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UploadsDir

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DownloadsDir

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NotFoundBuilder

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BadRequestBuilder

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InternalServerErrorBuilder

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PaymentRequiredErrorBuilder
