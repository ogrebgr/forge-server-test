package com.bolyartech.forge.test.modules.main

import com.bolyartech.forge.server.HttpMethod
import com.bolyartech.forge.server.handler.StaticFileHandler
import com.bolyartech.forge.server.misc.MimeTypeResolver
import com.bolyartech.forge.server.misc.VelocityTemplateEngineFactory
import com.bolyartech.forge.server.module.SiteModule
import com.bolyartech.forge.server.route.Route
import com.bolyartech.forge.server.route.RouteExact
import com.bolyartech.forge.server.route.RouteFlexible
import com.bolyartech.forge.test.dagger.StaticFilesDir
import com.bolyartech.forge.test.modules.main.pages.*
import javax.inject.Inject

class MainModule @Inject constructor(
    @StaticFilesDir private val staticFileDir: String,
    private val mimeTypeResolver: MimeTypeResolver,
    private val rootWp: RootWp,
    private val plainTextWp: PlainTextWp,
    private val pathInfoWp: PathInfoWp,
    private val myConfWp: MyConfWp,
    private val userUploadWp: UserUploadWp,
    private val userDownloadWp: UserDownloadWp,
) : SiteModule {

    companion object {
        private const val MODULE_SYSTEM_NAME = "main"
        private const val MODULE_VERSION_CODE = 1
        private const val MODULE_VERSION_NAME = "1.0.0"
        private const val PATH_PREFIX = "/"
    }

    override fun createRoutes(): List<Route> {
        val ret = mutableListOf<Route>()

        val map = mapOf<String, String>("event_handler.include.class" to "org.apache.velocity.app.event.implement.IncludeRelativePath")
        val templateEngineFactory = VelocityTemplateEngineFactory("/templates/modules/main/", map)

        ret.add(RouteExact(HttpMethod.GET, PATH_PREFIX, rootWp))
        ret.add(RouteExact(HttpMethod.GET, PATH_PREFIX + "plaintext", plainTextWp))
        ret.add(RouteFlexible(HttpMethod.GET, PATH_PREFIX + "pathinfo/", pathInfoWp))
        ret.add(RouteExact(HttpMethod.GET, PATH_PREFIX + "myconfig", myConfWp))
        ret.add(RouteExact(HttpMethod.GET, PATH_PREFIX + "user_upload", userUploadWp))
        ret.add(RouteExact(HttpMethod.POST, PATH_PREFIX + "user_upload", userUploadWp))
        ret.add(RouteExact(HttpMethod.GET, PATH_PREFIX + "user_download", userDownloadWp))
        // TODO - download
        // TODO - form
        // Web page db
        // Endpoints
        ret.add(RouteFlexible(HttpMethod.GET, PATH_PREFIX, StaticFileHandler("$staticFileDir/main", mimeTypeResolver)))

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