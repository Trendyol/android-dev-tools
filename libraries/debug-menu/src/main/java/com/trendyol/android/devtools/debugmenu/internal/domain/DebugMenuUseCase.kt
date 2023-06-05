package com.trendyol.android.devtools.debugmenu.internal.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.internal.lifecycle.SingleLiveEvent

internal class DebugMenuUseCase {

    private val items: MutableList<DebugActionItem> = mutableListOf()
    private val debugActions: MutableLiveData<List<DebugActionItem>> = MutableLiveData(mutableListOf())
    private val updateItemLiveData: SingleLiveEvent<Int> = SingleLiveEvent()

    fun getDebugActions(): LiveData<List<DebugActionItem>> = debugActions
    fun updateItemAtIndex(): SingleLiveEvent<Int> = updateItemLiveData

    fun addDebugActionItems(debugActions: List<DebugActionItem>) {
        items.addAll(debugActions)
        this.debugActions.postValue(items)
    }

    fun onDebugActionItemClick(index: Int) {
        (items.getOrNull(index) as? DebugActionItem.Clickable)?.onClickItem()
    }

    fun onDebugActionItemChecked(index: Int, checked: Boolean) {
        val debugActionItem = items.getOrNull(index) as? DebugActionItem.Switchable ?: return
        debugActionItem.lastState = checked
        debugActionItem.onCheckboxStatusChanged(checked)
    }

    fun onDebugActionItemUpdated(debugActionItem: DebugActionItem) {
        val indexToBeUpdated = items.indexOfFirst { it == debugActionItem }.takeIf { it != -1 } ?: return

        updateItemLiveData.postValue(indexToBeUpdated)
    }
}
