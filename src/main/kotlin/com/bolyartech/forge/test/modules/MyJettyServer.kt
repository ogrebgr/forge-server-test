package com.bolyartech.forge.test.modules

import com.bolyartech.forge.server.AbstractForgeServerAdapter
import com.bolyartech.forge.server.WebServer
import com.bolyartech.forge.server.jetty.WebServerJetty
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.test.modules.main.MainModule
import com.mchange.v2.c3p0.ComboPooledDataSource
import java.nio.file.FileSystem

class MyJettyServer : AbstractForgeServerAdapter() {
    override fun createWebServer(fs: FileSystem, forgeConfig: ConfigurationPack, dbDataSource: ComboPooledDataSource): WebServer {
        return WebServerJetty(fs, forgeConfig, dbDataSource, createModules())
    }

    private fun createModules(): List<SiteModule> {
        val mainModule = MainModule()
        return listOf(mainModule)
    }
}