package com.trendyol.android.devtools.analyticslogger.internal.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.ui.EventAdapter
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventsFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }

    private val eventAdapter: EventAdapter by lazy { EventAdapter() }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        observeData()
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventsFlow.collectLatest {
                eventAdapter.submitData(it)
            }
        }
    }
    
    private fun setQuery(query: String?) {
        viewModel.setQuery(query)
        eventAdapter.refresh()
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
