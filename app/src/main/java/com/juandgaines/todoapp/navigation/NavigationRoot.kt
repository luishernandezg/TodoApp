package com.juandgaines.todoapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juandgaines.todoapp.presentation.screens.detail.TaskScreenRoot
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenRoot


@Composable
fun NavigationRoot(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize())
    {
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreenDes.route,
        )
        {
            composable(route = Screen.HomeScreenDes.route) {
                HomeScreenRoot(
                    navigateToTaskScreen = {
                        navController.navigate(Screen.TaskScreenDes.route)
                    }
                )
            }
            composable(route = Screen.TaskScreenDes.route) {
                TaskScreenRoot(
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}


enum class Destinations { HOME, TASK, }
sealed class Screen(val route: String) {
    object HomeScreenDes : Screen(Destinations.HOME.name)
    object TaskScreenDes : Screen(Destinations.TASK.name)
}