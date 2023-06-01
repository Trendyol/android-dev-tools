package com.trendyol.android.devtools.debugmenu

import androidx.annotation.DrawableRes

sealed class DebugActionItem(
    internal open val text: String,
    @get:DrawableRes internal open val iconDrawable: Int,
    internal open val description: String?,
) {

    abstract class Clickable(
        override val text: String,
        @get:DrawableRes override val iconDrawable: Int,
        override val description: String?,
    ) : DebugActionItem(text, iconDrawable, description) {

        abstract fun onClickItem()
    }

    abstract class Switchable(
        override val text: String,
        @get:DrawableRes override val iconDrawable: Int,
        override val description: String?,
        internal val initialState: Boolean = false,
    ) : DebugActionItem(text, iconDrawable, description) {

        abstract fun onCheckboxStatusChanged(isChecked: Boolean): Boolean
    }
}
