package com.bolyartech.forge.test

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.JoranException
import com.bolyartech.forge.server.ForgeSystemServlet
import com.bolyartech.forge.server.config.ForgeConfigurationException
import com.bolyartech.forge.server.jetty.loadConfigurationPack
import com.bolyartech.forge.server.module.SiteModuleRegisterImpl
import com.bolyartech.forge.server.route.RouteRegisterImpl
import com.bolyartech.forge.test.modules.main.MainModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.FileSystems
import kotlin.io.path.pathString


fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("my_app")

    val confPack = try {
        loadConfigurationPack(FileSystems.getDefault(), args)
    } catch (e: ForgeConfigurationException) {
        logger.error(e.message)
        return
    }

    initLog(logger, confPack.configurationDirectory.pathString, confPack.forgeServerConfiguration.serverLogName)

    val mainModule = MainModule()
    val forgeSystemServlet = ForgeSystemServlet(
        listOf(mainModule),
        SiteModuleRegisterImpl(
            RouteRegisterImpl(
                confPack.forgeServerConfiguration.isPathInfoEnabled,
                confPack.forgeServerConfiguration.maxSlashesInPathInfo
            )
        ),
    )

//    val server = WebServerJetty(confPack.forgeJettyConfiguration, forgeSystemServlet, NullSessionDataStoreFactory())
//    server.start()
}


private fun initLog(logger: Logger, configDir: String, logFilenamePrefix: String = "", serverNameSuffix: String = "") {
    val context = LoggerFactory.getILoggerFactory() as LoggerContext
    val jc = JoranConfigurator()
    jc.context = context
    context.reset()

    context.putProperty("application-name", logFilenamePrefix + serverNameSuffix)

    val f = File(configDir, "logback.xml")
    println("Will try logback config: " + f.absolutePath)
    if (f.exists()) {
        val logbackConfigFilePath = f.absolutePath
        try {
            jc.doConfigure(logbackConfigFilePath)
            logger.info("+++ logback initialized OK")
        } catch (e: JoranException) {
            e.printStackTrace()
        }
    } else {
        println("!!! No logback configuration file found. Using default conf")
    }
}