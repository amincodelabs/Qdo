package amin.codelabs.qdo.infrastructure.logger

/**
 * Logger interface for logging errors and messages.
 * Implementations can log to Logcat, files, crash reporting, etc.
 */
interface Logger {
    fun logError(throwable: Throwable, message: String? = null)
    fun logDebug(message: String)
} 