package com.juandgaines.todoapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juandgaines.todoapp.presentation.screens.detail.TaskScreenRoot
import com.juandgaines.todoapp.presentation.screens.detail.TaskViewModel
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenRoot
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenViewModel
import kotlinx.serialization.Serializable


@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeScreenDes
        ) {
            composable<HomeScreenDes> {
                val viewModel = viewModel<HomeScreenViewModel>(
                    factory = HomeScreenViewModel.Factory
                )
                HomeScreenRoot(
                    navigateToTaskScreen = {
                        navController.navigate(TaskScreenDes(it))

                    },
                    viewModel = viewModel
                )
            }

            composable<TaskScreenDes> {
                val viewModel = viewModel<TaskViewModel>(
                    factory = TaskViewModel.Factory
                )
                TaskScreenRoot(
                    navigateBack = {
                        navController.navigateUp()
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}


@Serializable
object HomeScreenDes

@Serializable
data class TaskScreenDes(
    var taskId: String? = null
)