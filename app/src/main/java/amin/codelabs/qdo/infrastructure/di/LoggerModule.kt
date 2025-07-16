package amin.codelabs.qdo.infrastructure.di

import amin.codelabs.qdo.infrastructure.logger.AndroidLogger
import amin.codelabs.qdo.infrastructure.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {
    @Provides
    @Singleton
    fun provideLogger(): Logger = AndroidLogger()
} 