package com.bolyartech.forge.test.modules.main

import com.bolyartech.forge.server.HttpMethod
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.server.route.Route
import com.bolyartech.forge.server.route.RouteSimple
import com.bolyartech.forge.test.modules.main.pages.RootWp

class MainModule : SiteModule {
    companion object {
        private const val MODULE_SYSTEM_NAME = "main"
        private const val MODULE_VERSION_CODE = 1
        private const val MODULE_VERSION_NAME = "1.0.0"
        private const val PATH_PREFIX = "/"
    }

    override fun createRoutes(): List<Route> {
        val ret = mutableListOf<Route>()

        ret.add(RouteSimple(HttpMethod.GET, PATH_PREFIX, RootWp()))

        return ret
    }

    override fun getSystemName(): String {
        return MODULE_SYSTEM_NAME
    }

    override fun getShortDescription(): String {
        return ""
    }

    override fun getVersionCode(): Int {
        return MODULE_VERSION_CODE
    }

    override fun getVersionName(): String {
        return MODULE_VERSION_NAME
    }
}