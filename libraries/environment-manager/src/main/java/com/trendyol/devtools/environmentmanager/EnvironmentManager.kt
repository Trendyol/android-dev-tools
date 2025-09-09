package com.trendyol.devtools.environmentmanager

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.trendyol.devtools.environmentmanager.internal.di.environmentManagerModule
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentUseCase
import com.trendyol.devtools.environmentmanager.internal.domain.EnvironmentValidator
import com.trendyol.devtools.environmentmanager.internal.service.EnvironmentService
import com.trendyol.devtools.environmentmanager.internal.ui.MainActivity
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.android.ext.koin.androidLogger
import embedded.koin.core.Koin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinApplication

object EnvironmentManager {

    internal lateinit var koin: Koin

    private val environmentsUseCase: EnvironmentUseCase by lazy { koin.get() }

    fun init(application: Application) {
        koin = koinApplication {
            androidContext(application.applicationContext)
            androidLogger(Level.DEBUG)

            modules(
                environmentManagerModule(
                    application = application,
                )
            )
        }.koin
        EnvironmentService.initializeService(application)
    }

    fun show() {
        val context = koin.get<Context>()
        context.startActivity(
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun updateEnvironments(environments: List<String>) {
        EnvironmentValidator.validateEnvironments(environments)
        environmentsUseCase.updateEnvironments(environments)
    }

    fun getCurrentEnvironment(): String {
        return environmentsUseCase.getCurrentEnvironment()
    }

    fun getEnvironmentChangedLiveData(): LiveData<String> {
        return environmentsUseCase.getEnvironmentChangedLiveData()
    }
}
