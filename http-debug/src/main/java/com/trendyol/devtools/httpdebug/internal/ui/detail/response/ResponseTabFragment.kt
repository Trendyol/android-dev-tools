package com.trendyol.devtools.httpdebug.internal.ui.detail.response

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.devtools.common.viewBinding
import com.trendyol.devtools.httpdebug.R
import com.trendyol.devtools.httpdebug.databinding.DevToolsHttpDebugFragmentResponseTabBinding
import com.trendyol.devtools.httpdebug.internal.di.ResponseTabContainer

internal class ResponseTabFragment : Fragment(R.layout.dev_tools_http_debug_fragment_response_tab) {

    private val binding by viewBinding(DevToolsHttpDebugFragmentResponseTabBinding::bind)
    private val viewModel by viewModels<ResponseTabViewModel> { ResponseTabContainer().ResponseTabViewModelFactory() }
    private val id by lazy { requireArguments().getString(KEY_ID)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getResponse(id)
        }

        viewModel.getResponseLiveEvent().observe(viewLifecycleOwner) {
            TODO("Bind")
        }
    }

    companion object {

        private const val KEY_ID = "key_id"

        fun newInstance(id: String): ResponseTabFragment =
            ResponseTabFragment().apply {
                arguments = bundleOf(KEY_ID to id)
            }
    }
}
