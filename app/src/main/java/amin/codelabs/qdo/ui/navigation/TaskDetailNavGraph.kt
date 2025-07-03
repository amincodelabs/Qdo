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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

// TODO: Implement the Task Details screen
fun NavGraphBuilder.taskDetailsNavGraph(navController: NavHostController) {
    composable(QdoDestination.TaskList.route) {

    }
}