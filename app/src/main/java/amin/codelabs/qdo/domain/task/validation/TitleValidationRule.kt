package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import javax.inject.Inject

/**
 * Validates task title format and content.
 */
class TitleValidationRule @Inject constructor() : TaskValidationRule {
    
    private val maxLength = 100
    private val minLength = 1
    
    override fun validate(task: Task) {
        validateTitle(task.title)
    }
    
    fun validateTitle(title: String) {
        when {
            title.isBlank() -> throw TaskValidationException("Task title cannot be empty")
            title.length < minLength -> throw TaskValidationException("Task title must be at least $minLength character")
            title.length > maxLength -> throw TaskValidationException("Task title cannot exceed $maxLength characters")
            title.trim() != title -> throw TaskValidationException("Task title cannot start or end with whitespace")
        }
    }
} 