package com.trendyol.devtools.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.trendyol.devtools.MainActivity
import com.trendyol.devtools.TrendyolDevTools
import com.trendyol.devtools.databinding.MainFragmentBinding
import com.trendyol.devtools.ui.card.CardFragment
import com.trendyol.devtools.ui.login.LoginFragment
import kotlin.random.Random

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        MainFragmentBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.button.setOnClickListener {
            TrendyolDevTools.show()
        }
        binding.buttonUpdateEnvironments.setOnClickListener {
            val environments = (1..6).toList().map { Random.nextInt(0, 100) }.distinct().map { "env $it" }
            TrendyolDevTools.updateEnvironments(environments)
        }
        binding.buttonOpenLogin.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(
                LoginFragment.newInstance(),
                LoginFragment.FRAGMENT_TAG
            )
        }
        binding.buttonOpenCard.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(
                CardFragment.newInstance(),
                CardFragment.FRAGMENT_TAG
            )
        }

        binding.message.text = "current environment: ${TrendyolDevTools.getCurrentEnvironment()}"

        TrendyolDevTools.getEnvironmentChangedLiveData().observe(viewLifecycleOwner) {
            binding.message.text = "current environment: $it"
        }

        TrendyolDevTools.getDebugActionClickEvent().observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it.text, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}
