package com.trendyol.android.devtools.debugmenu.internal.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase

internal class DebugMenuViewModel constructor(private val debugMenuUseCase: DebugMenuUseCase) : ViewModel() {

    fun getDebugActions(): LiveData<List<DebugActionItem>> = debugMenuUseCase.getDebugActions()

    fun onDebugActionItemClick(index: Int) {
        debugMenuUseCase.onDebugActionItemClick(index)
    }

    fun onDebugActionItemChecked(index: Int, checked: Boolean): Boolean =
        debugMenuUseCase.onDebugActionItemChecked(index, checked)
}
