package com.trendyol.devtools.deeplinklauncher.internal.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.databinding.DeeplinkLauncherFragmentMainBinding
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ext.viewBinding

class MainFragment : Fragment(R.layout.deeplink_launcher_fragment_main) {

    private val binding: DeeplinkLauncherFragmentMainBinding by viewBinding(DeeplinkLauncherFragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }

    companion object{
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
