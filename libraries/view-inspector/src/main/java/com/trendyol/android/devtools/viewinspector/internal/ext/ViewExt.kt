package com.trendyol.android.devtools.viewinspector.internal.ext

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

internal fun View.findViews(): List<View> {
    return mutableListOf<View>().apply {
        when (this@findViews) {
            is ViewGroup -> {
                children.forEach { child ->
                    addAll(child.findViews())
                }
            }
            else -> add(this@findViews)
        }
    }
}

internal fun View.getHitBoxRect(): Rect {
    val rect = Rect()
    val coordinates = IntArray(2)
    getLocationOnScreen(coordinates)
    rect.set(coordinates[0], coordinates[1], coordinates[0] + width, coordinates[1] + height)
    return rect
}

internal fun View.getResourceId(context: Context): String? {
    return runCatching { context.resources.getResourceEntryName(id) }.getOrNull()
}
