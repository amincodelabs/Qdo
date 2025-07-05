package amin.codelabs.qdo.ui.navigation

import amin.codelabs.qdo.feature.taskdetail.TaskDetailViewModel
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailEffect
import amin.codelabs.qdo.feature.taskdetail.contract.TaskDetailIntent
import amin.codelabs.qdo.feature.taskdetail.ui.TaskDetailScreen
import amin.codelabs.qdo.ui.QdoDestination
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun NavGraphBuilder.taskDetailsNavGraph(navController: NavHostController) {
    composable(QdoDestination.TaskDetail.route) { backStackEntry ->
        val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
        val viewModel: TaskDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        val effect = viewModel.effect
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(taskId) {
            if (taskId != null) {
                viewModel.processIntent(TaskDetailIntent.LoadTask(taskId))
            }
        }

        LaunchedEffect(effect) {
            effect.collect { e ->
                when (e) {
                    is TaskDetailEffect.ShowSnackbar -> snackbarHostState.showSnackbar(e.message)
                    is TaskDetailEffect.NavigateBack -> navController.popBackStack()
                }
            }
        }

        TaskDetailScreen(
            state = state,
            onIntent = viewModel::processIntent,
            snackbarHostState = snackbarHostState
        )
    }
}