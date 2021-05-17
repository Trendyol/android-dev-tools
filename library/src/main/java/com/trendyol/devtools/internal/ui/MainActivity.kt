package com.trendyol.devtools.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trendyol.devtools.databinding.DevToolsActivityMainBinding

internal class MainActivity : AppCompatActivity() {

    private lateinit var binding: DevToolsActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DevToolsActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
