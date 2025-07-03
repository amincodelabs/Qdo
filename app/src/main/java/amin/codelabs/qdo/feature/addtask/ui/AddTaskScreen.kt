package amin.codelabs.qdo.feature.addtask.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import amin.codelabs.qdo.feature.addtask.contract.AddTaskState
import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent
import amin.codelabs.qdo.feature.addtask.contract.AddTaskEffect
import amin.codelabs.qdo.feature.tasklist.contract.TaskListEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AddTaskScreen(
    state: AddTaskState,
    onIntent: (AddTaskIntent) -> Unit,
    effectFlow: Flow<AddTaskEffect>,
    onNavigateToTask: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is AddTaskEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                is AddTaskEffect.NavigateBack -> onNavigateToTask()
            }
        }
    }

    Scaffold(
        topBar = { AddTaskTopBar(onCancel = { onIntent(AddTaskIntent.Cancel) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        AddTaskForm(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun AddTaskScreenEmptyPreview() {
    AddTaskScreen(
        state = AddTaskState(),
        onIntent = {},
        effectFlow = emptyFlow(),
        onNavigateToTask = {}
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun AddTaskScreenErrorPreview() {
    AddTaskScreen(
        state = AddTaskState(error = "Title cannot be empty"),
        onIntent = {},
        effectFlow = emptyFlow(),
        onNavigateToTask = {}
    )
}

@Preview(showBackground = true, name = "Saving State")
@Composable
private fun AddTaskScreenSavingPreview() {
    AddTaskScreen(
        state = AddTaskState(title = "New Task", description = "Description", isSaving = true),
        onIntent = {},
        effectFlow = emptyFlow(),
        onNavigateToTask = {}
    )
} 