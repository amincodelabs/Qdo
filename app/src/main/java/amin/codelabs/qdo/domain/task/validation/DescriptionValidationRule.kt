package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import javax.inject.Inject

/**
 * Validates task description format and content.
 */
class DescriptionValidationRule @Inject constructor() : TaskValidationRule {

    private val maxLength = 500

    override fun validate(task: Task) {
        validateDescription(task.description)
    }

    fun validateDescription(description: String?) {
        description?.let { desc ->
            if (desc.length > maxLength) {
                throw TaskValidationException("Task description cannot exceed $maxLength characters")
            }
            if (desc.trim() != desc) {
                throw TaskValidationException("Task description cannot start or end with whitespace")
            }
        }
    }
} 