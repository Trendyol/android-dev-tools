package com.trendyol.devtools.autofill.internal.ext

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent

class FragmentViewObserver(private val f: () -> Unit) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        
    }
}

internal fun Fragment.getView(activity: AppCompatActivity, callback: ((View) -> Unit)) {
    val observer = FragmentViewObserver({
        activity.lifecycle.removeObserver()
    })
    activity.lifecycle.addObserver(observer)


}
