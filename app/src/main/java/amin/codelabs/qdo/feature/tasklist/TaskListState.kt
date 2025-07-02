package amin.codelabs.qdo.feature.tasklist

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.infrastructure.mvi.UiState

/**
 * UI state for the Task List feature.
 *
 * @property isLoading Whether tasks are being loaded.
 * @property tasks The current list of tasks.
 * @property error Optional error message.
 */
data class TaskListState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
) : UiState 