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

internal class MainViewModel(
    private val eventManager: EventManager,
) : ViewModel() {

    val events: Flow<PagingData<Event>> = Pager(PagingConfig(pageSize = 20)) {
        EventPagingSource(
            eventManager = eventManager,
            query = "%%",
        )
    }
        .flow
        .cachedIn(viewModelScope)
}
