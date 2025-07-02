package amin.codelabs.qdo.feature.addtask.contract

import amin.codelabs.qdo.infrastructure.mvi.UiState

/**
 * UI state for the Add Task feature.
 *
 * @property title The current value of the task title input.
 * @property description The current value of the task description input.
 * @property isSaving Whether the task is being saved.
 * @property error Optional error message.
 */
data class AddTaskState(
    val title: String = "",
    val description: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
) : UiState 