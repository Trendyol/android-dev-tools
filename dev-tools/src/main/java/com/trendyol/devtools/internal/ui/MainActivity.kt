package com.trendyol.devtools.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trendyol.devtools.R
import com.trendyol.devtools.databinding.DevToolsActivityMainBinding
import com.trendyol.devtools.internal.main.MainFragment

internal class MainActivity : AppCompatActivity() {

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
            .replace(R.id.fragmentContainerViewMain, MainFragment.newInstance())
            .commit()
    }
}
