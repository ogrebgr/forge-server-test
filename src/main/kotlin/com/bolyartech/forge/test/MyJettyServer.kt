package com.bolyartech.forge.test

import com.bolyartech.forge.server.AbstractForgeServerAdapter
import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.ForgeServer.Companion.createDataSourceHelper
import com.bolyartech.forge.server.WebServer
import com.bolyartech.forge.server.db.DbPool
import com.bolyartech.forge.server.db.HikariCpDbPool
import com.bolyartech.forge.server.jetty.WebServerJetty
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl
import com.bolyartech.forge.server.misc.VelocityTemplateEngineFactory
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.test.data.TestTableDbhImpl
import com.bolyartech.forge.test.misc.MyServerConfigurationLoaderFile
import com.bolyartech.forge.test.modules.main.MainModule
import com.bolyartech.forge.test.modules.main.endpoints.ForgeEndpointEp
import com.bolyartech.forge.test.modules.main.pages.*
import com.google.gson.Gson
import java.nio.file.FileSystem

class MyJettyServer : AbstractForgeServerAdapter() {

    private lateinit var dbPool: DbPool

    override fun createWebServer(
        forgeConfig: ForgeServer.ConfigurationPack,
        fileSystem: FileSystem
    ): WebServer {
        dbPool = HikariCpDbPool(createDataSourceHelper(forgeConfig.dbConfiguration))
        return WebServerJetty(forgeConfig, createModules(forgeConfig, fileSystem, dbPool))
    }

    override fun testDbConnection() {
        dbPool.connection.use {
            // just to check if it is successful
        }
    }

    private fun createModules(
        forgeConfig: ForgeServer.ConfigurationPack,
        fileSystem: FileSystem,
        dbPool: DbPool,
    ): List<SiteModule> {
        val map =
            mapOf<String, String>("event_handler.include.class" to "org.apache.velocity.app.event.implement.IncludeRelativePath")
        val tplef = VelocityTemplateEngineFactory("/templates/modules/main/", map)

        val myConf = MyServerConfigurationLoaderFile(forgeConfig.configurationDirectory).load()

        val mainModule = MainModule(
            forgeConfig.forgeServerConfiguration.staticFilesDir,
            MimeTypeResolverImpl(),
            RootWp(tplef),
            PlainTextWp(),
            PathInfoWp(tplef),
            MyConfWp(tplef, myConf),
            UserUploadWp(tplef, fileSystem, forgeConfig.forgeServerConfiguration.uploadsDirectory),
            UserDownloadWp(tplef, forgeConfig.forgeServerConfiguration.downloadsDirectory),
            ListDbWp(tplef, dbPool, TestTableDbhImpl()),
            EditDbWp(tplef, dbPool, TestTableDbhImpl()),
            ForgeEndpointEp(Gson()),
        )

        return listOf(mainModule)
    }
}