package com.bolyartech.forge.test.modules

import com.bolyartech.forge.server.AbstractForgeServerAdapter
import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.ForgeServer.Companion.createDataSourceHelper
import com.bolyartech.forge.server.WebServer
import com.bolyartech.forge.server.db.DbConfiguration
import com.bolyartech.forge.server.jetty.WebServerJetty
import com.bolyartech.forge.server.misc.MimeTypeResolverImpl
import com.bolyartech.forge.server.misc.VelocityTemplateEngineFactory
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.test.misc.MyServerConfigurationLoaderFile
import com.bolyartech.forge.test.modules.main.MainModule
import com.bolyartech.forge.test.modules.main.pages.MyConfWp
import com.bolyartech.forge.test.modules.main.pages.PathInfoWp
import com.bolyartech.forge.test.modules.main.pages.PlainTextWp
import com.bolyartech.forge.test.modules.main.pages.RootWp
import com.mchange.v2.c3p0.ComboPooledDataSource

class MyJettyServer : AbstractForgeServerAdapter() {
    override fun createDbDataSource(dbConfig: DbConfiguration): ComboPooledDataSource {
        return createDataSourceHelper(dbConfig)
    }

    override fun createWebServer(forgeConfig: ForgeServer.ConfigurationPack, dbDataSource: ComboPooledDataSource): WebServer {
        return WebServerJetty(forgeConfig, dbDataSource, createModules(forgeConfig))
    }

    private fun createModules(forgeConfig: ForgeServer.ConfigurationPack): List<SiteModule> {
        val map =
            mapOf<String, String>("event_handler.include.class" to "org.apache.velocity.app.event.implement.IncludeRelativePath")
        val tplef = VelocityTemplateEngineFactory("/templates/modules/main/", map)

        val myConf = MyServerConfigurationLoaderFile(forgeConfig.configurationDirectory).load()

        val mainModule = MainModule(forgeConfig.forgeServerConfiguration.staticFilesDir,
            MimeTypeResolverImpl(),
            RootWp(tplef),
            PlainTextWp(),
            PathInfoWp(tplef),
            MyConfWp(tplef, myConf)
        )

        return listOf(mainModule)
    }
}