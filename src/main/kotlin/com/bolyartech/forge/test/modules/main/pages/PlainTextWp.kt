package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.RouteHandler
import com.bolyartech.forge.server.response.PlainTextResponse
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import javax.inject.Inject

class PlainTextWp @Inject constructor() : RouteHandler {
    override fun handle(ctx: RequestContext): Response {
        return PlainTextResponse("<h1>PlainTextResponse demo!</h1>")
    }
}