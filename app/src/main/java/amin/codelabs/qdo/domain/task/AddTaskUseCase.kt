package amin.codelabs.qdo.domain.task

import javax.inject.Inject

/**
 * Use case for adding a new task.
 * @param repository The repository to add the task to.
 */
class AddTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Add a new task.
     * @param task The task to add.
     */
    suspend operator fun invoke(task: Task) = repository.addTask(task)
} 