package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.model.Task

/**
 * Base interface for task validation rules.
 */
interface TaskValidationRule {
    /**
     * Validates the given task or task data.
     * @param task The task to validate
     * @throws TaskValidationException if validation fails
     */
    fun validate(task: Task)
}

/**
 * Interface for task ID validation rules.
 */
interface TaskIdValidationRule {
    /**
     * Validates the given task ID.
     * @param taskId The task ID to validate
     * @throws TaskValidationException if validation fails
     */
    fun validate(taskId: Long)
} 