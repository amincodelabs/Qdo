package amin.codelabs.qdo.feature.tasklist.ui

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
import amin.codelabs.qdo.domain.task.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    onDelete: (Long) -> Unit,
    onSelect: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            TaskListItem(task = task, onDelete = onDelete, onSelect = onSelect)
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
        onDelete = {},
        onSelect = {}
    )
} 