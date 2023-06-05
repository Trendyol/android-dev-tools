package com.trendyol.android.devtools.debugactionitem

import com.trendyol.android.devtools.debugmenu.DebugActionItem

class DummySwitchDebugActionItem : DebugActionItem.Switchable(
    text = "Not yet clicked",
    iconDrawableRes = android.R.drawable.ic_lock_lock,
    description = "Not yet switched!",
) {

    private var clickCounter = 1
    private var switchCounter = 1

    override fun onClickItem() {
        updateText("Clicked ${clickCounter++} times")
    }

    override fun onCheckboxStatusChanged(isChecked: Boolean) {
        val status = if (isChecked) "active" else "disabled"
        updateDescription("Status is $status and  changed ${switchCounter++} times.")
    }
}
