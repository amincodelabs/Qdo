package amin.codelabs.qdo.feature.taskdetail.ui

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun TaskDetailScreen(
    state: TaskDetailState,
    onIntent: (TaskDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TaskDetailTopBar(
                isEditing = state.isEditing,
                onEdit = { onIntent(TaskDetailIntent.Edit) },
                onDelete = { onIntent(TaskDetailIntent.Delete) }
            )
        },
        bottomBar = {
            if (!state.isEditing && state.task != null) {
                Button(
                    onClick = { onIntent(TaskDetailIntent.SetDone(!state.task.isDone)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !state.task.isDone
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (state.task.isDone) "Completed" else "Mark as Done")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.task != null -> {
                    TaskDetailContent(state, onIntent, Modifier.align(Alignment.TopCenter))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDetailTopBar(
    isEditing: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {},
        actions = {
            if (!isEditing) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun TaskDetailContent(
    state: TaskDetailState,
    onIntent: (TaskDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val task = state.task
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        if (state.isEditing) {
            OutlinedTextField(
                value = state.editTitle,
                onValueChange = { onIntent(TaskDetailIntent.ChangeTitle(it)) },
                label = { Text("Title") },
                enabled = !state.isSaving,
                isError = state.editTitle.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.editDescription,
                onValueChange = { onIntent(TaskDetailIntent.ChangeDescription(it)) },
                label = { Text("Description") },
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = { onIntent(TaskDetailIntent.Save) },
                    enabled = !state.isSaving && state.editTitle.isNotBlank(),
                    shape = MaterialTheme.shapes.small
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save")
                    }
                }
                OutlinedButton(
                    onClick = { onIntent(TaskDetailIntent.CancelEdit) },
                    enabled = !state.isSaving,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Cancel")
                }
            }
        } else if (task != null) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = formatRelativeTime(task.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (!task.description.isNullOrBlank()) {
                Text(
                    text = task.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    ),
                    enabled = false
                )
                Text(
                    if (task.isDone) "Done" else "Not done",
                    color = if (task.isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun formatRelativeTime(createdAt: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - createdAt
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val weeks = days / 7
    val months = days / 30
    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
        hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
        days < 7 -> "$days day${if (days == 1L) "" else "s"} ago"
        weeks < 4 -> "$weeks week${if (weeks == 1L) "" else "s"} ago"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(createdAt))
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskDetailScreenPreview() {
    TaskDetailScreen(
        state = TaskDetailState(
            task = Task(
                id = 1,
                title = "Buy groceries",
                description = "Milk, eggs, bread",
                isDone = false
            ),
            isLoading = false,
            isEditing = false
        ),
        onIntent = {}
    )
} 