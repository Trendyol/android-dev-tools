package com.trendyol.android.devtools.analyticslogger.internal.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.ui.events.EventsFragment

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analytics_logger_activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = getString(R.string.analytics_logger_title)
        initializeNavigation()
    }

    private fun initializeNavigation() {
        navigate(EventsFragment.newInstance())
    }

    fun navigate(fragment: Fragment, name: String? = null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .apply { if (name.isNullOrEmpty().not()) addToBackStack(name) }
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
