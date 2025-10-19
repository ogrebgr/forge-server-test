package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.RouteHandler
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.RequestContext
import javax.inject.Inject

class Trigger500Wp @Inject constructor() : RouteHandler {
    override fun handle(ctx: RequestContext): Response {
        throw IllegalStateException("Triggering http 500")
    }
}