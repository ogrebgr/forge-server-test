package com.bolyartech.forge.test.modules.main.pages

import com.bolyartech.forge.server.HttpMethod
import com.bolyartech.forge.server.handler.WebPage
import com.bolyartech.forge.server.misc.TemplateEngine
import com.bolyartech.forge.server.misc.TemplateEngineFactory
import com.bolyartech.forge.server.misc.convertStreamToString
import com.bolyartech.forge.server.misc.saveUploadedFile
import com.bolyartech.forge.server.response.HtmlResponse
import com.bolyartech.forge.server.response.RedirectResponse303SeeOther
import com.bolyartech.forge.server.response.Response
import com.bolyartech.forge.server.route.MissingParameterValueException
import com.bolyartech.forge.server.route.RequestContext
import com.bolyartech.forge.test.dagger.UploadsDir
import jakarta.servlet.http.Part
import java.nio.file.FileSystem
import javax.inject.Inject

class UserUploadWp @Inject constructor(
    templateEngineFactory: TemplateEngineFactory,
    private val fileSystem: FileSystem,
    @UploadsDir private val uploadsDir: String
) : WebPage(templateEngineFactory) {

    override fun handlePage(ctx: RequestContext, tple: TemplateEngine): Response {
        return if (ctx.isMethod(HttpMethod.GET)) {
            modeGet(tple)
        } else {
            modePost(ctx, tple)
        }
    }

    private fun modeGet(tple: TemplateEngine): Response {
        tple.export("mode", "form")
        return common(tple)
    }

    private fun modePost(ctx: RequestContext, tple: TemplateEngine): Response {
        val txtPart: Part = ctx.getPart("text_field") ?: throw MissingParameterValueException()
        val file: Part = ctx.getPart("file_field") ?: throw MissingParameterValueException()

        saveUploadedFile(file.inputStream, fileSystem.getPath(uploadsDir, "uploaded_file"))

        return RedirectResponse303SeeOther("/user_upload_done?param=${convertStreamToString(txtPart.inputStream)}")
    }


    private fun common(tple: TemplateEngine): Response {
        tple.export("_page", "user_upload")
        return HtmlResponse(tple.render("template.vm"))
    }
}