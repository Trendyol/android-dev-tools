package com.trendyol.devtools.deeplinklauncher.internal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.databinding.DeeplinkLauncherFragmentMainBinding
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ext.viewBinding

class MainFragment : Fragment(R.layout.deeplink_launcher_fragment_main) {

    private val binding: DeeplinkLauncherFragmentMainBinding by viewBinding(DeeplinkLauncherFragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() {
        binding.buttonFire.setOnClickListener {
            if (binding.editTextDeepLink.text.toString().isEmpty().not()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(binding.editTextDeepLink.text.toString())
                }
                startActivity(intent)
            } else {
                //todo show error
            }
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
