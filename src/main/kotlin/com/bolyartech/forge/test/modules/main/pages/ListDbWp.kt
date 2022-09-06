package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.db.DbPool
import com.bolyartech.forge.server.handler.DbWebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.data.TestTableDbh
import java.sql.Connection
import javax.inject.Inject

class ListDbWp @Inject constructor(
    templateEngineFactory: TemplateEngineFactory,
    dbPool: DbPool,
    private val testTableDbh: TestTableDbh
) : DbWebPage(templateEngineFactory, dbPool) {

    override fun handlePage(ctx: RequestContext, dbc: Connection, tple: TemplateEngine): Response {
        val items = testTableDbh.loadLastPage(dbc, 10)
        tple.export("items", items)

        tple.export("_page", "list_db")
        return createHtmlResponse(tple.render("template.vm"))
    }

}