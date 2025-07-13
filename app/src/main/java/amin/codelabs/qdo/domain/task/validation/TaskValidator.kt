package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task

/**
 * Task validator that composes specific validation rules.
 */
object TaskValidator {
    private val taskIdValidationRule by lazy { DefaultTaskIdValidationRule() }
    private val titleValidationRule by lazy { TitleValidationRule() }
    private val descriptionValidationRule by lazy { DescriptionValidationRule() }

    /**
     * Validates a task before adding it to the repository.
     * @param task The task to validate
     * @throws TaskValidationException if validation fails
     */
    fun validateForCreation(task: Task) {
        titleValidationRule.validate(task)
        descriptionValidationRule.validate(task)
    }

    /**
     * Validates a task before updating it in the repository.
     * @param task The task to validate
     * @throws TaskValidationException if validation fails
     */
    fun validateForUpdate(task: Task) {
        taskIdValidationRule.validate(task.id)
        titleValidationRule.validate(task)
        descriptionValidationRule.validate(task)
    }

    /**
     * Validates a task ID before deletion.
     * @param taskId The task ID to validate
     * @throws TaskValidationException if validation fails
     */
    fun validateForDeletion(taskId: Long) {
        taskIdValidationRule.validate(taskId)
    }

    /**
     * Validates a task ID for retrieval operations.
     * @param taskId The task ID to validate
     * @throws TaskValidationException if validation fails
     */
    fun validateForRetrieval(taskId: Long) {
        taskIdValidationRule.validate(taskId)
    }
} 