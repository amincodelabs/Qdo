package amin.codelabs.qdo.ui.navigation

import amin.codelabs.qdo.feature.tasklist.TaskListViewModel
import amin.codelabs.qdo.feature.tasklist.contract.TaskListEffect
import amin.codelabs.qdo.feature.tasklist.ui.TaskListScreen
import amin.codelabs.qdo.ui.QdoDestination
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun NavGraphBuilder.taskListNavGraph(
    navController: NavHostController,
    viewModel: TaskListViewModel
) {
    composable(QdoDestination.TaskList.route) {
        val state by viewModel.state.collectAsState()
        val effect = viewModel.effect
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(effect) {
            effect.collect { e ->
                when (e) {
                    is TaskListEffect.ShowSnackbar -> snackbarHostState.showSnackbar(e.message)
                    is TaskListEffect.NavigateToTask -> {
                        navController.navigate(QdoDestination.TaskDetail.createRoute(e.taskId))
                    }
                }
            }
        }

        TaskListScreen(
            state = state,
            onIntent = viewModel::processIntent,
            onAddTask = { navController.navigate(QdoDestination.AddTask.route) },
            snackbarHostState = snackbarHostState,
        )
    }
} 