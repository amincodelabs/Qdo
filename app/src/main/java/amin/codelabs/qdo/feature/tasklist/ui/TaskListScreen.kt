package amin.codelabs.qdo.feature.tasklist.ui

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import amin.codelabs.qdo.feature.tasklist.contract.TaskListState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

/**
 * Top-level screen for the Task List feature.
 */
@Composable
fun TaskListScreen(
    state: TaskListState,
    onIntent: (TaskListIntent) -> Unit,
    onAddTask: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    Scaffold(
        topBar = { TaskListTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { TaskListFab(onClick = onAddTask) },
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
                Task(
                    id = 4,
                    title = "Visit Grandma",
                    description = "She's been asking for a visit"
                ),
            )
        ),
        onIntent = {},
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun TaskListScreenErrorPreview() {
    TaskListScreen(
        state = TaskListState(error = "Something went wrong!"),
        onIntent = {},
    )
} 