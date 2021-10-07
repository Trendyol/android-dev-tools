package com.trendyol.devtools.autofill.internal.ext

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity

internal fun Activity.asAppcompatActivity(): AppCompatActivity {
    return this as AppCompatActivity
}

internal fun Activity.getView(callback: ((View) -> Unit)) {

    val view: View? = findViewById(android.R.id.content)

    if (view != null && view.width != 0) {
        callback.invoke(view)
        return
    }

    view?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback.invoke(view)
            return
        }
    })
}
