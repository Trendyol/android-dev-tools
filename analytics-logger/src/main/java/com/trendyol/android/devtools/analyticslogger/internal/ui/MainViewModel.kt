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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class MainViewModel(
    private val eventManager: EventManager,
) : ViewModel() {

    private val queryState = MutableStateFlow<String?>("")

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
}
