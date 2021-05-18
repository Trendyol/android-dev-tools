package com.trendyol.devtools

import android.app.Application
import android.content.Intent
import com.trendyol.devtools.internal.di.ContextContainer
import com.trendyol.devtools.internal.domain.EnvironmentUseCase
import com.trendyol.devtools.internal.service.DevToolsService
import com.trendyol.devtools.internal.ui.MainActivity
import com.trendyol.devtools.model.DefaultEnvironments

object TrendyolDevTools {

    fun init(application: Application, environments: List<String> = DefaultEnvironments.getAll()) {
        ContextContainer.setContext(application)
        ContextContainer.setEnvironments(environments)
        DevToolsService.initializeService(application)
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun updateEnvironments(environments: List<String>) {
        ContextContainer.setEnvironments(environments)
    }

    fun getCurrentEnvironment(): String = EnvironmentUseCase.getInstance().getCurrentEnvironment()
}
