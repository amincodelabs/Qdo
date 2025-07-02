package amin.codelabs.qdo.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import amin.codelabs.qdo.feature.tasklist.TaskListViewModel
import amin.codelabs.qdo.feature.tasklist.ui.TaskListScreen
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

sealed class QdoDestination(val route: String) {
    object TaskList : QdoDestination("tasklist")
    object TaskDetail : QdoDestination("taskdetail/{taskId}") {
        fun createRoute(taskId: Long) = "taskdetail/$taskId"
    }
}

@Composable
fun QdoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = QdoDestination.TaskList.route
    ) {
        composable(QdoDestination.TaskList.route) {
            val viewModel: TaskListViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            val effect = viewModel.effect
            TaskListScreen(
                state = state,
                onIntent = { viewModel.processIntent(it) },
                effectFlow = effect
            )
        }
    }
} 