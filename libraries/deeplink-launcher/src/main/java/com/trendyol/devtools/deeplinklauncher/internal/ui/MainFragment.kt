package com.trendyol.devtools.deeplinklauncher.internal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.google.android.material.snackbar.Snackbar
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.databinding.DeeplinkLauncherFragmentMainBinding
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ext.getView
import com.trendyol.devtools.deeplinklauncher.internal.ext.viewBinding
import java.lang.Exception

class MainFragment : Fragment(R.layout.deeplink_launcher_fragment_main) {

    private val binding: DeeplinkLauncherFragmentMainBinding by viewBinding(DeeplinkLauncherFragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }
    private val adapter: DeeplinkHistoryAdapter = DeeplinkHistoryAdapter().apply {
        onHistoryItemClicked = {
            fillEditText(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        clickListeners()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val recyclerHistory = binding.recyclerHistory
        val dividerItemDecoration = DividerItemDecoration(
            recyclerHistory.context,
            VERTICAL
        ).apply {
            ContextCompat.getDrawable(recyclerHistory.context, R.drawable.drawable_item_space_decorator)?.let {
                setDrawable(it)
            }
        }
        recyclerHistory.addItemDecoration(dividerItemDecoration)
        recyclerHistory.adapter = adapter
    }

    private fun observe() {
        viewModel.getHistory().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getNotValidDeepLinkEvent().observe(viewLifecycleOwner) {
            showSnackBar(it)
        }
        viewModel.getLaunchDeepLinkEvent().observe(viewLifecycleOwner) {
            fireDeeplink(it)
        }
    }

    private fun clickListeners() {
        fireDeepLinkListener()
    }

    private fun fireDeeplink(deepLinkText: String) {
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
