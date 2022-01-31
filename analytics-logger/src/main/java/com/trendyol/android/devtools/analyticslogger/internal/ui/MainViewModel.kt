package com.trendyol.android.devtools.analyticslogger.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManager
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.domain.paging.EventPagingSource
import com.trendyol.android.devtools.analyticslogger.internal.ui.detail.DetailState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class MainViewModel(
    private val eventManager: EventManager,
) : ViewModel() {

    private val queryState = MutableStateFlow<String?>("")

    private val _detailState = MutableStateFlow<DetailState>(DetailState.Initial)
    val detailState: StateFlow<DetailState> = _detailState

    val eventsFlow: Flow<PagingData<Event>> = Pager(PagingConfig(pageSize = 20)) {
        EventPagingSource(
            eventManager = eventManager,
            query = queryState.value,
        )
    }
        .flow
        .cachedIn(viewModelScope)

    fun setQuery(query: String?) {
        queryState.value = ""
    }

    fun onEventSelected(event: Event) {
        _detailState.value = DetailState.Selected(event)
    }
}
