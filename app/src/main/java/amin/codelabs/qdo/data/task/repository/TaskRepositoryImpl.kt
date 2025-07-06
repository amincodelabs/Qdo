package amin.codelabs.qdo.data.task.repository

import amin.codelabs.qdo.data.task.dao.TaskDao
import amin.codelabs.qdo.data.task.mapper.TaskEntityMapper
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {
    override suspend fun addTask(task: Task) {
        dao.insertTask(TaskEntityMapper.toEntity(task))
    }

    override fun getTasks(): Flow<List<Task>> =
        dao.getAllTasks().map { list -> list.map(TaskEntityMapper::fromEntity) }

    override suspend fun deleteTask(taskId: Long) {
        dao.deleteTaskById(taskId)
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(TaskEntityMapper.toEntity(task))
    }

    override fun getTaskById(taskId: Long): Flow<Task?> =
        dao.getTaskById(taskId).map { it?.let(TaskEntityMapper::fromEntity) }
} 