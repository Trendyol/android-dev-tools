package com.trendyol.android.devtools.viewinspector

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trendyol.android.devtools.viewinspector.internal.ext.findViews
import com.trendyol.android.devtools.viewinspector.internal.ext.getHitBoxRect
import com.trendyol.android.devtools.viewinspector.internal.ext.getResourceId
import com.trendyol.android.devtools.viewinspector.internal.ext.getSupportFragmentManager
import com.trendyol.android.devtools.viewinspector.internal.ext.orZero
import com.trendyol.android.devtools.viewinspector.internal.lifecycle.ViewLifecycleCallback
import com.trendyol.android.devtools.viewinspector.internal.window.WindowEventCallback
import java.lang.ref.WeakReference
import java.util.WeakHashMap

class ViewInspector {

    internal class ViewInspectorProcessor(private val application: Application) : ViewLifecycleCallback() {

        private val viewMap = WeakHashMap<Fragment, WeakReference<View>>()

        private val gestureDetector = GestureDetectorCompat(
            application,
            object : SimpleOnGestureListener() {
                override fun onLongPress(event: MotionEvent?) {
                    viewMap.filterKeys { it.isVisible }.values
                        .asSequence()
                        .flatMap { it.get()?.findViews().orEmpty() }
                        .filter { it.isVisible }
                        .filter { view ->
                            view.getHitBoxRect().contains(
                                event?.rawX?.toInt().orZero(),
                                event?.rawY?.toInt().orZero(),
                            )
                        }
                        .lastOrNull()
                        ?.let { view -> showResourceIdMessage(view.getResourceId(application)) }
                }
            }
        )

        private fun showResourceIdMessage(resId: String?) {
            if (resId.isNullOrEmpty()) return
            Toast.makeText(application, resId, Toast.LENGTH_SHORT).show()
        }

        override fun onFragmentResumed(fragmentManager: FragmentManager, fragment: Fragment) {
            super.onFragmentResumed(fragmentManager, fragment)
            val window = fragment.activity?.window ?: return
            val localCallback = window.callback
            window.callback = WindowEventCallback(localCallback, gestureDetector::onTouchEvent)
        }

        override fun onFragmentViewCreated(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            view: View,
            savedInstanceState: Bundle?,
        ) {
            super.onFragmentViewCreated(fragmentManager, fragment, view, savedInstanceState)
            viewMap[fragment] = WeakReference(view)
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, fragment: Fragment) {
            super.onFragmentViewDestroyed(fm, fragment)
            viewMap.remove(fragment)
        }

        override fun onFragmentAttached(fm: FragmentManager, fragment: Fragment, context: Context) {
            super.onFragmentAttached(fm, fragment, context)
            if (fragment.view != null) viewMap[fragment] = WeakReference(fragment.view)
        }

        override fun onFragmentDetached(fm: FragmentManager, fragment: Fragment) {
            super.onFragmentDetached(fm, fragment)
            viewMap.remove(fragment)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            super.onActivityCreated(activity, savedInstanceState)
            activity.getSupportFragmentManager()
                ?.registerFragmentLifecycleCallbacks(this, true)
        }

        override fun onActivityDestroyed(activity: Activity) {
            super.onActivityDestroyed(activity)
            activity.getSupportFragmentManager()
                ?.unregisterFragmentLifecycleCallbacks(this)
        }

        init {
            application.registerActivityLifecycleCallbacks(this)
        }
    }

    companion object {

        fun init(application: Application) {
            ViewInspectorProcessor(application = application)
        }
    }
}
