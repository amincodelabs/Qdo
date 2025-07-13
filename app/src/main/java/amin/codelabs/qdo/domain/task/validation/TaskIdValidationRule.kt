package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import javax.inject.Inject

/**
 * Validates that a task ID is positive.
 */
class DefaultTaskIdValidationRule @Inject constructor() : TaskIdValidationRule {
    override fun validate(taskId: Long) {
        if (taskId <= 0) {
            throw TaskValidationException("Task ID must be greater than 0")
        }
    }
} 