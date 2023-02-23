package com.bolyartech.forge.test.modules

import com.bolyartech.forge.server.AbstractForgeServerAdapter
import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.WebServer
import com.bolyartech.forge.server.db.DbConfiguration
import com.bolyartech.forge.server.jetty.WebServerJetty
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.test.modules.main.MainModule
import com.mchange.v2.c3p0.ComboPooledDataSource
import java.nio.file.FileSystem
import javax.inject.Inject

class MyJettyServerDepInj @Inject constructor(
    private val mainModule: MainModule
) : AbstractForgeServerAdapter() {

    override fun createDbDataSource(dbConfig: DbConfiguration): ComboPooledDataSource {
        return ForgeServer.createDataSourceHelper(dbConfig)
    }

    override fun createWebServer(
        forgeConfig: ForgeServer.ConfigurationPack,
        dbDataSource: ComboPooledDataSource,
        fileSystem: FileSystem
    ): WebServer {
        return WebServerJetty(forgeConfig, createModules())
    }

    private fun createModules(): List<SiteModule> {
        return listOf(mainModule)
    }
}