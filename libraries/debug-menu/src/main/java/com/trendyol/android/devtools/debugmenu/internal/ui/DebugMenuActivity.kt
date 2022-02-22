package com.trendyol.android.devtools.debugmenu.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trendyol.android.devtools.debugmenu.R
import com.trendyol.android.devtools.debugmenu.databinding.DevToolsActivityMainBinding

internal class DebugMenuActivity : AppCompatActivity() {

    private lateinit var binding: DevToolsActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DevToolsActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            startMainFragment()
        }
    }

    private fun startMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewMain, DebugMenuFragment.newInstance())
            .commit()
    }
}
