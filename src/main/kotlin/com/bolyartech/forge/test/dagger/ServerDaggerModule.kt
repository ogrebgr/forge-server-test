package com.bolyartech.forge.test.dagger


import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class ServerDaggerModule(private val staticFilesDir: String) {
    @Provides
    @StaticFilesDir
    fun provideStaticFilesDir(): String {
        return staticFilesDir
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

