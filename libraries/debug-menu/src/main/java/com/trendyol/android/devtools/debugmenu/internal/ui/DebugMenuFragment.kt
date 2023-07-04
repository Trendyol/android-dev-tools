package com.trendyol.android.devtools.debugmenu.internal.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.R
import com.trendyol.android.devtools.debugmenu.databinding.DebugMenuFragmentBinding
import com.trendyol.android.devtools.debugmenu.internal.di.ContextContainer
import com.trendyol.android.devtools.debugmenu.internal.ext.viewBinding
import com.trendyol.android.devtools.debugmenu.internal.ui.DebugMenuActivity.Companion.KEY_TITLE

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

        binding.toolbarDebugMenu.title = requireArguments().getString(KEY_TITLE)
    }

    private fun observeViewModel() {
        viewModel.getDebugActions().observe(viewLifecycleOwner, ::renderDebugActions)
        viewModel.getUpdatedItemIndex().observe(viewLifecycleOwner, ::renderUpdate)
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewDebugMenu.adapter = adapter
        adapter.debugActionItemClickListener = viewModel::onDebugActionItemClick
        adapter.debugActionItemSwitchChangedListener = viewModel::onDebugActionItemChecked
    }

    private fun renderDebugActions(debugActions: List<DebugActionItem>) {
        adapter.submitList(debugActions)
    }

    private fun renderUpdate(index: Int) {
        binding.recyclerViewDebugMenu.post {
            adapter.notifyItemChanged(index)
        }
    }

    companion object {

        fun newInstance(title: String): DebugMenuFragment {
            return DebugMenuFragment().apply {
                arguments = bundleOf(KEY_TITLE to title)
            }
        }
    }
}
