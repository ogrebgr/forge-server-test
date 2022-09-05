package com.bolyartech.forge.test

import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.config.ForgeConfigurationException
import com.bolyartech.forge.test.dagger.DaggerMyDaggerComponent
import com.bolyartech.forge.test.dagger.ServerDaggerModule
import org.slf4j.LoggerFactory
import java.nio.file.FileSystems

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("my_server")
    Class.forName("org.postgresql.Driver")

    val configPack = try {
        ForgeServer.loadConfigurationPack(FileSystems.getDefault(), args)
    } catch (e: ForgeConfigurationException) {
        logger.error("Cannot load forge.conf")
        return
    }

    val server = DaggerMyDaggerComponent.builder().serverDaggerModule(ServerDaggerModule(configPack)).build().provideServer()
    server.start(configPack)
}