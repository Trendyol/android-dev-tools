package com.trendyol.android.devtools.analyticslogger.internal.ext

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView

/**
 * Hides the soft keyboard from the current view
 */
internal fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Sets up RecyclerView to hide keyboard when scrolling or touching
 */
@SuppressLint("ClickableViewAccessibility")
internal fun androidx.recyclerview.widget.RecyclerView.setupHideKeyboardOnScroll() {
    addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING) {
                hideKeyboard()
            }
        }
    })
    
    // Also hide on touch to handle taps on items
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.hideKeyboard()
        }
        false // Don't consume the event
    }
}

/**
 * Sets up NestedScrollView to hide keyboard when scrolling or touching
 */
@SuppressLint("ClickableViewAccessibility")
internal fun androidx.core.widget.NestedScrollView.setupHideKeyboardOnScroll() {
    setOnScrollChangeListener { _: androidx.core.widget.NestedScrollView, _: Int, _: Int, _: Int, _: Int ->
        hideKeyboard()
    }
    
    // Also hide on touch
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.hideKeyboard()
        }
        false // Don't consume the event
    }
}

/**
 * Sets up touch listener to hide keyboard when clicking outside of SearchView
 * 
 * Note: SuppressLint is used because we're not handling clicks - we're only hiding the keyboard
 * and letting the event propagate normally (returning false). This is a UX enhancement, not
 * an accessibility feature, so performClick() is not needed here.
 */
@SuppressLint("ClickableViewAccessibility")
internal fun View.setupHideKeyboardOnTouch() {
    // Skip if not a container
    if (this !is ViewGroup) return

    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Check if the touch is outside any SearchView
            if (!isTouchInsideSearchView(this, event)) {
                v.hideKeyboard()
                v.clearFocus()
            }
        }
        false // Don't consume the event, let it propagate
    }
}

/**
 * Recursively checks if the touch event is inside a SearchView or its children
 */
private fun isTouchInsideSearchView(viewGroup: ViewGroup, event: MotionEvent): Boolean {
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        
        if (child is SearchView) {
            // Check if touch is within SearchView bounds
            val location = IntArray(2)
            child.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            
            if (event.rawX >= x && event.rawX <= x + child.width &&
                event.rawY >= y && event.rawY <= y + child.height) {
                return true
            }
        }
        
        if (child is ViewGroup) {
            if (isTouchInsideSearchView(child, event)) {
                return true
            }
        }
    }
    return false
}

