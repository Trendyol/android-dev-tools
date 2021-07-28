package com.trendyol.devtools.httpdebug.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.trendyol.devtools.httpdebug.R
import com.trendyol.devtools.httpdebug.internal.ui.list.ListFragment

private const val KEY_BACKSTACK = "http_debug_backstack"

internal class HttpDebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dev_tools_http_debug_activity)

        if (savedInstanceState == null) {
            initializeContainer()
        }
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.dev_tools_http_debug_container, fragment)
            .addToBackStack(KEY_BACKSTACK)
            .commit()
    }

    private fun initializeContainer() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.dev_tools_http_debug_container, ListFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
