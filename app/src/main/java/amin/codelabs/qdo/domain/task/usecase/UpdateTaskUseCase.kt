package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import amin.codelabs.qdo.domain.task.validation.TaskValidator
import javax.inject.Inject

/**
 * Use case for updating an existing task.
 * @param repository The repository to update the task in.
 */
class UpdateTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Update an existing task with domain validation.
     * @param task The task to update.
     * @throws TaskValidationException if the task data is invalid
     */
    suspend operator fun invoke(task: Task) {
        // Validate task before updating
        TaskValidator.validateForUpdate(task)
        
        // Update task in repository
        repository.updateTask(task)
    }
} 