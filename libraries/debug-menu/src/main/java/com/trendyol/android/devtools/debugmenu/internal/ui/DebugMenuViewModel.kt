package com.trendyol.android.devtools.debugmenu.internal.ui

import androidx.lifecycle.ViewModel
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase

internal class DebugMenuViewModel constructor(private val debugMenuUseCase: DebugMenuUseCase) : ViewModel() {

    fun getDebugActions() = debugMenuUseCase.getDebugActions()

    fun onDebugActionItemClick(debugActionItemClick: DebugActionItem) {
        debugMenuUseCase.onDebugActionItemClick(debugActionItemClick)
    }
}
