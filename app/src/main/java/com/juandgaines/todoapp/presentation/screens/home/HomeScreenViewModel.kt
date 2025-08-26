package com.juandgaines.todoapp.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.juandgaines.TodoApplication
import com.juandgaines.todoapp.domain.TaskLocalDataSource
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenAction.OnDeleteAllTasks
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenAction.OnDeleteTask
import com.juandgaines.todoapp.presentation.screens.home.HomeScreenAction.OnToggleTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource
) : ViewModel(

) {


    var state by mutableStateOf(HomeDataState())
        private set

    private val eventChannel = Channel<HomeScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state = state.copy(date = LocalDate.now().let {
            DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy").format(it)
        })
        taskLocalDataSource.tasksFlow.onEach {
            val completedTasks =
                it.filter { task -> task.isCompleted }.sortedByDescending { task -> task.date }
            val pendingTasks =
                it.filter { task -> !task.isCompleted }.sortedByDescending { task -> task.date }

            state = state.copy(
                summary = pendingTasks.size.toString(),
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val dataSource = (this[APPLICATION_KEY] as TodoApplication).dataSource
                HomeScreenViewModel(
                    taskLocalDataSource = dataSource,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }


}