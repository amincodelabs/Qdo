package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import javax.inject.Inject

/**
 * Use case for updating an existing task.
 * @param repository The repository to update the task in.
 */
class UpdateTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Update an existing task.
     * @param task The task to update.
     */
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
} 