package amin.codelabs.qdo.feature.tasklist.ui

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import amin.codelabs.qdo.feature.tasklist.contract.TaskListState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyListState

@Composable
fun TaskListContent(
    state: TaskListState,
    onIntent: (TaskListIntent) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                ErrorView(message = state.error)
            }

            state.tasks.isEmpty() -> {
                EmptyView()
            }

            else -> {
                TaskList(
                    tasks = state.tasks,
                    onMarkAsDone = { onIntent(TaskListIntent.MarkAsDone(it)) },
                    onSelect = { onIntent(TaskListIntent.SelectTask(taskId = it))},
                    markingAsDoneTaskId = state.markingAsDoneTaskId,
                    listState = listState
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListContentPreview() {
    TaskListContent(
        state = TaskListState(
            isLoading = false,
            tasks = listOf(
                Task(id = 1, title = "Buy groceries", description = "Milk, eggs, bread"),
                Task(id = 2, title = "Read a book", description = "Atomic Habits")
            ),
            error = null
        ),
        onIntent = {},
        listState = LazyListState()
    )
} 