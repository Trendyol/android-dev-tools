package com.trendyol.android.devtools.analyticslogger.internal.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.databinding.AnalyticsLoggerFragmentEventsBinding
import com.trendyol.android.devtools.analyticslogger.internal.NotificationManager
import com.trendyol.android.devtools.analyticslogger.internal.di.AnalyticsLoggerKoinComponent
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.ui.EventAdapter
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainActivity
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import com.trendyol.android.devtools.analyticslogger.internal.ui.detail.DetailFragment
import com.trendyol.android.devtools.analyticslogger.internal.ext.setupHideKeyboardOnScroll
import com.trendyol.android.devtools.analyticslogger.internal.ext.setupHideKeyboardOnTouch
import embedded.koin.android.ext.android.inject
import embedded.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class EventsFragment : Fragment(), AnalyticsLoggerKoinComponent {

    private val viewModel: MainViewModel by activityViewModel()
    private val notificationManager: NotificationManager by inject()

    private var _binding: AnalyticsLoggerFragmentEventsBinding? = null

    private val binding get() = _binding!!

    private var eventAdapter: EventAdapter? = null
    private var searchView: SearchView? = null

    private lateinit var eventPlatformAdapter: EventPlatformAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AnalyticsLoggerFragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initView()
        observeData()
    }

    override fun onPause() {
        super.onPause()
        // CRITICAL: Remove listeners to prevent SearchView from clearing query during collapse
        searchView?.setOnQueryTextListener(null)
        searchView?.setOnCloseListener(null)
    }

    override fun onDestroyView() {
        _binding = null
        eventAdapter = null
        searchView = null
        super.onDestroyView()
    }

    private fun initView() {
        binding.root.setupHideKeyboardOnTouch()
        binding.recyclerView.setupHideKeyboardOnScroll()

        eventPlatformAdapter = EventPlatformAdapter()
        binding.platformsRecyclerView.adapter = eventPlatformAdapter

        eventAdapter = EventAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventAdapter

        eventAdapter?.onItemSelected = { event ->
            navigateToEventDetail(event)
        }

        eventPlatformAdapter.onItemSelected = {
            viewModel.setFilterState(it)
            eventAdapter?.refresh()
        }
    }

    private fun observeData() {
        with(viewLifecycleOwner.lifecycleScope) {
            launch {
                viewModel.eventsFlow.collectLatest {
                    eventAdapter?.submitData(it)
                }
            }

            launch {
                viewModel.platformsState.collectLatest {
                    eventPlatformAdapter.submitData(it)
                }
            }

            // Observe query changes from ViewModel (single source of truth)
            launch {
                viewModel.queryState.collectLatest { query ->
                    // Update adapter's search query for highlighting
                    eventAdapter?.searchQuery = query

                    // Update SearchView if different
                    if (searchView?.query?.toString() != query) {
                        searchView?.setQuery(query, false)
                    }

                    // Trigger rebind for highlighting
                    if (query.isNotEmpty()) {
                        val itemCount = eventAdapter?.snapshot()?.items?.size ?: 0
                        if (itemCount > 0) {
                            eventAdapter?.notifyItemRangeChanged(0, itemCount, "HIGHLIGHT_UPDATE")
                        }
                    }
                }
            }
        }
    }

    private fun setQuery(query: String?) {
        viewModel.setQuery(query.orEmpty())
        eventAdapter?.refresh()
    }

    private fun deleteAll() {
        viewModel.deleteAll()
        eventAdapter?.refresh()
        notificationManager.cancelNotification()
    }

    private fun navigateToEventDetail(event: Event) {
        viewModel.onEventSelected(event)
        (activity as MainActivity).navigate(DetailFragment.newInstance(), DetailFragment.NAME)
    }

    private fun initSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        // Attach listeners
        initSearchViewListeners(searchItem)

        // Restore query from ViewModel
        val currentQuery = viewModel.getQuery()
        if (currentQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView?.setQuery(currentQuery, false)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.analytics_logger_menu_events, menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        // Get SearchView reference and re-attach listeners
        val searchItem = menu.findItem(R.id.action_search)
        if (searchView == null && searchItem != null) {
            searchView = searchItem.actionView as? SearchView
            initSearchViewListeners(searchItem)
        }

        // Restore query from ViewModel
        val currentQuery = viewModel.getQuery()
        if (currentQuery.isNotEmpty() && searchView?.query?.toString() != currentQuery) {
            searchItem?.expandActionView()
            searchView?.setQuery(currentQuery, false)
        }
    }

    private fun initSearchViewListeners(searchItem: MenuItem) {
        // Prevent query loss when SearchView collapses
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                searchView?.query?.toString()?.let { viewModel.setQuery(it) }
                return true
            }
        })

        // Prevent close button from clearing query
        searchView?.setOnCloseListener {
            searchView?.query?.toString()?.let { viewModel.setQuery(it) }
            false
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                // Only accept query changes when SearchView is actively expanded
                if (searchItem.isActionViewExpanded) {
                    setQuery(newText)
                }
                return true
            }
        })
    }

    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}
