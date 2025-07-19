package amin.codelabs.qdo.feature.tasklist.contract

import amin.codelabs.mvix.core.state.UiState
import amin.codelabs.qdo.domain.task.model.Task

/**
 * UI state for the Task List feature.
 *
 * @property isLoading Whether tasks are being loaded.
 * @property tasks The current list of tasks.
 * @property error Optional error message.
 * @property markingAsDoneTaskId ID of the task currently being marked as done
 */
data class TaskListState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null,
    val markingAsDoneTaskId: Long? = null
) : UiState