package amin.codelabs.qdo.feature.tasklist.contract

import amin.codelabs.mvix.core.effect.UiEffect

/**
 * One-off UI effects for the Task List feature (e.g., navigation, snackbars).
 */
sealed interface TaskListEffect : UiEffect {
    /** Show a snackbar with a message. */
    data class ShowSnackbar(val message: String) : TaskListEffect

    /** Navigate to the details screen for a task. */
    data class NavigateToTask(val taskId: Long) : TaskListEffect
} 