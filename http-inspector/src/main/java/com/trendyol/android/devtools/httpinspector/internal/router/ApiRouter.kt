package com.trendyol.android.devtools.httpinspector.internal.router

import com.trendyol.android.devtools.httpinspector.internal.domain.controller.HttpController
import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

class ApiRouter(
    private val httpController: HttpController,
) : Router() {

    override fun init(routing: Routing) {
        with(routing) {
            get("/mock-data") { httpController.getMockData(call) }
            post("/add-mock") { httpController.saveMockData(call) }
            post("/delete-mock") { httpController.deleteMockData(call) }
        }
    }
}
