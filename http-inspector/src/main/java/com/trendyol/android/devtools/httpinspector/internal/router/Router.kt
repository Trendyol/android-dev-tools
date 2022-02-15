package com.trendyol.android.devtools.httpinspector.internal.router

import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.routing.routing

internal abstract class Router {

    abstract fun init(routing: Routing)

    fun run(application: Application) = application.routing {
        init(this)
    }
}
