package com.trendyol.android.devtools.analyticslogger.internal.ext

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

/**
 * Highlights all occurrences of the query string in the text with background and foreground colors
 * Safe implementation that handles edge cases and prevents crashes
 */
internal fun String.highlightQuery(query: String?, highlightBackgroundColor: Int, defaultTextColor: Int): SpannableString {
    val spannable = SpannableString(this)

    if (query.isNullOrEmpty() || query.length < 2) {
        return spannable
    }

    try {
        var startIndex = 0
        while (startIndex < this.length) {
            val index = this.indexOf(query, startIndex, ignoreCase = true)
            if (index == -1) break

            // Safety check: ensure end index doesn't exceed text length
            val endIndex = (index + query.length).coerceAtMost(this.length)

            // Only apply span if we have a valid range
            if (index >= 0 && endIndex <= this.length && index < endIndex) {
                spannable.setSpan(
                    BackgroundColorSpan(highlightBackgroundColor),
                    index,
                    endIndex,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(defaultTextColor),
                    index,
                    endIndex,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    index,
                    endIndex,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            startIndex = index + query.length
        }
    } catch (e: Exception) {
        // If anything goes wrong, just return the non-highlighted text
        android.util.Log.w("TextExtensions", "Error highlighting query: ${e.message}")
        return spannable
    }

    return spannable
}

/**
 * Finds the first line in the text that contains the query and returns a preview with context
 */
internal fun String?.findMatchingLinePreview(query: String?): String? {
    if (this.isNullOrEmpty() || query.isNullOrEmpty() || query.length < 2) {
        return null
    }

    // Split by common JSON separators to get individual lines/fields
    val lines = this.split("\n", ",", "{", "}", "[", "]")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    // Find the first line that contains the query
    val matchingLine = lines.firstOrNull {
        it.contains(query, ignoreCase = true)
    } ?: return null

    // Create a preview with ellipsis if needed
    val maxPreviewLength = 100
    return if (matchingLine.length > maxPreviewLength) {
        val queryIndex = matchingLine.indexOf(query, ignoreCase = true)

        // Try to center the query in the preview
        val start = (queryIndex - 30).coerceAtLeast(0)
        val end = (start + maxPreviewLength).coerceAtMost(matchingLine.length)

        val preview = matchingLine.substring(start, end)
        val prefix = if (start > 0) "..." else ""
        val suffix = if (end < matchingLine.length) "..." else ""

        "$prefix$preview$suffix"
    } else {
        matchingLine
    }
}

/**
 * Checks if the text contains the query string (case insensitive)
 */
internal fun String?.containsQuery(query: String?): Boolean {
    if (this.isNullOrEmpty() || query.isNullOrEmpty()) {
        return false
    }
    return this.contains(query, ignoreCase = true)
}

