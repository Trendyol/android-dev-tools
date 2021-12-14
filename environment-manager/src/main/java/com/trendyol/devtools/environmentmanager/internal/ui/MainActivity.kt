package com.trendyol.devtools.environmentmanager.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trendyol.devtools.environmentmanager.R
import com.trendyol.devtools.environmentmanager.databinding.DevToolsActivityMainBinding

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
