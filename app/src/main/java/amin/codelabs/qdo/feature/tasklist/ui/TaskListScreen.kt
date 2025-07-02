package amin.codelabs.qdo.feature.tasklist.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import amin.codelabs.qdo.feature.tasklist.contract.TaskListState
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import amin.codelabs.qdo.feature.tasklist.contract.TaskListEffect
import androidx.compose.foundation.layout.padding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import amin.codelabs.qdo.domain.task.Task

/**
 * Top-level screen for the Task List feature.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskListState,
    onIntent: (TaskListIntent) -> Unit,
    effectFlow: Flow<TaskListEffect>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            // Handle effects (snackbar, navigation)
        }
    }

    Scaffold(
        topBar = { TaskListTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { TaskListFab() },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        TaskListContent(
            state = state,
            onIntent = onIntent,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun TaskListScreenEmptyPreview() {
    TaskListScreen(
        state = TaskListState(tasks = emptyList()),
        onIntent = {},
        effectFlow = emptyFlow()
    )
}

@Preview(showBackground = true, name = "Filled State")
@Composable
private fun TaskListScreenFilledPreview() {
    TaskListScreen(
        state = TaskListState(
            tasks = listOf(
                Task(id = 1, title = "Buy groceries", description = "Milk, eggs, bread"),
                Task(id = 2, title = "Read a book", description = "Atomic Habits"),
                Task(id = 3, title = "Go to the gym", description = "Leg day workout"),
                Task(id = 4, title = "Visit Grandma", description = "She's been asking for a visit"),
            )
        ),
        onIntent = {},
        effectFlow = emptyFlow()
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun TaskListScreenErrorPreview() {
    TaskListScreen(
        state = TaskListState(error = "Something went wrong!"),
        onIntent = {},
        effectFlow = emptyFlow()
    )
} 