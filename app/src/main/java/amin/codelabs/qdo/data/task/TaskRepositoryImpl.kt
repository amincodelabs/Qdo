package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.domain.task.TaskRepository
import amin.codelabs.qdo.domain.task.TaskNotFoundException
import amin.codelabs.qdo.infrastructure.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao,
    private val logger: Logger
) : TaskRepository {
    override suspend fun addTask(task: Task) {
        try {
            dao.insertTask(TaskEntityMapper.toEntity(task))
        } catch (e: Exception) {
            throw TaskRepositoryErrorHandler.handleAndConvert(e, "addTask", logger)
        }
    }

    override fun getTasks(): Flow<List<Task>> =
        dao.getAllTasks()
            .map { list -> list.map(TaskEntityMapper::fromEntity) }
            .catch { e -> throw TaskRepositoryErrorHandler.handleAndConvert(e, "getTasks", logger) }

    override suspend fun deleteTask(taskId: Long) {
        try {
            dao.deleteTaskById(taskId)
        } catch (e: Exception) {
            throw TaskRepositoryErrorHandler.handleAndConvert(e, "deleteTask", logger)
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            dao.updateTask(TaskEntityMapper.toEntity(task))
        } catch (e: Exception) {
            throw TaskRepositoryErrorHandler.handleAndConvert(e, "updateTask", logger)
        }
    }

    override fun getTaskById(taskId: Long): Flow<Task?> =
        dao.getTaskById(taskId)
            .map { entity ->
                if (entity == null) throw TaskNotFoundException()
                TaskEntityMapper.fromEntity(entity)
            }
            .catch { e -> throw TaskRepositoryErrorHandler.handleAndConvert(e, "getTaskById", logger) }
} 