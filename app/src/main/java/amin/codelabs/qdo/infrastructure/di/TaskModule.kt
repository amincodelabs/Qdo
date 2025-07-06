package amin.codelabs.qdo.infrastructure.di

import android.content.Context
import androidx.room.Room
import amin.codelabs.qdo.data.task.dao.TaskDao
import amin.codelabs.qdo.data.task.database.TaskDatabase
import amin.codelabs.qdo.data.task.repository.TaskRepositoryImpl
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import amin.codelabs.qdo.domain.task.usecase.UpdateTaskUseCase
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
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepositoryImpl(dao)

    @Provides
    fun provideUpdateTaskUseCase(repository: TaskRepository): UpdateTaskUseCase = UpdateTaskUseCase(repository)
} 