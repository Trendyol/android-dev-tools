package com.trendyol.android.devtools.debugactionitem

import com.trendyol.android.devtools.R
import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import com.trendyol.android.devtools.debugmenu.DebugActionItem

class AnalyticsLoggerDebugActionItem : DebugActionItem.Switchable(
    text = "Analytics Logger",
    iconDrawableRes = R.drawable.ic_analytics,
    description = "Switch to hide notifications.",
    initialState = true,
) {

    override fun onClickItem() {
        AnalyticsLogger.show()
    }

    override fun onCheckboxStatusChanged(isChecked: Boolean) {
        if (isChecked) {
            AnalyticsLogger.showNotification()
            updateDescription("Switch to hide notifications.")
        } else {
            AnalyticsLogger.hideNotification()
            updateDescription("Switch to show notifications.")
        }
    }
}
