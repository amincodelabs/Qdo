package amin.codelabs.qdo.infrastructure.datetime

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Utility class for formatting dates and times in a user-friendly way.
 */
object DateTimeFormatter {
    
    /**
     * Formats a timestamp into a relative time string (e.g., "2 minutes ago", "1 hour ago").
     * For timestamps older than 1 month, returns a formatted date.
     *
     * @param timestamp The timestamp in milliseconds since epoch.
     * @return A human-readable relative time string.
     */
    fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val weeks = days / 7
        val months = days / 30
        
        return when {
            minutes < 1 -> "just now"
            minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
            hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
            days < 7 -> "$days day${if (days == 1L) "" else "s"} ago"
            weeks < 4 -> "$weeks week${if (weeks == 1L) "" else "s"} ago"
            else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
        }
    }
} 