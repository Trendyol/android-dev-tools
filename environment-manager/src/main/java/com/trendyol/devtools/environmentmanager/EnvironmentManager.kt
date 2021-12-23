package com.trendyol.devtools.environmentmanager

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import com.trendyol.devtools.environmentmanager.internal.di.ContextContainer
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentUseCase
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentValidator
import com.trendyol.devtools.environmentmanager.internal.service.EnvironmentService
import com.trendyol.devtools.environmentmanager.internal.ui.MainActivity

object EnvironmentManager {

    private lateinit var environmentsUseCase: EnvironmentUseCase

    fun init(application: Application) {
        ContextContainer.setContext(application)
        environmentsUseCase = ContextContainer.environmentsContainer.environmentUseCase
        EnvironmentService.initializeService(application)
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun updateEnvironments(environments: List<String>) {
        EnvironmentValidator.validateEnvironments(environments)
        environmentsUseCase.updateEnvironments(environments)
    }

    fun getCurrentEnvironment(): String =
        environmentsUseCase.getCurrentEnvironment()

    fun getEnvironmentChangedLiveData(): LiveData<String> {
        return environmentsUseCase.getEnvironmentChangedLiveData()
    }
}
