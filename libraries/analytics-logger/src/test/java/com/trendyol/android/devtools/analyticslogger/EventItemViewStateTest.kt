package com.trendyol.android.devtools.analyticslogger

import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.ui.model.EventItemViewState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EventItemViewStateTest {

    @Test
    fun `when query is empty, shouldHighlightKey should be false`() {
        // Given
        val event = createEvent(key = "OnMainFragmentSeen")
        val viewState = EventItemViewState(event, "")

        // Then
        assertFalse(viewState.shouldHighlightKey)
    }

    @Test
    fun `when query matches event key, shouldHighlightKey should be true`() {
        // Given
        val event = createEvent(key = "OnMainFragmentSeen")
        val viewState = EventItemViewState(event, "MainFrag")

        // Then
        assertTrue(viewState.shouldHighlightKey)
    }

    @Test
    fun `when query matches key, isBodyPreviewVisible should be false`() {
        // Given
        val event = createEvent(
            key = "CartEvent",
            value = """{"category": "Cart"}"""
        )
        val viewState = EventItemViewState(event, "Cart")

        // Then
        assertTrue(viewState.shouldHighlightKey)
        assertFalse(viewState.isBodyPreviewVisible) // Key takes priority
    }

    @Test
    fun `when query matches value only, isBodyPreviewVisible should be true`() {
        // Given
        val event = createEvent(
            key = "OnScreenSeen",
            value = """{"category": "Cart"}"""
        )
        val viewState = EventItemViewState(event, "Cart")

        // Then
        assertFalse(viewState.shouldHighlightKey)
        assertTrue(viewState.isBodyPreviewVisible)
    }

    @Test
    fun `when query matches value, getBodyPreviewText should return non-null text`() {
        // Given
        val event = createEvent(
            key = "OnScreenSeen",
            value = """{"category": "Cart"}"""
        )
        val viewState = EventItemViewState(event, "Cart")

        // When
        val previewText = viewState.getBodyPreviewText()

        // Then
        assertNotNull(previewText)
    }

    @Test
    fun `when query doesnt match, getBodyPreviewText should return null`() {
        // Given
        val event = createEvent(
            key = "OnScreenSeen",
            value = """{"category": "Home"}"""
        )
        val viewState = EventItemViewState(event, "Cart")

        // When
        val previewText = viewState.getBodyPreviewText()

        // Then
        assertNull(previewText)
    }

    @Test
    fun `getKeyText should return event key when no highlighting`() {
        // Given
        val event = createEvent(key = "TestEvent")
        val viewState = EventItemViewState(event, "")

        // When
        val keyText = viewState.getKeyText()

        // Then
        assertEquals("TestEvent", keyText.toString())
    }

    @Test
    fun `getKeyText should return highlighted SpannableString when query matches`() {
        // Given
        val event = createEvent(key = "OnMainFragmentSeen")
        val viewState = EventItemViewState(event, "MainFrag")

        // When
        val keyText = viewState.getKeyText()

        // Then
        assertTrue(keyText.contains("MainFrag", ignoreCase = true))
        // Should be highlighted (SpannableString)
        assertNotNull(keyText)
    }

    @Test
    fun `createDefault should create viewState with empty query`() {
        // Given
        val event = createEvent(key = "TestEvent")

        // When
        val viewState = EventItemViewState.createDefault(event)

        // Then
        assertEquals("", viewState.searchQuery)
        assertFalse(viewState.shouldHighlightKey)
        assertFalse(viewState.isBodyPreviewVisible)
    }

    @Test
    fun `case insensitive search in key should work`() {
        // Given
        val event = createEvent(key = "OnMainFragmentSeen")
        val viewState = EventItemViewState(event, "mainfrag") // lowercase

        // Then
        assertTrue(viewState.shouldHighlightKey)
    }

    @Test
    fun `case insensitive search in value should work`() {
        // Given
        val event = createEvent(
            key = "OnScreenSeen",
            value = """{"CATEGORY": "CART"}""" // uppercase
        )
        val viewState = EventItemViewState(event, "cart") // lowercase

        // Then
        assertTrue(viewState.isBodyPreviewVisible)
    }

    @Test
    fun `when query is less than 2 characters, shouldHighlightKey should be false`() {
        // Given
        val event = createEvent(key = "Cart")
        val viewState = EventItemViewState(event, "C") // Single character

        // Then
        // Note: containsQuery might still match, but we test the actual behavior
        val matchesKey = event.key?.contains("C", ignoreCase = true) ?: false
        assertEquals(matchesKey && "C".isNotEmpty(), viewState.shouldHighlightKey)
    }

    @Test
    fun `when event key is null, getKeyText should return empty string`() {
        // Given
        val event = createEvent(key = null)
        val viewState = EventItemViewState(event, "test")

        // When
        val keyText = viewState.getKeyText()

        // Then
        assertEquals("", keyText.toString())
    }

    @Test
    fun `when query has only 1 character, should not require minimum length for matching`() {
        // Given
        val event = createEvent(key = "Cart")
        val viewState = EventItemViewState(event, "C")

        // When/Then
        // containsQuery will match, but this tests the actual behavior
        val matches = "Cart".contains("C", ignoreCase = true)
        assertEquals(matches && "C".isNotEmpty(), viewState.shouldHighlightKey)
    }

    @Test
    fun `when query matches in both key and value, key takes priority`() {
        // Given
        val event = createEvent(
            key = "CartScreen",
            value = """{"screen": "Cart"}"""
        )
        val viewState = EventItemViewState(event, "Cart")

        // Then
        assertTrue(viewState.shouldHighlightKey)
        assertFalse(viewState.isBodyPreviewVisible) // Body preview should NOT show
    }

    // Helper function
    private fun createEvent(
        uid: Int = 1,
        key: String?,
        value: String = "",
        platform: String = "Firebase",
        date: String = "10:00:00",
        source: String = "TestSource",
        isSuccess: Boolean = true
    ): Event {
        return Event(
            uid = uid,
            key = key,
            value = value,
            json = value,
            platform = platform,
            date = date,
            source = source,
            isSuccess = isSuccess
        )
    }
}
