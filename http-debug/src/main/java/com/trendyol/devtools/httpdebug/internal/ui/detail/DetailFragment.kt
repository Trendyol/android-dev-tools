package com.trendyol.devtools.httpdebug.internal.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.trendyol.devtools.common.viewBinding
import com.trendyol.devtools.httpdebug.R
import com.trendyol.devtools.httpdebug.databinding.DevToolsHttpDebugFragmentDetailBinding
import com.trendyol.devtools.httpdebug.internal.di.DetailContainer
import com.trendyol.devtools.httpdebug.internal.ui.detail.request.RequestTabFragment
import com.trendyol.devtools.httpdebug.internal.ui.detail.response.ResponseTabFragment

internal class DetailFragment : Fragment(R.layout.dev_tools_http_debug_fragment_detail) {

    private val binding by viewBinding(DevToolsHttpDebugFragmentDetailBinding::bind)
    private val viewModel by viewModels<DetailViewModel> { DetailContainer().DetailViewModelFactory() }
    private val id by lazy { requireArguments().getString(KEY_ID)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.devToolsHttpDebugFabDetail.setOnClickListener {
            viewModel.skipRequest(id, binding.devToolsHttpDebugTabLayoutDetail.selectedTabPosition == 0)
        }

        binding.devToolsHttpDebugTabLayoutDetail.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab) {
                    binding.devToolsHttpDebugTabLayoutDetail.getTabAt(0) -> openRequestTab()
                    binding.devToolsHttpDebugTabLayoutDetail.getTabAt(1) -> openResponseTab()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // do nothing
            }
        })

        if (savedInstanceState == null) {
            openRequestTab()
        }
    }

    private fun openRequestTab() {
        childFragmentManager
            .beginTransaction()
            .replace(binding.devToolsHttpDebugContainerDetail.id, RequestTabFragment.newInstance(id))
            .commit()
    }

    private fun openResponseTab() {
        childFragmentManager
            .beginTransaction()
            .replace(binding.devToolsHttpDebugContainerDetail.id, ResponseTabFragment.newInstance(id))
            .commit()
    }

    companion object {

        private const val KEY_ID = "key_id"

        fun newInstance(id: String): DetailFragment =
            DetailFragment().apply {
                arguments = bundleOf(KEY_ID to id)
            }
    }
}
