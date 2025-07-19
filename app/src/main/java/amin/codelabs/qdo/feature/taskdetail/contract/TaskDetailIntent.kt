package amin.codelabs.qdo.feature.taskdetail.contract

import amin.codelabs.mvix.core.intent.UiIntent

/**
 * User intents (actions) for the Task Detail feature.
 */
sealed interface TaskDetailIntent : UiIntent {
    /** Load the task by ID. */
    data class LoadTask(val taskId: Long) : TaskDetailIntent

    /** Enter edit mode. */
    data object Edit : TaskDetailIntent

    /** Save changes to the task. */
    data object Save : TaskDetailIntent

    /** Delete the task. */
    data object Delete : TaskDetailIntent

    /** Mark the task as done/undone. */
    data class SetDone(val isDone: Boolean) : TaskDetailIntent

    /** Change the title while editing. */
    data class ChangeTitle(val title: String) : TaskDetailIntent

    /** Change the description while editing. */
    data class ChangeDescription(val description: String) : TaskDetailIntent

    /** Cancel editing. */
    data object CancelEdit : TaskDetailIntent
} 