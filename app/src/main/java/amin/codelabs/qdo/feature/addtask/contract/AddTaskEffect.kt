package amin.codelabs.qdo.feature.addtask.contract

import amin.codelabs.qdo.infrastructure.mvi.UiEffect

/**
 * One-off UI effects for the Add Task feature (e.g., navigation, snackbars).
 */
sealed interface AddTaskEffect : UiEffect {
    /** Show a snackbar with a message. */
    data class ShowSnackbar(val message: String) : AddTaskEffect
    /** Navigate back after successful save or cancel. */
    object NavigateBack : AddTaskEffect
} 