package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.HttpMethod
import com.bolyartech.forge.server.db.DbPool
import com.bolyartech.forge.server.handler.DbWebPage
import com.bolyartech.forge.server.misc.Params
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.response.BadRequestResponse
import com.bolyartech.forge.server.response.HtmlResponse
import com.bolyartech.forge.server.response.RedirectResponse303SeeOther
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.MissingParameterValueException
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.data.TestTableDbh
import java.sql.Connection
import javax.inject.Inject

class EditDbWp @Inject constructor(
    templateEngineFactory: TemplateEngineFactory,
    dbPool: DbPool,
    private val testTableDbh: TestTableDbh
) : DbWebPage(templateEngineFactory, dbPool) {
    companion object {
        private const val PARAM_ID = "id"
        private const val FF_FIELD_STRING = "field_string"
        private const val FF_FIELD_INT = "field_int"
    }

    override fun handlePage(ctx: RequestContext, dbc: Connection, tple: TemplateEngine): Response {
        return if (ctx.isMethod(HttpMethod.GET)) {
            modeGet(ctx, dbc, tple)
        } else {
            modePost(ctx, dbc, tple)
        }
    }

    private fun modeGet(ctx: RequestContext, dbc: Connection, tple: TemplateEngine): Response {
        val id = ctx.getFromQuery(PARAM_ID)
        if (id != null) {
            val item = testTableDbh.loadById(dbc, id.toInt())
            if (item != null) {
                tple.export("item", item)
            } else {
                return BadRequestResponse()
            }
        }

        return common(tple)
    }

    private fun modePost(ctx: RequestContext, dbc: Connection, tple: TemplateEngine): Response {
        val fieldStr = ctx.getFromPost(FF_FIELD_STRING) ?: throw MissingParameterValueException()
        val fieldInt = Params.extractIntFromPost(ctx, FF_FIELD_INT)

        testTableDbh.createNew(dbc, fieldStr, fieldInt)

        return RedirectResponse303SeeOther("list_db")
    }


    private fun common(tple: TemplateEngine): Response {
        tple.export("_page", "edit_db")
        return HtmlResponse(tple.render("template.vm"))
    }
}
