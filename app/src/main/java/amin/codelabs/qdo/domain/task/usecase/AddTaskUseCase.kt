package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import amin.codelabs.qdo.domain.task.validation.TaskValidator
import javax.inject.Inject

/**
 * Use case for adding a new task.
 * @param repository The repository to add the task to.
 */
class AddTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Add a new task with domain validation.
     * @param task The task to add.
     * @throws TaskValidationException if the task data is invalid
     */
    suspend operator fun invoke(task: Task) {
        // Validate task before adding
        TaskValidator.validateForCreation(task)
        
        // Add task to repository
        repository.addTask(task)
    }
} 