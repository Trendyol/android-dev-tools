package com.trendyol.android.devtools.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trendyol.android.devtools.MainActivity
import com.trendyol.android.devtools.R
import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import com.trendyol.android.devtools.databinding.MainFragmentBinding
import com.trendyol.android.devtools.debugmenu.DebugMenu
import com.trendyol.android.devtools.ui.login.LoginFragment
import com.trendyol.devtools.deeplinklauncher.DeepLinkLauncher
import com.trendyol.devtools.environmentmanager.EnvironmentManager
import kotlin.random.Random

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        MainFragmentBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonEnvironmentManager.setOnClickListener {
            EnvironmentManager.show()
        }
        binding.buttonUpdateEnvironments.setOnClickListener {
            val environments = (1..6).toList().map { Random.nextInt(0, 100) }.distinct().map { "env $it" }
            EnvironmentManager.updateEnvironments(environments)
        }
        binding.buttonDebugMenu.setOnClickListener {
            DebugMenu.show()
        }
        binding.buttonAutofillService.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(
                LoginFragment.newInstance(),
                LoginFragment.FRAGMENT_TAG
            )
        }

        binding.buttonDeepLinkLauncher.setOnClickListener {
            DeepLinkLauncher.show()
        }

        binding.buttonAnalyticsLogger.setOnClickListener {
            AnalyticsLogger.show()
        }

        binding.switchAnalyticsLogger.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AnalyticsLogger.showNotification()
            } else {
                AnalyticsLogger.hideNotification()
            }
        }

        binding.message.text = getString(R.string.current_environment, EnvironmentManager.getCurrentEnvironment())

        EnvironmentManager.getEnvironmentChangedLiveData().observe(viewLifecycleOwner) {
            binding.message.text = getString(R.string.current_environment, it)
        }

        AnalyticsLogger.report(
            key = "OnMainFragmentSeenEvent",
            value = "{\"category\": \"Cart\", \"data\": \"TestData\" }",
            platform = "Firebase",
        )
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}
