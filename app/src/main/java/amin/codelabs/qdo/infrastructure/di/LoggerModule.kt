package amin.codelabs.qdo.infrastructure.di

import amin.codelabs.qdo.infrastructure.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.util.Log

private const val LOGGER_TAG = "QdoLogger"

class AndroidLogger : Logger {
    override fun logError(throwable: Throwable, message: String?) {
        Log.e(LOGGER_TAG, message ?: throwable.message ?: "Unknown error", throwable)
    }
    override fun logDebug(message: String) {
        Log.d(LOGGER_TAG, message)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {
    @Provides
    @Singleton
    fun provideLogger(): Logger = AndroidLogger()
} 