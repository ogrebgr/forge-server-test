package com.bolyartech.forge.test

import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.ForgeServer.Companion.initLogger
import com.bolyartech.forge.server.config.ForgeConfigurationException
import com.bolyartech.forge.test.dagger.DaggerMyDaggerComponent
import com.bolyartech.forge.test.dagger.DbDaggerModule
import com.bolyartech.forge.test.dagger.ServerDaggerModule
import com.bolyartech.forge.test.misc.MyServerConfigurationLoaderFile
import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("my_server")
    Class.forName("org.postgresql.Driver")

    val configPack = try {
        ForgeServer.loadConfigurationPack(FileSystems.getDefault(), args)
    } catch (e: ForgeConfigurationException) {
        logger.error("Cannot load forge.conf: ${e.message}")
        return
    }

    // We need to initialize the logger here, i.e. before dependency injection to avoid cluttering the log with messages from
    // Velocity, DB, etc.
    initLogger(
        logger,
        configPack.configurationDirectory,
        configPack.forgeServerConfiguration.deployment.deploymentName,
        configPack.forgeServerConfiguration.logPrefix,
    )

    val myConf = MyServerConfigurationLoaderFile(configPack.configurationDirectory).load()
    val server =
        DaggerMyDaggerComponent.builder()
            .serverDaggerModule(ServerDaggerModule(FileSystems.getDefault(), configPack, myConf))
            .dbDaggerModule(DbDaggerModule(configPack.dbConfiguration))
            .build().provideServer()

    if (!server.start(configPack, FileSystems.getDefault())) {
        exitProcess(1)
    }
}
