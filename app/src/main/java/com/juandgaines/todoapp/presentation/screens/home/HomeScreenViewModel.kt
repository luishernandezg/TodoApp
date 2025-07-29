package com.juandgaines.todoapp.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.todoapp.data.FakeTaskLocalDataSource
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenAction.OnDeleteAllTasks
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenAction.OnDeleteTask
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenAction.OnToggleTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val taskLocalDataSource = FakeTaskLocalDataSource

    var state by mutableStateOf(HomeDataState())
        private set

    private val eventChannel = Channel<HomeScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        taskLocalDataSource.tasksFlow.onEach {
            val completedTasks = it.filter { task -> task.isCompleted }
            val pendingTasks = it.filter { task -> !task.isCompleted }

            state = HomeDataState(
                date = "March 9, 2024",
                summary = "You have ${pendingTasks.size} pending tasks & ${completedTasks.size} completed tasks",
                completedTask = completedTasks,
                pendingTask = pendingTasks,
            )
        }.launchIn(viewModelScope)

    }


    fun onAction(action: HomeScreenAction) {
        viewModelScope.launch {
            when (action) {
                OnDeleteAllTasks -> {
                    taskLocalDataSource.deleteAllTasks()
                    eventChannel.send(HomeScreenEvent.DeleteAllTasks)
                }

                is OnDeleteTask -> {
                    taskLocalDataSource.removeTask(action.task)
                    eventChannel.send(HomeScreenEvent.DeleteTask)
                }

                is OnToggleTask -> {
                    val updatedTask = action.task.copy(isCompleted = !action.task.isCompleted)
                    taskLocalDataSource.updateTask(updatedTask)
//                    eventChannel.send(HomeScreenEvent.UpdateTask)
                }

                else -> Unit
            }
        }

    }


}