package amin.codelabs.qdo.feature.taskdetail.ui

import amin.codelabs.qdo.domain.task.Task
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskDetailScreen(
    state: TaskDetailState,
    onIntent: (TaskDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
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

@Composable
private fun TaskDetailContent(
    state: TaskDetailState,
    onIntent: (TaskDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onIntent(TaskDetailIntent.Save) },
                    enabled = !state.isSaving && state.editTitle.isNotBlank()
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
                    enabled = !state.isSaving
                ) {
                    Text("Cancel")
                }
            }
        } else {
            Text(
                text = state.task?.title ?: "",
                style = MaterialTheme.typography.titleLarge
            )
            if (!state.task?.description.isNullOrBlank()) {
                Text(
                    text = state.task?.description ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = state.task?.isDone ?: false,
                    onCheckedChange = { onIntent(TaskDetailIntent.SetDone(it)) }
                )
                Text(if (state.task?.isDone == true) "Done" else "Not done")
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { onIntent(TaskDetailIntent.Edit) }) {
                    Text("Edit")
                }
                OutlinedButton(onClick = { onIntent(TaskDetailIntent.Delete) }) {
                    Text("Delete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskDetailScreenPreview() {
    TaskDetailScreen(
        state = TaskDetailState(
            task = Task(id = 1, title = "Buy groceries", description = "Milk, eggs, bread", isDone = false),
            isLoading = false,
            isEditing = false
        ),
        onIntent = {}
    )
} 