package com.trendyol.android.devtools.analyticslogger.internal.ui.events

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.ui.EventAdapter
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainActivity
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import com.trendyol.android.devtools.analyticslogger.internal.ui.detail.DetailFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class EventsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        ContextContainer.mainContainer.MainViewModelFactory()
    }

    private val eventAdapter: EventAdapter by lazy { EventAdapter() }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initView(view)
        observeData()
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter

        eventAdapter.onItemSelected = { event ->
            navigateToEventDetail(event)
        }
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

    private fun deleteAll() {
        viewModel.deleteAll()
        eventAdapter.refresh()
    }

    private fun navigateToEventDetail(event: Event) {
        viewModel.onEventSelected(event)
        (activity as MainActivity).navigate(DetailFragment.newInstance(), DetailFragment.NAME)
    }

    private fun initSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(requireActivity().componentName)
        )

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setQuery(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_events, menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
