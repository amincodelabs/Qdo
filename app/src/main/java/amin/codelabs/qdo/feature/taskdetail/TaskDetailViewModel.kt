package amin.codelabs.qdo.feature.taskdetail

import amin.codelabs.qdo.domain.task.TaskDatabaseException
import amin.codelabs.qdo.domain.task.TaskRepositoryException
import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.usecase.DeleteTaskUseCase
import amin.codelabs.qdo.domain.task.usecase.GetTasksUseCase
import amin.codelabs.qdo.domain.task.usecase.UpdateTaskUseCase
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailEffect
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.CancelEdit
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.ChangeDescription
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.ChangeTitle
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.Delete
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.Edit
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.LoadTask
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.Save
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent.SetDone
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailState
import amin.codelabs.qdo.infrastructure.logger.Logger
import amin.codelabs.qdo.infrastructure.mvi.BaseMviViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTasks: GetTasksUseCase,
    private val updateTask: UpdateTaskUseCase,
    private val deleteTask: DeleteTaskUseCase,
    private val logger: Logger
) : BaseMviViewModel<TaskDetailIntent, TaskDetailState, TaskDetailEffect>(TaskDetailState()) {

    override suspend fun handleIntent(intent: TaskDetailIntent) {
        when (intent) {
            is LoadTask -> loadTask(intent.taskId)
            is Edit -> enterEditMode()
            is Save -> saveTask()
            is Delete -> deleteCurrentTask()
            is SetDone -> setDone(intent.isDone)
            is ChangeTitle -> setState { copy(editTitle = intent.title) }
            is ChangeDescription -> setState { copy(editDescription = intent.description) }
            is CancelEdit -> cancelEdit()
        }
    }

    private fun loadTask(taskId: Long) {
        setState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getTasks().catch { exception ->
                setState {
                    copy(
                        isLoading = false,
                        error = "Failed to load tasks. Please try again."
                    )
                }
                logger.logError(exception, "Failed to load tasks")
            }.collectLatest { tasks ->
                val task = tasks.find { it.id == taskId }
                if (task != null) {
                    setState {
                        copy(
                            isLoading = false,
                            task = task,
                            editTitle = task.title,
                            editDescription = task.description ?: ""
                        )
                    }
                } else {
                    setState { copy(isLoading = false, error = "Task not found") }
                    sendEffect { TaskDetailEffect.NavigateBack }
                }
            }
        }
    }

    private fun enterEditMode() {
        val task = state.value.task ?: return
        setState {
            copy(
                isEditing = true,
                editTitle = task.title,
                editDescription = task.description ?: ""
            )
        }
    }

    private fun cancelEdit() {
        val task = state.value.task ?: return
        setState {
            copy(
                isEditing = false,
                editTitle = task.title,
                editDescription = task.description ?: ""
            )
        }
    }

    private fun setDone(isDone: Boolean) {
        val task = state.value.task ?: return
        viewModelScope.launch {
            try {
                updateTask(task.copy(isDone = isDone))
                setState { copy(task = task.copy(isDone = isDone)) }
                sendEffect { TaskDetailEffect.ShowSnackbar(if (isDone) "Marked as done" else "Marked as not done") }
            } catch (e: TaskValidationException) {
                logger.logDebug("Task update validation failed: ${e.message}")
                setState { 
                    copy(error = e.message ?: "Invalid task data")
                }
                sendEffect { TaskDetailEffect.ShowSnackbar("Invalid task data") }
            }  catch (e: TaskRepositoryException) {
                logger.logError(e, "Failed to update task done state - repository error")
                setState { copy(error = "Failed to update task. Please try again.") }
                sendEffect { TaskDetailEffect.ShowSnackbar("Failed to update task") }
            } catch (e: Exception) {
                logger.logError(e, "Failed to update task done state - unexpected error")
                setState { copy(error = "An unexpected error occurred. Please try again.") }
                sendEffect { TaskDetailEffect.ShowSnackbar("Failed to update task") }
            }
        }
    }

    private fun saveTask() {
        val currentState = state.value
        val task = currentState.task ?: return
        val newTitle = currentState.editTitle.trim()
        val newDescription = currentState.editDescription.trim()
        
        // Clear previous errors
        setState { copy(error = null) }
        
        // Basic UI validation
        if (newTitle.isBlank()) {
            setState { copy(error = "Title cannot be empty") }
            return
        }
        
        setState { copy(isSaving = true) }
        viewModelScope.launch {
            try {
                updateTask(task.copy(title = newTitle, description = newDescription))
                setState {
                    copy(
                        isEditing = false,
                        isSaving = false,
                        task = task.copy(title = newTitle, description = newDescription)
                    )
                }
                sendEffect { TaskDetailEffect.ShowSnackbar("Task updated") }
            } catch (e: TaskValidationException) {
                logger.logDebug("Task update validation failed: ${e.message}")
                setState { 
                    copy(
                        isSaving = false, 
                        error = e.message ?: "Invalid task data"
                    ) 
                }
                sendEffect { TaskDetailEffect.ShowSnackbar("Invalid task data") }
            } catch (e: TaskRepositoryException) {
                logger.logError(e, "Failed to update task - repository error")
                setState { copy(isSaving = false, error = "Failed to update task. Please try again.") }
                sendEffect { TaskDetailEffect.ShowSnackbar("Failed to update task") }
            } catch (e: Exception) {
                logger.logError(e, "Failed to update task - unexpected error")
                setState { copy(isSaving = false, error = "An unexpected error occurred. Please try again.") }
                sendEffect { TaskDetailEffect.ShowSnackbar("Failed to update task") }
            }
        }
    }

    private fun deleteCurrentTask() {
        val task = state.value.task ?: return

        setState { copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                deleteTask(task.id)
                sendEffect { TaskDetailEffect.NavigateBack }
            } catch (e: TaskValidationException) {
                logger.logDebug("Task deletion validation failed: ${e.message}")
                setState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Task not found"
                    )
                }
                sendEffect { TaskDetailEffect.ShowSnackbar("Task not found") }

            } catch (e: TaskRepositoryException) {
                logger.logError(e, "Repository error while deleting task")
                setState {
                    copy(
                        isLoading = false,
                        error = "Failed to delete task. Please try again."
                    )
                }
                sendEffect { TaskDetailEffect.ShowSnackbar("Failed to delete task") }

            } catch (e: Exception) {
                logger.logError(e, "Unexpected error while deleting task")
                setState {
                    copy(
                        isLoading = false,
                        error = "An unexpected error occurred. Please try again."
                    )
                }
                sendEffect { TaskDetailEffect.ShowSnackbar("Failed to delete task") }
            }
        }
    }
}