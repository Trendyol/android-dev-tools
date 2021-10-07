package com.trendyol.devtools.autofill.internal.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trendyol.devtools.autofill.internal.ext.getView

internal abstract class AutofillViewLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks(),
    Application.ActivityLifecycleCallbacks {

    abstract fun onActivityViewCreated(activity: Activity, view: View)

    abstract fun onFragmentViewCreated(activity: Activity, fragment: Fragment, view: View)

    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPostCreated(activity, savedInstanceState)
        activity.getView { view -> onActivityViewCreated(activity, view) }
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        onFragmentViewCreated(f.requireActivity(), f, v)
    }

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
