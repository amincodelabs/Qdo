package amin.codelabs.qdo.domain.task

/**
 * Domain model representing a user task.
 *
 * @property id Unique identifier for the task.
 * @property title Title of the task.
 * @property description Optional description of the task.
 * @property isDone Whether the task is completed.
 * @property createdAt Timestamp when the task was created (milliseconds since epoch).
 */
data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) 