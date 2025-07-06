package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.repository.TaskRepository
import javax.inject.Inject

/**
 * Use case for deleting a task by its ID.
 * @param repository The repository to delete the task from.
 */
class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Delete a task by its ID.
     * @param taskId The ID of the task to delete.
     */
    suspend operator fun invoke(taskId: Long) = repository.deleteTask(taskId)
} 