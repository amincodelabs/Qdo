package amin.codelabs.qdo.feature.tasklist.ui

import amin.codelabs.qdo.domain.task.model.Task
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.lazy.LazyListState

@Composable
fun TaskList(
    tasks: List<Task>,
    onMarkAsDone: (Long) -> Unit,
    onSelect: (Long) -> Unit,
    markingAsDoneTaskId: Long? = null,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                TaskListItem(
                    task = task,
                    onMarkAsDone = onMarkAsDone,
                    onSelect = onSelect,
                    modifier = Modifier.animateEnterExit(),
                    isMarkingAsDone = markingAsDoneTaskId == task.id
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListPreview() {
    TaskList(
        tasks = listOf(
            Task(id = 1, title = "Buy groceries", description = "Milk, eggs, bread"),
            Task(id = 2, title = "Read a book", description = "Atomic Habits")
        ),
        onMarkAsDone = {},
        onSelect = {},
        listState = LazyListState()
    )
} 