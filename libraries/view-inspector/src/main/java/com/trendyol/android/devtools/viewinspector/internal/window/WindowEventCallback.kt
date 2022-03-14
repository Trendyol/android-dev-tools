package com.trendyol.android.devtools.viewinspector.internal.window

import android.os.Build
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.SearchEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi

internal class WindowEventCallback(
    private val localCallback: Window.Callback,
    private val onTouchEvent: ((MotionEvent) -> Unit),
) : Window.Callback {

    override fun dispatchKeyEvent(p0: KeyEvent?): Boolean {
        return localCallback.dispatchKeyEvent(p0)
    }

    override fun dispatchKeyShortcutEvent(p0: KeyEvent?): Boolean {
        return localCallback.dispatchKeyShortcutEvent(p0)
    }

    override fun dispatchTouchEvent(p0: MotionEvent?): Boolean {
        p0?.let { onTouchEvent.invoke(p0) }
        return localCallback.dispatchTouchEvent(p0)
    }

    override fun dispatchTrackballEvent(p0: MotionEvent?): Boolean {
        return localCallback.dispatchTrackballEvent(p0)
    }

    override fun dispatchGenericMotionEvent(p0: MotionEvent?): Boolean {
        return localCallback.dispatchGenericMotionEvent(p0)
    }

    override fun dispatchPopulateAccessibilityEvent(p0: AccessibilityEvent?): Boolean {
        return localCallback.dispatchPopulateAccessibilityEvent(p0)
    }

    override fun onCreatePanelView(p0: Int): View? {
        return localCallback.onCreatePanelView(p0)
    }

    override fun onCreatePanelMenu(p0: Int, p1: Menu): Boolean {
        return localCallback.onCreatePanelMenu(p0, p1)
    }

    override fun onPreparePanel(p0: Int, p1: View?, p2: Menu): Boolean {
        return localCallback.onPreparePanel(p0, p1, p2)
    }

    override fun onMenuOpened(p0: Int, p1: Menu): Boolean {
        return localCallback.onMenuOpened(p0, p1)
    }

    override fun onMenuItemSelected(p0: Int, p1: MenuItem): Boolean {
        return localCallback.onMenuItemSelected(p0, p1)
    }

    override fun onWindowAttributesChanged(p0: WindowManager.LayoutParams?) {
        return localCallback.onWindowAttributesChanged(p0)
    }

    override fun onContentChanged() {
        return localCallback.onContentChanged()
    }

    override fun onWindowFocusChanged(p0: Boolean) {
        return localCallback.onWindowFocusChanged(p0)
    }

    override fun onAttachedToWindow() {
        return localCallback.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        return localCallback.onDetachedFromWindow()
    }

    override fun onPanelClosed(p0: Int, p1: Menu) {
        return localCallback.onPanelClosed(p0, p1)
    }

    override fun onSearchRequested(): Boolean {
        return localCallback.onSearchRequested()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSearchRequested(p0: SearchEvent?): Boolean {
        return localCallback.onSearchRequested(p0)
    }

    override fun onWindowStartingActionMode(p0: ActionMode.Callback?): ActionMode? {
        return localCallback.onWindowStartingActionMode(p0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onWindowStartingActionMode(p0: ActionMode.Callback?, p1: Int): ActionMode? {
        return localCallback.onWindowStartingActionMode(p0, p1)
    }

    override fun onActionModeStarted(p0: ActionMode?) {
        localCallback.onActionModeStarted(p0)
    }

    override fun onActionModeFinished(p0: ActionMode?) {
        localCallback.onActionModeFinished(p0)
    }
}
