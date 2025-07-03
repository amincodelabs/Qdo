package amin.codelabs.qdo.feature.tasklist.contract

import amin.codelabs.qdo.infrastructure.mvi.UiIntent

/**
 * User intents (actions) for the Task List feature.
 */
sealed interface TaskListIntent : UiIntent {
    /** User requests to delete a task. */
    data class DeleteTask(val taskId: Long) : TaskListIntent
    /** User clicks on a task (for details, edit, etc). */
    data class SelectTask(val taskId: Long) : TaskListIntent
} 