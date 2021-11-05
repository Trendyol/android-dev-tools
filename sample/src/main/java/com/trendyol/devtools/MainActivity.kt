package com.trendyol.devtools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.trendyol.devtools.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            navigateToFragment(MainFragment.newInstance())
        }
    }

    fun navigateToFragment(fragment: Fragment, backStack: String? = null) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment).apply {
                if (backStack.isNullOrBlank().not()) addToBackStack(backStack)
            }
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
