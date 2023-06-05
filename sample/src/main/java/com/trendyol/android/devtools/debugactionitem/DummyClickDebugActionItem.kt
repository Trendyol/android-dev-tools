package com.trendyol.android.devtools.debugactionitem

import android.content.Context
import android.widget.Toast
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugtoast.DebugToast

class DummyClickDebugActionItem(private val context: Context) : DebugActionItem.Clickable(
    text = "Clickable",
    iconDrawableRes = android.R.drawable.ic_dialog_alert,
    description = "Click to see debug toast message!",
) {

    private var counter: Int = 1

    override fun onClickItem() {
        DebugToast.show(context, "Clicked this button ${counter++} times!", Toast.LENGTH_LONG)
    }
}
