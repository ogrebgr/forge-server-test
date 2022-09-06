package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.WebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.misc.MyServerConfiguration
import javax.inject.Inject

class MyConfWp @Inject constructor(
    templateEngineFactory: TemplateEngineFactory,
    private val myConf: MyServerConfiguration
) :
    WebPage(templateEngineFactory) {
    override fun handlePage(ctx: RequestContext, tple: TemplateEngine): Response {
        tple.export("value", myConf.siteConfigParam)

        tple.export("_page", "my_conf")
        return createHtmlResponse(tple.render("template.vm"))
    }


}