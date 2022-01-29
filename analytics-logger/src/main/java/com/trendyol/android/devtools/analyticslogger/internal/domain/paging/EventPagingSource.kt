package com.trendyol.android.devtools.analyticslogger.internal.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trendyol.android.devtools.analyticslogger.internal.domain.manager.EventManager
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

internal class EventPagingSource(
    private val eventManager: EventManager,
    private val query: String?,
) : PagingSource<Int, Event>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val nextPage = params.key ?: 1
        val response = eventManager.find(query = query, page = nextPage)

        return LoadResult.Page(
            data = response,
            prevKey = if (nextPage == 1) null else nextPage - 1,
            nextKey = if (response.isNotEmpty()) nextPage + 1 else null,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return null
    }
}
