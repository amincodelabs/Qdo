package amin.codelabs.qdo.feature.taskdetail.contract

import amin.codelabs.mvix.core.effect.UiEffect

/**
 * One-off UI effects for the Task Detail feature (e.g., navigation, snackbars).
 */
sealed interface TaskDetailEffect : UiEffect {
    /** Show a snackbar with a message. */
    data class ShowSnackbar(val message: String) : TaskDetailEffect

    /** Navigate back after delete or save. */
    data object NavigateBack : TaskDetailEffect
} 