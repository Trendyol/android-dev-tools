package com.trendyol.android.devtools.debugmenu

import androidx.annotation.DrawableRes
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase

sealed class DebugActionItem(
    internal open var text: String,
    @get:DrawableRes internal open var iconDrawableRes: Int,
    internal open var description: String?,
) {

    abstract class Clickable(
        override var text: String,
        @get:DrawableRes override var iconDrawableRes: Int,
        override var description: String?,
    ) : DebugActionItem(text, iconDrawableRes, description) {

        abstract fun onClickItem()
    }

    abstract class Switchable(
        override var text: String,
        @get:DrawableRes override var iconDrawableRes: Int,
        override var description: String?,
        initialState: Boolean = false,
    ) : Clickable(text, iconDrawableRes, description) {

        internal var lastState = initialState

        override fun onClickItem() {
            // do nothing
        }

        abstract fun onCheckboxStatusChanged(isChecked: Boolean)
    }

    private val debugMenuUseCase: DebugMenuUseCase by lazy { DebugMenu.koin.get() }

    fun updateDescription(newDescription: String) {
        description = newDescription
        debugMenuUseCase.onDebugActionItemUpdated(this)
    }

    fun updateText(newText: String) {
        text = newText
        debugMenuUseCase.onDebugActionItemUpdated(this)
    }

    fun updateIconDrawableRes(@DrawableRes newIconDrawableRes: Int) {
        iconDrawableRes = newIconDrawableRes
        debugMenuUseCase.onDebugActionItemUpdated(this)
    }
}
