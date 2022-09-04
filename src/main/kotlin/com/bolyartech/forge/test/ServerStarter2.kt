package com.bolyartech.forge.test

import com.bolyartech.forge.test.modules.MyJettyServer
import java.nio.file.FileSystems

fun main(args: Array<String>) {
    Class.forName("org.postgresql.Driver")

    val myServer = MyJettyServer()
    myServer.start(FileSystems.getDefault(), args)
}