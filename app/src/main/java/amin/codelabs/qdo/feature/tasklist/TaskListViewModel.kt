package amin.codelabs.qdo.feature.tasklist

import amin.codelabs.qdo.domain.task.DeleteTaskUseCase
import amin.codelabs.qdo.domain.task.GetTasksUseCase
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent.*
import amin.codelabs.qdo.feature.tasklist.contract.TaskListState
import amin.codelabs.qdo.feature.tasklist.contract.TaskListEffect
import amin.codelabs.qdo.infrastructure.logger.Logger
import amin.codelabs.qdo.infrastructure.mvi.BaseMviViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for the Task List feature, using MVI architecture.
 * Handles loading and deleting tasks, and emits UI state and effects.
 */
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasks: GetTasksUseCase,
    private val deleteTask: DeleteTaskUseCase,
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
                    setState { copy(isLoading = false, tasks = tasks, error = null) }
                }
        }
    }

    override suspend fun handleIntent(intent: TaskListIntent) {
        when (intent) {
            is DeleteTask -> deleteTask(intent.taskId)
            is SelectTask -> sendEffect { TaskListEffect.NavigateToTask(intent.taskId) }
        }
    }

    private suspend fun deleteTask(taskId: Long) {
        setState { copy(isLoading = true) }
        try {
            deleteTask(taskId)
            sendEffect { TaskListEffect.ShowSnackbar("Task deleted") }
        } catch (e: Exception) {
            logger.logError(e, "Failed to delete task")
            setState { copy(isLoading = false, error = e.message) }
            sendEffect { TaskListEffect.ShowSnackbar("Failed to delete task") }
        }
    }
} 