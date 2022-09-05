package com.bolyartech.forge.test.modules

import com.bolyartech.forge.server.AbstractForgeServerAdapter
import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.ForgeServer.Companion.createDataSourceHelper
import com.bolyartech.forge.server.WebServer
import com.bolyartech.forge.server.db.DbConfiguration
import com.bolyartech.forge.server.jetty.WebServerJetty
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.test.modules.main.MainModule
import com.mchange.v2.c3p0.ComboPooledDataSource

class MyJettyServer : AbstractForgeServerAdapter() {
    override fun createDbDataSource(dbConfig: DbConfiguration): ComboPooledDataSource {
        return createDataSourceHelper(dbConfig)
    }

    override fun createWebServer(forgeConfig: ForgeServer.ConfigurationPack, dbDataSource: ComboPooledDataSource): WebServer {
        return WebServerJetty(forgeConfig, dbDataSource, createModules(forgeConfig.forgeServerConfiguration.staticFilesDir))
    }

    private fun createModules(staticFilesDir: String): List<SiteModule> {
        val mainModule = MainModule(staticFilesDir, MimeTypeResolverImpl())
        return listOf(mainModule)
    }
}