package com.juandgaines.todoapp.presentation.screens.home

sealed class HomeScreenEvent {
    data object UpdateTask : HomeScreenEvent()
    data object DeleteAllTasks : HomeScreenEvent()
    data object DeleteTask : HomeScreenEvent()

}