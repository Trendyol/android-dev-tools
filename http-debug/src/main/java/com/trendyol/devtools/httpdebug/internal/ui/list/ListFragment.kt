package com.trendyol.devtools.httpdebug.internal.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.devtools.common.viewBinding
import com.trendyol.devtools.httpdebug.R
import com.trendyol.devtools.httpdebug.databinding.DevToolsHttpDebugFragmentListBinding
import com.trendyol.devtools.httpdebug.internal.di.MainContainer
import com.trendyol.devtools.httpdebug.internal.ui.HttpDebugActivity
import com.trendyol.devtools.httpdebug.internal.ui.detail.DetailFragment

internal class ListFragment : Fragment(R.layout.dev_tools_http_debug_fragment_list) {

    private val binding by viewBinding(DevToolsHttpDebugFragmentListBinding::bind)
    private val viewModel by viewModels<ListViewModel> {
        MainContainer.requestListContainer.ListViewModelFactory()
    }
    private val adapter by lazy {
        RequestListAdapter { requestId ->
            val detailFragment = DetailFragment.newInstance(requestId)
            (requireActivity() as HttpDebugActivity).openFragment(detailFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.devToolsHttpDebugFabSkipAllRequests.setOnClickListener { viewModel.skipAllRequests() }
        binding.devToolsHttpDebugRecyclerRequests.adapter = adapter
        val itemTouchHelper = object : ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = true

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val requestId = adapter.currentList[viewHolder.adapterPosition].id
                    viewModel.skipRequest(requestId)
                }
            }
        ) {
        }

        itemTouchHelper.attachToRecyclerView(binding.devToolsHttpDebugRecyclerRequests)

        viewModel.getRequests()
        viewModel.getRequestsLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            if (it.isEmpty()) {
                binding.devToolsHttpDebugFabSkipAllRequests.visibility = View.GONE
            } else {
                binding.devToolsHttpDebugFabSkipAllRequests.visibility = View.VISIBLE
            }
        }
    }
}
