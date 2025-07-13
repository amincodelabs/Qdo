package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task

/**
 * Validates task domain rules and business logic.
 */
object TaskValidator {
    
    private const val MAX_TITLE_LENGTH = 100
    private const val MAX_DESCRIPTION_LENGTH = 500
    private const val MIN_TITLE_LENGTH = 1
    
    /**
     * Validates a task before adding it to the repository.
     * @param task The task to validate
     * @throws TaskValidationException if validation fails
     */
    fun validateForCreation(task: Task) {
        validateTitle(task.title)
        validateDescription(task.description)
    }
    
    /**
     * Validates a task before updating it in the repository.
     * @param task The task to validate
     * @throws TaskValidationException if validation fails
     */
    fun validateForUpdate(task: Task) {
        if (task.id <= 0) {
            throw TaskValidationException("Task ID must be greater than 0")
        }
        validateTitle(task.title)
        validateDescription(task.description)
    }
    
    private fun validateTitle(title: String) {
        when {
            title.isBlank() -> throw TaskValidationException("Task title cannot be empty")
            title.length < MIN_TITLE_LENGTH -> throw TaskValidationException("Task title must be at least $MIN_TITLE_LENGTH character")
            title.length > MAX_TITLE_LENGTH -> throw TaskValidationException("Task title cannot exceed $MAX_TITLE_LENGTH characters")
            title.trim() != title -> throw TaskValidationException("Task title cannot start or end with whitespace")
        }
    }
    
    private fun validateDescription(description: String?) {
        description?.let { desc ->
            if (desc.length > MAX_DESCRIPTION_LENGTH) {
                throw TaskValidationException("Task description cannot exceed $MAX_DESCRIPTION_LENGTH characters")
            }
            if (desc.trim() != desc) {
                throw TaskValidationException("Task description cannot start or end with whitespace")
            }
        }
    }
} 