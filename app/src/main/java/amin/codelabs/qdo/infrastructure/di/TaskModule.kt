package amin.codelabs.qdo.infrastructure.di

import android.content.Context
import androidx.room.Room
import amin.codelabs.qdo.data.task.TaskDao
import amin.codelabs.qdo.data.task.TaskDatabase
import amin.codelabs.qdo.data.task.TaskRepositoryImpl
import amin.codelabs.qdo.domain.task.TaskRepository
import amin.codelabs.qdo.infrastructure.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "tasks.db").build()

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao, logger: Logger): TaskRepository = TaskRepositoryImpl(dao, logger)
} 