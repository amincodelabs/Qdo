package amin.codelabs.qdo.feature.addtask

import amin.codelabs.qdo.domain.task.AddTaskUseCase
import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent.*
import amin.codelabs.qdo.feature.addtask.contract.AddTaskState
import amin.codelabs.qdo.feature.addtask.contract.AddTaskEffect
import amin.codelabs.qdo.infrastructure.logger.Logger
import amin.codelabs.qdo.infrastructure.mvi.BaseMviViewModel
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
        if (currentState.title.isBlank()) {
            setState { copy(error = "Title cannot be empty") }
            return
        }
        setState { copy(isSaving = true, error = null) }
        try {
            addTask(Task(title = currentState.title, description = currentState.description))
            sendEffect { AddTaskEffect.ShowSnackbar("Task added") }
            delay(1000)
            sendEffect { AddTaskEffect.NavigateBack }
        } catch (e: Exception) {
            logger.logError(e, "Failed to add task")
            setState { copy(isSaving = false, error = e.message) }
            sendEffect { AddTaskEffect.ShowSnackbar("Failed to add task") }
        }
    }
} 