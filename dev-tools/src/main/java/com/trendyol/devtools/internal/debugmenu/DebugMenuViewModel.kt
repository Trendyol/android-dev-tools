package com.trendyol.devtools.internal.debugmenu

import androidx.lifecycle.ViewModel

internal class DebugMenuViewModel constructor(private val debugMenuUseCase: DebugMenuUseCase) : ViewModel() {

    fun getDebugActions() = debugMenuUseCase.getDebugActions()

    fun onDebugActionItemClick(debugActionItemClick: DebugActionItem) {
        debugMenuUseCase.onDebugActionItemClick(debugActionItemClick)
    }
}
