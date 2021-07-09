package com.trendyol.devtools

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import com.trendyol.devtools.internal.debugmenu.DebugActionItem
import com.trendyol.devtools.internal.debugmenu.DebugMenuUseCase
import com.trendyol.devtools.internal.di.ContextContainer
import com.trendyol.devtools.internal.domain.EnvironmentUseCase
import com.trendyol.devtools.internal.domain.EnvironmentValidator
import com.trendyol.devtools.internal.service.DevToolsService
import com.trendyol.devtools.internal.ui.MainActivity

object TrendyolDevTools {

    private lateinit var environmentsUseCase: EnvironmentUseCase
    private lateinit var debugMenuUseCase: DebugMenuUseCase

    fun init(application: Application) {
        ContextContainer.setContext(application)
        debugMenuUseCase = ContextContainer.debugMenuContainer.debugMenuUseCase
        environmentsUseCase = ContextContainer.environmentsContainer.environmentUseCase
        DevToolsService.initializeService(application)
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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

    fun setDebugActionItems(debugActionItems: List<DebugActionItem>) {
        debugMenuUseCase.setDebugActionItems(debugActionItems)
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        debugMenuUseCase.addDebugActionItems(debugActions)
    }

    fun addDebugAction(debugAction: DebugActionItem) {
        debugMenuUseCase.addDebugAction(debugAction)
    }

    fun getDebugActionClickEvent(): LiveData<DebugActionItem> = debugMenuUseCase.getDebugActionClickEvent()
}
