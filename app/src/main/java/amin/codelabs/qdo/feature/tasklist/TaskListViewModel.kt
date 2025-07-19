package amin.codelabs.qdo.feature.tasklist

import amin.codelabs.mvix.core.viewmodel.BaseMviViewModel
import amin.codelabs.qdo.domain.task.TaskRepositoryException
import amin.codelabs.qdo.domain.task.usecase.GetTasksUseCase
import amin.codelabs.qdo.domain.task.usecase.UpdateTaskUseCase
import amin.codelabs.qdo.feature.tasklist.contract.TaskListEffect
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent.MarkAsDone
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent.SelectTask
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent.TaskDeleted
import amin.codelabs.qdo.feature.tasklist.contract.TaskListState
import amin.codelabs.qdo.infrastructure.logger.Logger
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
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getTasks()
                .catch { exception ->
                    logger.logError(exception, "Failed to load tasks")

                    when (exception) {
                        is TaskRepositoryException -> {
                            setState {
                                copy(
                                    isLoading = false,
                                    error = "Failed to load tasks. Please try again."
                                )
                            }
                            sendEffect { TaskListEffect.ShowSnackbar("Failed to load tasks") }
                        }

                        else -> {
                            setState {
                                copy(
                                    isLoading = false,
                                    error = "An unexpected error occurred. Please try again."
                                )
                            }
                            sendEffect { TaskListEffect.ShowSnackbar("Failed to load tasks") }
                        }
                    }
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
        } catch (e: TaskRepositoryException) {
            logger.logError(e, "Failed to mark task as done - repository error")
            setState {
                copy(
                    markingAsDoneTaskId = null,
                    error = "Failed to update task. Please try again."
                )
            }
            sendEffect { TaskListEffect.ShowSnackbar("Failed to update task") }
        } catch (e: Exception) {
            logger.logError(e, "Failed to mark task as done - unexpected error")
            setState {
                copy(
                    markingAsDoneTaskId = null,
                    error = "An unexpected error occurred. Please try again."
                )
            }
            sendEffect { TaskListEffect.ShowSnackbar("Failed to update task") }
        }
    }
} 