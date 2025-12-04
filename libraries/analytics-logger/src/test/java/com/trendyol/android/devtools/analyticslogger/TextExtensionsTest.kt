package com.trendyol.android.devtools.analyticslogger

import android.graphics.Typeface
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.graphics.toColorInt
import com.trendyol.android.devtools.analyticslogger.internal.ext.containsQuery
import com.trendyol.android.devtools.analyticslogger.internal.ext.findMatchingLinePreview
import com.trendyol.android.devtools.analyticslogger.internal.ext.highlightQuery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Unit tests for TextExtensions utility functions
 * Uses Robolectric to mock Android framework dependencies
 */
@RunWith(RobolectricTestRunner::class)
class TextExtensionsTest {

    private val highlightColor = "#FFD54F".toColorInt()
    private val textColor = "#000000".toColorInt()

    // ========== highlightQuery Tests ==========

    @Test
    fun `highlightQuery should highlight single occurrence`() {
        // Given
        val text = "OnCartPageSeen"
        val query = "Cart"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        assertNotNull(result)
        assertTrue(result.toString().contains("Cart"))
        
        // Verify spans are applied
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertTrue(spans.isNotEmpty())
    }

    @Test
    fun `highlightQuery should highlight multiple occurrences`() {
        // Given
        val text = "Cart items in cart"
        val query = "cart"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        val backgroundSpans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(2, backgroundSpans.size)
    }

    @Test
    fun `highlightQuery should be case insensitive`() {
        // Given
        val text = "OnMainFragmentSeen"
        val query = "mainfrag"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertTrue(spans.isNotEmpty())
    }

    @Test
    fun `highlightQuery should return plain text when query is null`() {
        // Given
        val text = "SomeEvent"

        // When
        val result = text.highlightQuery(null, highlightColor, textColor)

        // Then
        assertEquals(text, result.toString())
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(0, spans.size)
    }

    @Test
    fun `highlightQuery should return plain text when query is empty`() {
        // Given
        val text = "SomeEvent"

        // When
        val result = text.highlightQuery("", highlightColor, textColor)

        // Then
        assertEquals(text, result.toString())
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(0, spans.size)
    }

    @Test
    fun `highlightQuery should return plain text when query is less than 2 characters`() {
        // Given
        val text = "SomeEvent"

        // When
        val result = text.highlightQuery("a", highlightColor, textColor)

        // Then
        assertEquals(text, result.toString())
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(0, spans.size)
    }

    @Test
    fun `highlightQuery should apply all three span types`() {
        // Given
        val text = "OnCartPageSeen"
        val query = "Cart"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        val backgroundSpans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        val foregroundSpans = result.getSpans(0, result.length, ForegroundColorSpan::class.java)
        val styleSpans = result.getSpans(0, result.length, StyleSpan::class.java)

        assertEquals(1, backgroundSpans.size)
        assertEquals(1, foregroundSpans.size)
        assertEquals(1, styleSpans.size)
        assertEquals(Typeface.BOLD, styleSpans[0].style)
    }

    @Test
    fun `highlightQuery should handle query at start of text`() {
        // Given
        val text = "CartPage"
        val query = "Cart"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(1, spans.size)
        assertEquals(0, result.getSpanStart(spans[0]))
    }

    @Test
    fun `highlightQuery should handle query at end of text`() {
        // Given
        val text = "OnPageCart"
        val query = "Cart"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(1, spans.size)
        assertEquals(text.length, result.getSpanEnd(spans[0]))
    }

    @Test
    fun `highlightQuery should handle very long text safely`() {
        // Given
        val text = "a".repeat(10000) + "Cart" + "b".repeat(10000)
        val query = "Cart"

        // When
        val result = text.highlightQuery(query, highlightColor, textColor)

        // Then
        val spans = result.getSpans(0, result.length, BackgroundColorSpan::class.java)
        assertEquals(1, spans.size)
    }

    // ========== findMatchingLinePreview Tests ==========

    @Test
    fun `findMatchingLinePreview should find matching line in JSON`() {
        // Given
        val json = """{"category": "Cart", "page": "home"}"""
        val query = "Cart"

        // When
        val result = json.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertTrue(result!!.contains("Cart", ignoreCase = true))
    }

    @Test
    fun `findMatchingLinePreview should return null when no match`() {
        // Given
        val json = """{"category": "Home", "page": "main"}"""
        val query = "Cart"

        // When
        val result = json.findMatchingLinePreview(query)

        // Then
        assertNull(result)
    }

    @Test
    fun `findMatchingLinePreview should return null when text is null`() {
        // Given
        val text: String? = null
        val query = "Cart"

        // When
        val result = text.findMatchingLinePreview(query)

        // Then
        assertNull(result)
    }

    @Test
    fun `findMatchingLinePreview should return null when query is null`() {
        // Given
        val text = """{"category": "Cart"}"""
        val query: String? = null

        // When
        val result = text.findMatchingLinePreview(query)

        // Then
        assertNull(result)
    }

    @Test
    fun `findMatchingLinePreview should return null when query is less than 2 characters`() {
        // Given
        val text = """{"category": "Cart"}"""
        val query = "C"

        // When
        val result = text.findMatchingLinePreview(query)

        // Then
        assertNull(result)
    }

    @Test
    fun `findMatchingLinePreview should truncate long lines with ellipsis`() {
        // Given
        val longValue = "x".repeat(150)
        val text = """{"category": "$longValue"}"""
        val query = "category"

        // When
        val result = text.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertTrue(result!!.length <= 110) // 100 + "..." prefix/suffix
        assertTrue(result.contains("..."))
    }

    @Test
    fun `findMatchingLinePreview should center query in preview for long lines`() {
        // Given
        val prefix = "x".repeat(50)
        val suffix = "y".repeat(50)
        val text = """{"data": "$prefix Cart $suffix"}"""
        val query = "Cart"

        // When
        val result = text.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertTrue(result!!.contains("Cart"))
        // Should have context on both sides
        assertTrue(result.contains("..."))
    }

    @Test
    fun `findMatchingLinePreview should be case insensitive`() {
        // Given
        val json = """{"category": "CART"}"""
        val query = "cart"

        // When
        val result = json.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertTrue(result!!.contains("CART"))
    }

    @Test
    fun `findMatchingLinePreview should handle multiline JSON`() {
        // Given
        val json = """
            {
                "category": "Home",
                "subcategory": "Cart"
            }
        """.trimIndent()
        val query = "Cart"

        // When
        val result = json.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertTrue(result!!.contains("Cart", ignoreCase = true))
    }

    @Test
    fun `findMatchingLinePreview should return first match when multiple lines match`() {
        // Given
        val json = """{"first": "Cart", "second": "Cart"}"""
        val query = "Cart"

        // When
        val result = json.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertTrue(result!!.contains("Cart"))
        // Should contain "first" since it's the first match
        assertTrue(result.contains("first", ignoreCase = true))
    }

    @Test
    fun `findMatchingLinePreview should handle short lines without ellipsis`() {
        // Given
        val json = """{"category": "Cart"}"""
        val query = "Cart"

        // When
        val result = json.findMatchingLinePreview(query)

        // Then
        assertNotNull(result)
        assertFalse(result!!.contains("..."))
    }

    // ========== containsQuery Tests ==========

    @Test
    fun `containsQuery should return true when text contains query`() {
        // Given
        val text = "OnCartPageSeen"
        val query = "Cart"

        // When
        val result = text.containsQuery(query)

        // Then
        assertTrue(result)
    }

    @Test
    fun `containsQuery should return false when text doesnt contain query`() {
        // Given
        val text = "OnHomePageSeen"
        val query = "Cart"

        // When
        val result = text.containsQuery(query)

        // Then
        assertFalse(result)
    }

    @Test
    fun `containsQuery should be case insensitive`() {
        // Given
        val text = "OnCartPageSeen"
        val query = "cart"

        // When
        val result = text.containsQuery(query)

        // Then
        assertTrue(result)
    }

    @Test
    fun `containsQuery should return false when text is null`() {
        // Given
        val text: String? = null
        val query = "Cart"

        // When
        val result = text.containsQuery(query)

        // Then
        assertFalse(result)
    }

    @Test
    fun `containsQuery should return false when query is null`() {
        // Given
        val text = "OnCartPageSeen"
        val query: String? = null

        // When
        val result = text.containsQuery(query)

        // Then
        assertFalse(result)
    }

    @Test
    fun `containsQuery should return false when text is empty`() {
        // Given
        val text = ""
        val query = "Cart"

        // When
        val result = text.containsQuery(query)

        // Then
        assertFalse(result)
    }

    @Test
    fun `containsQuery should return false when query is empty`() {
        // Given
        val text = "OnCartPageSeen"
        val query = ""

        // When
        val result = text.containsQuery(query)

        // Then
        assertFalse(result)
    }

    @Test
    fun `containsQuery should handle single character query`() {
        // Given
        val text = "Cart"
        val query = "C"

        // When
        val result = text.containsQuery(query)

        // Then
        assertTrue(result)
    }

    @Test
    fun `containsQuery should handle unicode characters`() {
        // Given
        val text = "Sepet sayfası"
        val query = "Sepet"

        // When
        val result = text.containsQuery(query)

        // Then
        assertTrue(result)
    }
}

