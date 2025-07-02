package amin.codelabs.qdo.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import amin.codelabs.qdo.feature.addtask.AddTaskViewModel
import amin.codelabs.qdo.feature.addtask.ui.AddTaskScreen
import amin.codelabs.qdo.feature.addtask.contract.AddTaskEffect
import amin.codelabs.qdo.ui.QdoDestination
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

fun NavGraphBuilder.addTaskNavGraph(navController: NavHostController) {
    composable(QdoDestination.AddTask.route) {
        val viewModel: AddTaskViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        val effect = viewModel.effect
        AddTaskScreen(
            state = state,
            onIntent = { viewModel.processIntent(it) },
            effectFlow = effect,
            onNavigateToTask = {
                navController.popBackStack()
            }
        )
        LaunchedEffect(effect) {
            effect.collect { e ->
                if (e is AddTaskEffect.NavigateBack) {
                    navController.popBackStack()
                }
            }
        }
    }
} 