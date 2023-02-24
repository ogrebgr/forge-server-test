package com.bolyartech.forge.test

import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.config.ForgeConfigurationException
import com.bolyartech.forge.test.dagger.DaggerMyDaggerComponent
import com.bolyartech.forge.test.dagger.DbDaggerModule
import com.bolyartech.forge.test.dagger.ServerDaggerModule
import com.bolyartech.forge.test.misc.MyServerConfigurationLoaderFile
import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import kotlin.io.path.pathString

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("my_server")
    Class.forName("org.postgresql.Driver")

    val configPack = try {
        ForgeServer.loadConfigurationPack(FileSystems.getDefault(), args)
    } catch (e: ForgeConfigurationException) {
        logger.error("Cannot load forge.conf: ${e.message}")
        return
    }

    ForgeServer.initLog(logger, configPack.configurationDirectory.pathString, configPack.forgeServerConfiguration.logPrefix)

    val myConf = MyServerConfigurationLoaderFile(configPack.configurationDirectory).load()

    val server =
        DaggerMyDaggerComponent.builder()
            .serverDaggerModule(ServerDaggerModule(FileSystems.getDefault(), configPack, myConf))
            .dbDaggerModule(DbDaggerModule(configPack.dbConfiguration))
            .build().provideServer()

    server.start(configPack, FileSystems.getDefault())
}