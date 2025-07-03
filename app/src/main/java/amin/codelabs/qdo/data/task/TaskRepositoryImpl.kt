package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.domain.task.TaskRepository
import amin.codelabs.qdo.infrastructure.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.util.Log

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao,
    private val logger: Logger
) : TaskRepository {
    override suspend fun addTask(task: Task) {
        logger.logDebug("Inserting task: $task")
        dao.insertTask(TaskEntityMapper.toEntity(task))
    }

    override fun getTasks(): Flow<List<Task>> =
        dao.getAllTasks().map { list -> list.map(TaskEntityMapper::fromEntity) }

    override suspend fun deleteTask(taskId: Long) {
        dao.deleteTaskById(taskId)
    }
} 