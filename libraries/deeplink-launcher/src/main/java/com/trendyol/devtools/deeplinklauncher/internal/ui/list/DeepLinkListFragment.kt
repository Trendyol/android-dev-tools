package com.trendyol.devtools.deeplinklauncher.internal.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.trendyol.devtools.deeplinklauncher.R
import com.trendyol.devtools.deeplinklauncher.databinding.DeeplinkLauncherFragmentDeeplinkListBinding
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ext.viewBinding

class DeepLinkListFragment : Fragment(R.layout.deeplink_launcher_fragment_deeplink_list) {

    private val binding: DeeplinkLauncherFragmentDeeplinkListBinding by viewBinding(
        DeeplinkLauncherFragmentDeeplinkListBinding::bind
    )
    private val viewModel: DeepLinkListViewModel by viewModels { ContextContainer.deepLinkListContainer.DeepLinkListViewModelFactory() }
    private val deepLinkSharedViewModel: DeepLinkListSharedViewModel by activityViewModels()

    private val adapter: DeepLinkListAdapter = DeepLinkListAdapter().apply {
        onHistoryItemClicked = {
            deepLinkSharedViewModel.selectedDeepLink.value = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        setRecyclerView()
        setTabIndexAndGetDeepLinks()
    }

    private fun setTabIndexAndGetDeepLinks() {
        viewModel.setTabIndex(arguments?.getInt(TAB_INDEX_KEY))
        viewModel.getDeepLinks()
    }

    private fun observe() {
        viewModel.getDeepLinkList().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setRecyclerView() {
        val recyclerHistory = binding.recyclerDeepLinkList
        val dividerItemDecoration = DividerItemDecoration(
            recyclerHistory.context,
            DividerItemDecoration.VERTICAL
        ).apply {
            ContextCompat.getDrawable(recyclerHistory.context, R.drawable.drawable_item_space_decorator)?.let {
                setDrawable(it)
            }
        }
        recyclerHistory.addItemDecoration(dividerItemDecoration)
        recyclerHistory.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(TAB_INDEX: Int) =
            DeepLinkListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TAB_INDEX_KEY, TAB_INDEX)
                }
            }

        private const val TAB_INDEX_KEY: String = "DEEPLINK_LIST_TAB_INDEX"
    }
}
