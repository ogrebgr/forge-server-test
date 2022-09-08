package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.RouteHandlerFlexible
import com.bolyartech.forge.server.handler.WebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.response.BadRequestResponse
import com.bolyartech.forge.server.response.HtmlResponse
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import javax.inject.Inject

class PathInfoWp @Inject constructor(templateEngineFactory: TemplateEngineFactory) : WebPage(templateEngineFactory),
    RouteHandlerFlexible {


    override fun handlePage(ctx: RequestContext, tple: TemplateEngine): Response {
        val params = ctx.getPathInfoParameters()
        if (params.size != 3) {
            return BadRequestResponse()
        }

        tple.export("param1", params[0])
        tple.export("param2", params[1])
        tple.export("param3", params[2])

        tple.export("_page", "pathinfo")
        return HtmlResponse(tple.render("template.vm"))
    }

    override fun willingToHandle(pathInfo: String): Boolean {
        val params = pathInfo.split("/")
        if (params.size != 3) {
            return false
        }

        return true
    }

}