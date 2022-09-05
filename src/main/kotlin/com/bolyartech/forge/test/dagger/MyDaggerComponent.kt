package com.bolyartech.forge.test.dagger

import com.bolyartech.forge.test.modules.MyJettyServerDepInj
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ServerDaggerModule::class,
        ServerDaggerModuleBinds::class,
        DbDaggerModule::class,
        DbDaggerModuleBinds::class]
)
interface MyDaggerComponent {
    fun provideServer(): MyJettyServerDepInj
}