package amin.codelabs.qdo.domain.task

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