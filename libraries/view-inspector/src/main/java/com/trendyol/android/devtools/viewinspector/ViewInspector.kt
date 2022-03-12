package com.trendyol.android.devtools.viewinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trendyol.android.devtools.viewinspector.internal.ext.findViews
import com.trendyol.android.devtools.viewinspector.internal.ext.getHitBoxRect
import com.trendyol.android.devtools.viewinspector.internal.ext.getResourceId
import com.trendyol.android.devtools.viewinspector.internal.ext.getSupportFragmentManager
import com.trendyol.android.devtools.viewinspector.internal.lifecycle.ViewLifecycleCallback
import com.trendyol.android.devtools.viewinspector.internal.window.WindowEventCallback
import java.lang.ref.WeakReference

class ViewInspector {

    internal class ViewInspectorProcessor(private val application: Application) : ViewLifecycleCallback() {

        private val viewMap = mutableMapOf<Fragment, List<WeakReference<View>>>()

        override fun onActivityViewCreated(activity: Activity, view: View) {
            val win: Window = activity.window
            val localCallback: Window.Callback = win.callback
            win.callback = WindowEventCallback(localCallback, onWindowTouchEvent)
        }

        private val onWindowTouchEvent = { event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                viewMap.values.forEach { views ->
                    views.forEach { viewRef ->
                        val view = viewRef.get()
                        if (view?.isVisible == true) {
                            val hitBox = view.getHitBoxRect()
                            if (hitBox.contains(event.rawX.toInt(), event.rawY.toInt())) {
                                val resId = view.getResourceId(application)
                                Toast.makeText(application, "Res: $resId", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onFragmentViewCreated(activity: Activity, fragment: Fragment, view: View) {
            view.post { viewMap[fragment] = view.findViews().map { WeakReference(it) } }
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, fragment: Fragment) {
            super.onFragmentViewDestroyed(fm, fragment)
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

    class Builder(private val application: Application) {

        fun build() {
            ViewInspectorProcessor(application = application)
        }
    }
}
