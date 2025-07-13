package amin.codelabs.qdo.infrastructure.di

import amin.codelabs.qdo.data.task.dao.TaskDao
import amin.codelabs.qdo.data.task.database.TaskDatabase
import amin.codelabs.qdo.data.task.repository.TaskRepositoryImpl
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import amin.codelabs.qdo.domain.task.validation.DefaultTaskIdValidationRule
import amin.codelabs.qdo.domain.task.validation.DescriptionValidationRule
import amin.codelabs.qdo.domain.task.validation.TaskIdValidationRule
import amin.codelabs.qdo.domain.task.validation.TaskValidationRule
import amin.codelabs.qdo.domain.task.validation.TitleValidationRule
import amin.codelabs.qdo.infrastructure.logger.Logger
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideTaskRepository(dao: TaskDao, logger: Logger): TaskRepository =
        TaskRepositoryImpl(dao, logger)

    @Provides
    @Singleton
    fun provideTaskIdValidationRule(): TaskIdValidationRule = DefaultTaskIdValidationRule()

    @Provides
    @Singleton
    fun provideTitleValidationRule(): TaskValidationRule = TitleValidationRule()

    @Provides
    @Singleton
    fun provideDescriptionValidationRule(): TaskValidationRule = DescriptionValidationRule()
} 