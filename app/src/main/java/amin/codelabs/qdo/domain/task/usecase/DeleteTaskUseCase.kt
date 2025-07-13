package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import amin.codelabs.qdo.domain.task.validation.TaskValidator
import javax.inject.Inject

/**
 * Use case for deleting a task by its ID.
 * @param repository The repository to delete the task from.
 */
class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Delete a task by its ID with domain validation.
     * @param taskId The ID of the task to delete.
     * @throws TaskValidationException if the task ID is invalid
     */
    suspend operator fun invoke(taskId: Long) {
        // Validate task ID before deletion
        TaskValidator.validateForDeletion(taskId)
        
        // Delete task from repository
        repository.deleteTask(taskId)
    }
} 