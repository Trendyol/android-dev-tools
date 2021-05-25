package com.trendyol.devtools

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import com.trendyol.devtools.internal.di.ContextContainer
import com.trendyol.devtools.internal.domain.EnvironmentValidator
import com.trendyol.devtools.internal.service.DevToolsService
import com.trendyol.devtools.internal.ui.MainActivity

object TrendyolDevTools {

    fun init(application: Application) {
        ContextContainer.setContext(application)
        DevToolsService.initializeService(application)
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun updateEnvironments(environments: List<String>) {
        EnvironmentValidator.validateEnvironments(environments)
        ContextContainer.environmentsContainer.environmentUseCase.updateEnvironments(environments)
    }

    fun getCurrentEnvironment(): String =
        ContextContainer.environmentsContainer.environmentUseCase.getCurrentEnvironment()

    fun getEnvironmentChangedLiveData(): LiveData<String> {
        return ContextContainer.environmentsContainer.environmentUseCase.getEnvironmentChangedLiveData()
    }
}
