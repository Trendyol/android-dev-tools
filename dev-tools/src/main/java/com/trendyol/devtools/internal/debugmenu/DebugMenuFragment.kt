package com.trendyol.devtools.internal.debugmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.devtools.R
import com.trendyol.devtools.common.viewBinding
import com.trendyol.devtools.databinding.DevToolsFragmentDebugMenuBinding
import com.trendyol.devtools.internal.di.ContextContainer

internal class DebugMenuFragment : Fragment(R.layout.dev_tools_fragment_debug_menu) {

    private val binding: DevToolsFragmentDebugMenuBinding by viewBinding(DevToolsFragmentDebugMenuBinding::bind)
    private val viewModel by viewModels<DebugMenuViewModel> {
        ContextContainer.debugMenuContainer.DebugMenuViewModelFactory()
    }
    private val adapter: DebugMenuAdapter = DebugMenuAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.getDebugActions().observe(viewLifecycleOwner) { renderDebugActions(it) }
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewDebugMenu.adapter = adapter
        adapter.debugActionItemClickListener = { viewModel.onDebugActionItemClick(it) }
    }

    private fun renderDebugActions(debugActions: List<DebugActionItem>) {
        adapter.submitList(debugActions)
    }

    companion object {
        fun newInstance(): DebugMenuFragment {
            return DebugMenuFragment()
        }
    }
}
