package com.trendyol.devtools.deeplinklauncher.internal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.databinding.DeeplinkLauncherActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: DeeplinkLauncherActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeeplinkLauncherActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            startMainFragment()
        }
    }

    private fun startMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewMain, MainFragment.newInstance())
            .commit()
    }
}
