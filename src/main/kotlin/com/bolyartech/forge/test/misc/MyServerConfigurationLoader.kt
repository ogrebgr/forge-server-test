package com.bolyartech.forge.test.misc

import com.bolyartech.forge.server.config.ForgeConfigurationException
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.pathString


interface MyServerConfigurationLoader {
    fun load(): MyServerConfiguration
}

class MyServerConfigurationLoaderFile(private val configDirPath: Path) : MyServerConfigurationLoader {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val CONF_FILENAME = "my.conf"
        private const val PROP_SOME_CONFIG_PARAM = "some_config_param"
    }


    override fun load(): MyServerConfiguration {
        val path = Path.of(configDirPath.pathString, CONF_FILENAME)
        if (!path.exists()) {
            throw ForgeConfigurationException("Cannot find forge configuration file (${path.pathString})")
        }

        val prop = Properties()
        Files.newInputStream(path).use {
            prop.load(it)
        }

        val someConfigParam = prop.getProperty(PROP_SOME_CONFIG_PARAM)
        if (someConfigParam == null) {
            logger.error(
                "Missing {} in forge.conf",
                PROP_SOME_CONFIG_PARAM
            )
        }

        return MyServerConfiguration(someConfigParam)
    }

}