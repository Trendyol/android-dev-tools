package com.trendyol.android.devtools.viewinspector.internal.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trendyol.android.devtools.viewinspector.internal.ext.getView

internal abstract class ViewLifecycleCallback :
    FragmentManager.FragmentLifecycleCallbacks(), Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Can be overridden
    }

    override fun onActivityResumed(activity: Activity) {
        // Can be overridden
    }

    override fun onActivityStarted(activity: Activity) {
        // Can be overridden
    }

    override fun onActivityPaused(activity: Activity) {
        // Can be overridden
    }

    override fun onActivityStopped(activity: Activity) {
        // Can be overridden
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Can be overridden
    }

    override fun onActivityDestroyed(activity: Activity) {
        // Can be overridden
    }
}
