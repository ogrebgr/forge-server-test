package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.WebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.route.RequestContext

class RootWp(templateEngineFactory: TemplateEngineFactory): WebPage(templateEngineFactory) {

    override fun produceHtml(ctx: RequestContext, tple: TemplateEngine): String {
        tple.export("_page", "root")
        return tple.render("template.vm")
    }
}