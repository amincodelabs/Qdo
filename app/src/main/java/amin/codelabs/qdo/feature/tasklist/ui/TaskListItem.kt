package amin.codelabs.qdo.feature.tasklist.ui

import amin.codelabs.qdo.domain.task.Task
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import amin.codelabs.qdo.infrastructure.datetime.DateTimeFormatter

@Composable
fun TaskListItem(
    task: Task,
    onMarkAsDone: (Long) -> Unit,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isMarkingAsDone: Boolean = false
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(task.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!task.description.isNullOrBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                }
                Text(
                    text = DateTimeFormatter.AutoRefreshingRelativeTime(task.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            if (isMarkingAsDone) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (!task.isDone) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .size(40.dp)
                        .clickable { onMarkAsDone(task.id) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Mark as Done",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskListItemPreview() {
    TaskListItem(
        task = Task(id = 1, title = "Buy groceries", description = "Milk, eggs, bread"),
        onMarkAsDone = {},
        onSelect = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun TaskListItemDonePreview() {
    TaskListItem(
        task = Task(
            id = 1,
            title = "Buy groceries",
            description = "Milk, eggs, bread",
            isDone = true
        ),
        onMarkAsDone = {},
        onSelect = {}
    )
}