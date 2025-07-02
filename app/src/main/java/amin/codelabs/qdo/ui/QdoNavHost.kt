package amin.codelabs.qdo.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import amin.codelabs.qdo.ui.navigation.taskListNavGraph
import amin.codelabs.qdo.ui.navigation.addTaskNavGraph

sealed class QdoDestination(val route: String) {
    object TaskList : QdoDestination("tasklist")
    object AddTask : QdoDestination("addtask")
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
        taskListNavGraph(navController)
        addTaskNavGraph(navController)
    }
} 