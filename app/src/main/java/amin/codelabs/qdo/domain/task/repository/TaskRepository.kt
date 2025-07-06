package amin.codelabs.qdo.domain.task.repository

import amin.codelabs.qdo.domain.task.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing tasks.
 * Provides methods to add, retrieve, and delete tasks.
 */
interface TaskRepository {
    /**
     * Add a new task to the data source.
     * @param task The task to add.
     */
    suspend fun addTask(task: Task)

    /**
     * Get a stream of all tasks.
     * @return A Flow emitting the current list of tasks.
     */
    fun getTasks(): Flow<List<Task>>

    /**
     * Delete a task by its ID.
     * @param taskId The ID of the task to delete.
     */
    suspend fun deleteTask(taskId: Long)

    /**
     * Update an existing task.
     * @param task The task to update.
     */
    suspend fun updateTask(task: Task)

    /**
     * Get a single task by its ID.
     * @param taskId The ID of the task to retrieve.
     * @return A Flow emitting the task, or null if not found.
     */
    fun getTaskById(taskId: Long): Flow<Task?>
} 