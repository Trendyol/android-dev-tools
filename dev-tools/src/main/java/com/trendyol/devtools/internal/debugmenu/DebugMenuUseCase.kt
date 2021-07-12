package com.trendyol.devtools.internal.debugmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trendyol.devtools.internal.util.SingleLiveEvent

internal class DebugMenuUseCase constructor(
    private val clickEvent: SingleLiveEvent<DebugActionItem> = SingleLiveEvent(),
    private val debugActions: MutableLiveData<List<DebugActionItem>> = MutableLiveData(mutableListOf())
) {

    fun getDebugActions(): LiveData<List<DebugActionItem>> = debugActions

    fun getDebugActionClickEvent(): LiveData<DebugActionItem> = clickEvent

    fun setDebugActionItems(debugActions: List<DebugActionItem>) {
        this.debugActions.value = debugActions.toMutableList()
    }

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        val currentList = this.debugActions.value?.toMutableList() ?: mutableListOf()
        this.debugActions.value = currentList + debugActions
    }

    fun addDebugAction(debugAction: DebugActionItem) {
        val currentList = this.debugActions.value?.toMutableList() ?: mutableListOf()
        this.debugActions.value = currentList.apply { add(debugAction) }
    }

    fun onDebugActionItemClick(debugActionItemClick: DebugActionItem) {
        this.clickEvent.value = debugActionItemClick
    }
}
