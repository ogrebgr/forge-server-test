package com.bolyartech.forge.test.modules.main.endpoints

import com.bolyartech.forge.server.handler.ForgeEndpoint
import com.bolyartech.forge.server.response.forge.ForgeResponse
import com.bolyartech.forge.server.response.forge.OkResponse
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.data.TestTable
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ForgeEndpointEp @Inject constructor() : ForgeEndpoint() {
    override fun handleForge(ctx: RequestContext): ForgeResponse {
        val items = listOf(TestTable(1, "aaaa", 11), TestTable(2, "bbbb", 22))

        return OkResponse(Json.encodeToString(items))
    }
}