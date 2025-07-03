package amin.codelabs.qdo.feature.addtask.ui

import amin.codelabs.qdo.feature.addtask.contract.AddTaskIntent
import amin.codelabs.qdo.feature.addtask.contract.AddTaskState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskForm(
    state: AddTaskState,
    onIntent: (AddTaskIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = { onIntent(AddTaskIntent.EnterTitle(it)) },
            label = { Text("Title") },
            enabled = !state.isSaving,
            isError = state.error != null && state.title.isBlank(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.description,
            onValueChange = { onIntent(AddTaskIntent.EnterDescription(it)) },
            label = { Text("Description") },
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(state.error, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onIntent(AddTaskIntent.SaveTask) },
            enabled = !state.isSaving && state.title.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
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
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTaskFormPreview() {
    AddTaskForm(
        state = AddTaskState(title = "", description = "", isSaving = false, error = null),
        onIntent = {}
    )
} 