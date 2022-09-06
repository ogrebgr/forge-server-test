package com.bolyartech.forge.test.dagger


import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.misc.MimeTypeResolver
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.misc.VelocityTemplateEngineFactory
import com.bolyartech.forge.test.misc.MyServerConfiguration
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class ServerDaggerModule(private val forgeConfig: ForgeServer.ConfigurationPack, private val myConf: MyServerConfiguration) {
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
    fun provideMimeTypeResolver() : MimeTypeResolver {
        return MimeTypeResolverImpl()
    }

    @Provides
    fun providePublicTemplateEngineFactory(): TemplateEngineFactory {
        val map =
            mapOf<String, String>("event_handler.include.class" to "org.apache.velocity.app.event.implement.IncludeRelativePath")
        return VelocityTemplateEngineFactory("/templates/modules/main/", map)
    }

    @Provides
    fun provideMyServerConfiguration() : MyServerConfiguration {
        return myConf
    }
}


@Module
abstract class ServerDaggerModuleBinds {

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class StaticFilesDir

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NotFoundHandler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InternalServerErrorHandler

