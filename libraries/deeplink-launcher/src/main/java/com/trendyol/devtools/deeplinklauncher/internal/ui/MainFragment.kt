package com.trendyol.devtools.deeplinklauncher.internal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.databinding.DeeplinkLauncherFragmentMainBinding
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ext.getView
import com.trendyol.devtools.deeplinklauncher.internal.ext.viewBinding
import com.trendyol.devtools.deeplinklauncher.internal.ui.list.DeepLinkListSharedViewModel
import java.lang.Exception

class MainFragment : Fragment(R.layout.deeplink_launcher_fragment_main) {

    private val binding: DeeplinkLauncherFragmentMainBinding by viewBinding(DeeplinkLauncherFragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }
    private val deepLinkSharedViewModel: DeepLinkListSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        clickListeners()
        setPagerAdapter()
    }

    private fun setPagerAdapter() {
        binding.viewPagerDeepLinks.adapter = DeepLinkPagerAdapter(this)

        val tabTitles = listOf(
            getString(R.string.deeplink_list_history),
            getString(R.string.deeplink_list_app)
        )

        TabLayoutMediator(
            binding.tabLayoutDeepLinkListContainer,
            binding.viewPagerDeepLinks
        ) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun observe() {
        viewModel.getNotValidDeepLinkEvent().observe(viewLifecycleOwner) {
            showSnackBar(it)
        }
        viewModel.getLaunchDeepLinkEvent().observe(viewLifecycleOwner) {
            fireDeepLink(it)
        }
        deepLinkSharedViewModel.selectedDeepLink.observe(viewLifecycleOwner) {
            fillEditText(it)
        }
    }

    private fun clickListeners() {
        fireDeepLinkListener()
    }

    private fun fireDeepLink(deepLinkText: String) {
        try {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(deepLinkText)
            }
            startActivity(intent)
        } catch (e: Exception) {
            showSnackBar(R.string.no_activity_found)
        }
    }

    private fun fireDeepLinkListener() {
        binding.buttonFire.setOnClickListener {
            val deepLinkText = binding.editTextDeepLink.text.toString()
            viewModel.onDeepLinkFired(deepLinkText)
        }
    }

    private fun showSnackBar(stringResId: Int) {
        activity?.getView { activityView ->
            Snackbar.make(activityView, stringResId, Snackbar.LENGTH_LONG)
                .setDuration(2000)
                .show()
        }
    }

    private fun fillEditText(it: String) {
        binding.editTextDeepLink.setText(it)
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
