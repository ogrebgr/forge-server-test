package com.bolyartech.forge.test

import com.bolyartech.forge.server.AbstractForgeServerAdapter
import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.WebServer
import com.bolyartech.forge.server.db.DbPool
import com.bolyartech.forge.server.jetty.WebServerJetty
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.test.modules.main.MainModule
import java.nio.file.FileSystem
import javax.inject.Inject

class MyJettyServerDepInj @Inject constructor(
    private val mainModule: MainModule,
    private val dbPool: DbPool,
) : AbstractForgeServerAdapter() {

    override fun testDbConnection() {
        dbPool.connection.use {
            // just to check if it is successful
        }
    }

    override fun createWebServer(
        forgeConfig: ForgeServer.ConfigurationPack,
        fileSystem: FileSystem
    ): WebServer {
        return WebServerJetty(forgeConfig, createModules())
    }

    private fun createModules(): List<SiteModule> {
        return listOf(mainModule)
    }
}