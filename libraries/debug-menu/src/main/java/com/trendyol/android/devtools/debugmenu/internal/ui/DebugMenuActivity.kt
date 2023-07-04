package com.trendyol.android.devtools.debugmenu.internal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trendyol.android.devtools.debugmenu.R
import com.trendyol.android.devtools.debugmenu.databinding.DebugMenuActivityBinding

internal class DebugMenuActivity : AppCompatActivity() {

    private lateinit var binding: DebugMenuActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DebugMenuActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            startMainFragment()
        }
    }

    private fun startMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainerViewMain,
                DebugMenuFragment.newInstance(requireNotNull(intent.getStringExtra(KEY_TITLE))),
            )
            .commit()
    }

    companion object {

        internal const val KEY_TITLE: String = "key_title"

        internal fun newIntent(context: Context, title: String): Intent =
            Intent(context, DebugMenuActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(KEY_TITLE, title)
    }
}
