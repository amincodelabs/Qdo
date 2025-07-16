package amin.codelabs.qdo.data.task.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) 