package com.bolyartech.forge.test.dagger


import com.bolyartech.forge.server.ForgeServer
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

