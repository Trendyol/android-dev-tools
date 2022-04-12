package com.trendyol.android.devtools.viewinspector.internal.ext

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

internal fun Activity.getSupportFragmentManager(): FragmentManager? {
    return if (this is FragmentActivity) return supportFragmentManager else null
}
