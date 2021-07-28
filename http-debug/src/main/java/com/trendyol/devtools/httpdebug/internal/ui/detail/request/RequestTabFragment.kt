package com.trendyol.devtools.httpdebug.internal.ui.detail.request

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.devtools.common.viewBinding
import com.trendyol.devtools.httpdebug.R
import com.trendyol.devtools.httpdebug.databinding.DevToolsHttpDebugFragmentRequestTabBinding
import com.trendyol.devtools.httpdebug.internal.di.RequestTabContainer

internal class RequestTabFragment : Fragment(R.layout.dev_tools_http_debug_fragment_request_tab) {

    private val binding by viewBinding(DevToolsHttpDebugFragmentRequestTabBinding::bind)
    private val viewModel by viewModels<RequestTabViewModel> { RequestTabContainer().RequestTabViewModelFactory() }
    private val id by lazy { requireArguments().getString(KEY_ID)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getRequestDetails(id)
        }

        viewModel.getRequestLiveEvent().observe(viewLifecycleOwner) {
            TODO("Bind")
        }
    }

    companion object {

        private const val KEY_ID = "key_id"

        fun newInstance(id: String): RequestTabFragment =
            RequestTabFragment().apply {
                arguments = bundleOf(KEY_ID to id)
            }
    }
}
