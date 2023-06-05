package com.trendyol.android.devtools.debugmenu.internal.ui

import androidx.recyclerview.widget.DiffUtil
import com.trendyol.android.devtools.debugmenu.DebugActionItem

class DebugMenuItemDiffCallback : DiffUtil.ItemCallback<DebugActionItem>() {

    override fun areItemsTheSame(oldItem: DebugActionItem, newItem: DebugActionItem): Boolean {
        val isTextsSame = oldItem.text == newItem.text
        val isDescriptionsSame = oldItem.description == newItem.description
        val isIconsSame = oldItem.iconDrawableRes == newItem.iconDrawableRes
        val isLastStatesSame = (oldItem as? DebugActionItem.Switchable)?.lastState ==
            (newItem as? DebugActionItem.Switchable)?.lastState
        return isTextsSame && isDescriptionsSame && isIconsSame && isLastStatesSame
    }

    override fun areContentsTheSame(oldItem: DebugActionItem, newItem: DebugActionItem): Boolean {
        return oldItem == newItem
    }
}
