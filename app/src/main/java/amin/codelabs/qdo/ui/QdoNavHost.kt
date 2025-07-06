package amin.codelabs.qdo.ui

import amin.codelabs.qdo.feature.tasklist.TaskListViewModel
import amin.codelabs.qdo.feature.tasklist.contract.TaskListIntent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import amin.codelabs.qdo.ui.navigation.taskListNavGraph
import amin.codelabs.qdo.ui.navigation.addTaskNavGraph
import amin.codelabs.qdo.ui.navigation.taskDetailsNavGraph
import androidx.hilt.navigation.compose.hiltViewModel

sealed class QdoDestination(val route: String) {
    data object TaskList : QdoDestination("tasklist")
    data object AddTask : QdoDestination("addtask")
    data object TaskDetail : QdoDestination("taskdetail/{taskId}") {
        fun createRoute(taskId: Long) = "taskdetail/$taskId"
    }
}

@Composable
fun QdoNavHost(navController: NavHostController, taskListViewModel: TaskListViewModel = hiltViewModel()) {
    NavHost(
        navController = navController,
        startDestination = QdoDestination.TaskList.route
    ) {
        taskListNavGraph(navController, taskListViewModel)
        addTaskNavGraph(navController)
        taskDetailsNavGraph(navController, onTaskDeleted = {
            taskListViewModel.processIntent(TaskListIntent.TaskDeleted)
        })
    }
}

