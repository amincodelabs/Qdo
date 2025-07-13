package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all tasks as a Flow.
 * @param repository The repository to retrieve tasks from.
 */
class GetTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Get a stream of all tasks.
     * Note: This operation doesn't require domain validation as it retrieves all tasks.
     * Any validation would be handled at the repository level for database access.
     * 
     * @return A Flow emitting the current list of tasks.
     * @throws TaskRepositoryException if the repository operation fails
     */
    operator fun invoke(): Flow<List<Task>> = repository.getTasks()
} 