package amin.codelabs.qdo.feature.addtask

import amin.codelabs.mvix.core.viewmodel.BaseMviViewModel
import amin.codelabs.qdo.domain.task.TaskRepositoryException
import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.usecase.AddTaskUseCase
import amin.codelabs.qdo.feature.addtask.contract.AddTaskEffect
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent.Cancel
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent.EnterDescription
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent.EnterTitle
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent.SaveTask
import amin.codelabs.qdo.feature.addtask.contract.AddTaskState
import amin.codelabs.qdo.infrastructure.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * ViewModel for the Add Task feature, using MVI architecture.
 * Handles user input, saving, and emits UI state and effects.
 */
@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addTask: AddTaskUseCase,
    private val logger: Logger
) : BaseMviViewModel<AddTaskIntent, AddTaskState, AddTaskEffect>(AddTaskState()) {
    override suspend fun handleIntent(intent: AddTaskIntent) {
        when (intent) {
            is EnterTitle -> setState { copy(title = intent.title, error = null) }
            is EnterDescription -> setState { copy(description = intent.description, error = null) }
            is SaveTask -> saveTask()
            is Cancel -> sendEffect { AddTaskEffect.NavigateBack }
        }
    }

    private suspend fun saveTask() {
        val currentState = state.value

        // Clear previous errors
        setState { copy(error = null) }

        // Basic UI validation
        if (currentState.title.isBlank()) {
            setState { copy(error = "Title cannot be empty") }
            return
        }

        setState { copy(isSaving = true) }

        try {
            // Create task with trimmed values
            val task = Task(
                title = currentState.title.trim(),
                description = currentState.description?.trim()?.takeIf { it.isNotBlank() }
            )

            addTask(task)

            // Success - show message and navigate back
            sendEffect { AddTaskEffect.ShowSnackbar("Task added successfully") }
            delay(1000) // Give user time to see success message
            sendEffect { AddTaskEffect.NavigateBack }

        } catch (e: TaskValidationException) {
            logger.logDebug("Task validation failed: ${e.message}")
            setState {
                copy(
                    isSaving = false,
                    error = e.message ?: "Invalid task data"
                )
            }
        } catch (e: TaskRepositoryException) {
            logger.logError(e, "Repository error while adding task")
            setState { copy(isSaving = false, error = "Failed to save task. Please try again.") }
            sendEffect { AddTaskEffect.ShowSnackbar("Failed to add task") }

        } catch (e: Exception) {
            // Unexpected errors - log and show generic message
            logger.logError(e, "Unexpected error while adding task")
            setState {
                copy(
                    isSaving = false,
                    error = "An unexpected error occurred. Please try again."
                )
            }
            sendEffect { AddTaskEffect.ShowSnackbar("Failed to add task") }
        }
    }
} 