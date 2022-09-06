package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.WebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.response.FileUploadResponse
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.dagger.DownloadsDir
import javax.inject.Inject

class UserDownloadWp @Inject constructor(
    templateEngineFactory: TemplateEngineFactory,
    @DownloadsDir private val downloadsDir: String
) : WebPage(templateEngineFactory) {
    override fun handlePage(ctx: RequestContext, tple: TemplateEngine): Response {
        return FileUploadResponse(downloadsDir + "/test_file")
    }
}