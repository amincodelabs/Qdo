package amin.codelabs.qdo.data.task.database

import amin.codelabs.qdo.data.task.dao.TaskDao
import amin.codelabs.qdo.data.task.entity.TaskEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
} 