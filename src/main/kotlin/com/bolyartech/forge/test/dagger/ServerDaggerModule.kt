package com.bolyartech.forge.test.dagger


import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.misc.MimeTypeResolver
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class ServerDaggerModule(private val forgeConfig: ForgeServer.ConfigurationPack) {
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

