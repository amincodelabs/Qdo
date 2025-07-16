package amin.codelabs.qdo.infrastructure.logger

import android.util.Log

class AndroidLogger : Logger {
    companion object {
        private const val LOGGER_TAG = "QdoLogger"
    }

    override fun logError(throwable: Throwable, message: String?) {
        Log.e(LOGGER_TAG, message ?: throwable.message ?: "Unknown error", throwable)
    }

    override fun logDebug(message: String) {
        Log.d(LOGGER_TAG, message)
    }
}