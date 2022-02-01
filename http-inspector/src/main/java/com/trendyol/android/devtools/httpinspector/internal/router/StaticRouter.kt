package com.trendyol.android.devtools.httpinspector.internal.router

import android.content.Context
import com.trendyol.android.devtools.core.io.FileReader
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get

class StaticRouter(private val appContext: Context) : Router() {

    override fun init(routing: Routing)  {
        routing.get("/") {
            val fileData = FileReader.readAssetFile(appContext, "control.html")
            call.respondText(
                status = HttpStatusCode.OK,
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
        routing.get("/mock.html") {
            val fileData = FileReader.readAssetFile(appContext, "mock.html")
            call.respondText(
                status = HttpStatusCode.OK,
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
        routing.get("/add-mock.html") {
            val fileData = FileReader.readAssetFile(appContext, "add-mock.html")
            call.respondText(
                status = HttpStatusCode.OK,
                text = fileData.orEmpty(),
                contentType = ContentType.Text.Html,
            )
        }
    }
}
