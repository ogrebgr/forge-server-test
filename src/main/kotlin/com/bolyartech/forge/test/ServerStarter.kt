package com.bolyartech.forge.test

import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.ForgeServer.Companion.initLog
import com.bolyartech.forge.server.config.ForgeConfigurationException
import com.bolyartech.forge.test.modules.MyJettyServer
import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import kotlin.io.path.pathString

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("my_server")
    Class.forName("org.postgresql.Driver")

    val configPack = try {
        ForgeServer.loadConfigurationPack(FileSystems.getDefault(), args)
    } catch (e: ForgeConfigurationException) {
        logger.error("Cannot load forge.conf")
        return
    }

    initLog(logger, configPack.configurationDirectory.pathString, configPack.forgeServerConfiguration.serverLogName)

    val myServer = MyJettyServer()
    myServer.start(configPack)
}