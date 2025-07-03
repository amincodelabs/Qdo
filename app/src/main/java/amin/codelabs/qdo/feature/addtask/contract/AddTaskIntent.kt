package amin.codelabs.qdo.feature.addtask.contract

import amin.codelabs.qdo.infrastructure.mvi.UiIntent

/**
 * User intents (actions) for the Add Task feature.
 */
sealed interface AddTaskIntent : UiIntent {
    /** User enters or changes the task title. */
    data class EnterTitle(val title: String) : AddTaskIntent
    /** User enters or changes the task description. */
    data class EnterDescription(val description: String) : AddTaskIntent
    /** User clicks the save/add button. */
    data object SaveTask : AddTaskIntent
    /** User clicks cancel/back. */
    data object Cancel : AddTaskIntent
} 