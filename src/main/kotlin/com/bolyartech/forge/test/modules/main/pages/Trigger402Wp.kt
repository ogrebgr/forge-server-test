package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.handler.RouteHandler
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.response.builders.HtmlResponseBuilder
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.dagger.PaymentRequiredErrorBuilder
import javax.inject.Inject

class Trigger402Wp @Inject constructor(
    @PaymentRequiredErrorBuilder private val paymentRequiredErrorBuilder: HtmlResponseBuilder,
) : RouteHandler {
    override fun handle(ctx: RequestContext): Response {
        return paymentRequiredErrorBuilder.build()
    }
}