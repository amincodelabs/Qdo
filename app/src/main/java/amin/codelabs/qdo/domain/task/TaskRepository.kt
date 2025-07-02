package amin.codelabs.qdo.domain.task

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
} 