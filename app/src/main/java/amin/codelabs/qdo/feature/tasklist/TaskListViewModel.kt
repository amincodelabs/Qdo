package amin.codelabs.qdo.feature.tasklist

import amin.codelabs.qdo.domain.task.GetTasksUseCase
import amin.codelabs.qdo.domain.task.UpdateTaskUseCase
import amin.codelabs.qdo.feature.tasklist.contract.TaskListEffect
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent.*
import amin.codelabs.qdo.feature.tasklist.contract.TaskListState
import amin.codelabs.qdo.infrastructure.logger.Logger
import amin.codelabs.qdo.infrastructure.mvi.BaseMviViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Task List feature, using MVI architecture.
 * Handles loading and deleting tasks, and emits UI state and effects.
 */
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasks: GetTasksUseCase,
    private val updateTask: UpdateTaskUseCase,
    private val logger: Logger
) : BaseMviViewModel<TaskListIntent, TaskListState, TaskListEffect>(TaskListState()) {

    init {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getTasks()
                .catch {
                    logger.logError(it, "Failed to load tasks")
                    setState { copy(isLoading = false, error = it.message) }
                }
                .collectLatest { tasks ->
                    setState { 
                        copy(
                            isLoading = false, 
                            tasks = tasks.sortedBy { it.isDone }, 
                            error = null
                        ) 
                    }
                }
        }
    }

    override suspend fun handleIntent(intent: TaskListIntent) {
        when (intent) {
            is MarkAsDone -> markAsDone(intent.taskId)
            is SelectTask -> sendEffect { TaskListEffect.NavigateToTask(intent.taskId) }
            is TaskDeleted -> sendEffect { TaskListEffect.ShowSnackbar("Task deleted successfully") }
        }
    }

    private suspend fun markAsDone(taskId: Long) {
        setState { copy(markingAsDoneTaskId = taskId) }
        try {
            val task = state.value.tasks.find { it.id == taskId }
            if (task != null) {
                updateTask(task.copy(isDone = true))
                setState { copy(markingAsDoneTaskId = null) }
                sendEffect { TaskListEffect.ShowSnackbar("Great job!") }
            }
        } catch (e: Exception) {
            logger.logError(e, "Failed to mark task as done")
            setState { copy(markingAsDoneTaskId = null, error = e.message) }
            sendEffect { TaskListEffect.ShowSnackbar("Failed to mark task as done") }
        }
    }
} 