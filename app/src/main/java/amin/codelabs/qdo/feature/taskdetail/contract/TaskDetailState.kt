package amin.codelabs.qdo.feature.taskdetail.contract

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.infrastructure.mvi.UiState

/**
 * UI state for the Task Detail feature.
 *
 * @property task The current task being viewed/edited.
 * @property isLoading Whether the task is being loaded.
 * @property isEditing Whether the task is in edit mode.
 * @property isSaving Whether the task is being saved.
 * @property error Optional error message.
 * @property editTitle The editable title (when editing).
 * @property editDescription The editable description (when editing).
 */
data class TaskDetailState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val editTitle: String = "",
    val editDescription: String = ""
) : UiState 