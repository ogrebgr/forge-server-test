package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.WebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.response.HtmlResponse
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import javax.inject.Inject

class RootWp @Inject constructor(templateEngineFactory: TemplateEngineFactory) : WebPage(templateEngineFactory) {
    override fun handlePage(ctx: RequestContext, tple: TemplateEngine): Response {
        tple.export("_page", "root")
        return HtmlResponse(tple.render("template.vm"))
    }
}