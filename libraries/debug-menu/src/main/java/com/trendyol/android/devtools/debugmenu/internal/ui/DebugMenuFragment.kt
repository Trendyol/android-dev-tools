package com.trendyol.android.devtools.debugmenu.internal.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.R
import com.trendyol.android.devtools.debugmenu.databinding.DebugMenuFragmentBinding
import com.trendyol.android.devtools.debugmenu.internal.di.ContextContainer
import com.trendyol.android.devtools.debugmenu.internal.ext.viewBinding

internal class DebugMenuFragment : Fragment(R.layout.debug_menu_fragment) {

    private val binding: DebugMenuFragmentBinding by viewBinding(DebugMenuFragmentBinding::bind)
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
        adapter.debugActionItemClickListener = viewModel::onDebugActionItemClick
        adapter.debugActionItemSwitchChangedListener = viewModel::onDebugActionItemChecked
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
