package amin.codelabs.qdo.domain.task

// Base exception for all repository errors
sealed class TaskRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)

// Thrown when a task is not found
class TaskNotFoundException(message: String = "Task not found", cause: Throwable? = null) : TaskRepositoryException(message, cause)

// Thrown when task data is invalid
class TaskValidationException(message: String, cause: Throwable? = null) : TaskRepositoryException(message, cause)

// Thrown for database-related errors (wraps SQLite, Room, or IO errors)
class TaskDatabaseException(message: String, cause: Throwable? = null) : TaskRepositoryException(message, cause)

// Thrown for unknown/unexpected errors
class TaskUnknownException(message: String = "Unknown error", cause: Throwable? = null) : TaskRepositoryException(message, cause) 