package amin.codelabs.qdo.infrastructure.datetime

import org.junit.Assert.assertEquals
import org.junit.Test

class DateTimeFormatterTest {
    private val now = System.currentTimeMillis()

    @Test
    fun `formatRelativeTime returns just now for less than 1 minute`() {
        val ts = now - 30 * 1000 // 30 seconds ago
        assertEquals("just now", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns 1 minute ago for exactly 1 minute`() {
        val ts = now - 60 * 1000
        assertEquals("1 minute ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns N minutes ago for under 1 hour`() {
        val ts = now - 25 * 60 * 1000 // 25 minutes ago
        assertEquals("25 minutes ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns 1 hour ago for exactly 1 hour`() {
        val ts = now - 60 * 60 * 1000
        assertEquals("1 hour ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns N hours ago for under 1 day`() {
        val ts = now - 5 * 60 * 60 * 1000 // 5 hours ago
        assertEquals("5 hours ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns 1 day ago for exactly 1 day`() {
        val ts = now - 24 * 60 * 60 * 1000
        assertEquals("1 day ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns N days ago for under 1 week`() {
        val ts = now - 3 * 24 * 60 * 60 * 1000 // 3 days ago
        assertEquals("3 days ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns 1 week ago for exactly 1 week`() {
        val ts = now - 7 * 24 * 60 * 60 * 1000
        assertEquals("1 week ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns N weeks ago for under 1 month`() {
        val ts = now - 3 * 7 * 24 * 60 * 60 * 1000 // 3 weeks ago
        assertEquals("3 weeks ago", DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns formatted date for 1 month ago`() {
        val ts = now - 30L * 24 * 60 * 60 * 1000 // 30 days ago
        val expected = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            .format(java.util.Date(ts))
        assertEquals(expected, DateTimeFormatter.formatRelativeTime(ts))
    }

    @Test
    fun `formatRelativeTime returns formatted date for much older timestamps`() {
        val ts = now - 365L * 24 * 60 * 60 * 1000 // 1 year ago
        val expected = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            .format(java.util.Date(ts))
        assertEquals(expected, DateTimeFormatter.formatRelativeTime(ts))
    }
} 