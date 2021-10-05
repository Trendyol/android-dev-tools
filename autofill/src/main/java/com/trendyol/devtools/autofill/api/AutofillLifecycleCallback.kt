package com.trendyol.devtools.autofill.api

import android.app.Activity
import android.app.Application
import android.os.Bundle

abstract class AutofillLifecycleCallback : Application.ActivityLifecycleCallbacks {

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
