package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.domain.task.Task

object TaskEntityMapper {
    fun fromEntity(entity: TaskEntity): Task = Task(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        isDone = entity.isDone,
        createdAt = entity.createdAt
    )

    fun toEntity(task: Task): TaskEntity = TaskEntity(
        id = task.id,
        title = task.title,
        description = task.description,
        isDone = task.isDone,
        createdAt = task.createdAt
    )
} 