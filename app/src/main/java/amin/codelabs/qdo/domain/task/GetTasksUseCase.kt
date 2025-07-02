package amin.codelabs.qdo.domain.task

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all tasks as a Flow.
 * @param repository The repository to retrieve tasks from.
 */
class GetTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    /**
     * Get a stream of all tasks.
     * @return A Flow emitting the current list of tasks.
     */
    operator fun invoke(): Flow<List<Task>> = repository.getTasks()
} 