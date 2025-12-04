package com.trendyol.android.devtools.analyticslogger.internal.ui.model

import android.text.SpannableString
import androidx.core.graphics.toColorInt
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.ext.containsQuery
import com.trendyol.android.devtools.analyticslogger.internal.ext.findMatchingLinePreview
import com.trendyol.android.devtools.analyticslogger.internal.ext.highlightQuery

/**
 * ViewState for an Event item in the list
 * Contains both data and presentation logic
 */
internal data class EventItemViewState(
    val event: Event,
    val searchQuery: String
) {
    private val keyMatchesQuery: Boolean
        get() = event.key.containsQuery(searchQuery)

    private val valueMatchesQuery: Boolean
        get() = event.value.containsQuery(searchQuery) && !keyMatchesQuery

    private val bodyPreviewRawText: String?
        get() = if (valueMatchesQuery) {
            event.value.findMatchingLinePreview(searchQuery)
        } else {
            null
        }

    /**
     * Whether the event key should be highlighted
     */
    val shouldHighlightKey: Boolean
        get() = keyMatchesQuery && searchQuery.isNotEmpty()

    /**
     * Whether the body preview should be visible
     */
    val isBodyPreviewVisible: Boolean
        get() = valueMatchesQuery && bodyPreviewRawText != null

    /**
     * Gets the event key text (plain or highlighted)
     */
    fun getKeyText(): CharSequence {
        return if (shouldHighlightKey) {
            event.key.orEmpty().highlightQuery(searchQuery, highlightColor, defaultTextColor)
        } else {
            event.key.orEmpty()
        }
    }

    /**
     * Gets the body preview text with highlighting
     * Returns null if preview should not be shown
     */
    fun getBodyPreviewText(): SpannableString? {
        return if (isBodyPreviewVisible && bodyPreviewRawText != null) {
            bodyPreviewRawText?.highlightQuery(searchQuery, highlightColor, defaultTextColor)
        } else {
            null
        }
    }

    companion object {
        private val highlightColor = "#FFD54F".toColorInt()
        private val defaultTextColor = "#000000".toColorInt()

        fun createDefault(event: Event): EventItemViewState {
            return EventItemViewState(event = event, searchQuery = "")
        }
    }
}
