package com.trendyol.android.devtools.debugmenu.internal.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trendyol.android.devtools.debugmenu.DebugActionItem

internal class DebugMenuUseCase constructor(
    private val debugActions: MutableLiveData<List<DebugActionItem>> = MutableLiveData(mutableListOf())
) {

    fun getDebugActions(): LiveData<List<DebugActionItem>> = debugActions

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        val currentList = this.debugActions.value?.toMutableList() ?: mutableListOf()
        this.debugActions.value = currentList + debugActions
    }

    fun onDebugActionItemClick(index: Int) {
        (debugActions.value?.get(index) as? DebugActionItem.Clickable)?.onClickItem()
    }

    fun onDebugActionItemChecked(index: Int, checked: Boolean): Boolean =
        (debugActions.value?.get(index) as DebugActionItem.Switchable).onCheckboxStatusChanged(checked)
}
