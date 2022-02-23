package com.trendyol.android.devtools.analyticslogger.internal.ui.detail

import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

internal sealed class DetailState {

    object Initial : DetailState()

    data class Selected(val event: Event) : DetailState()
}
