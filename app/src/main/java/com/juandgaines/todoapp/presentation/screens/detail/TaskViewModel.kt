package com.juandgaines.todoapp.presentation.screens.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.juandgaines.TodoApplication
import com.juandgaines.todoapp.domain.Category
import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.domain.TaskLocalDataSource
import com.juandgaines.todoapp.navigation.TaskScreenDes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID


class TaskViewModel(
    saveStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource
) : ViewModel(

) {

    val taskData = saveStateHandle.toRoute<TaskScreenDes>()

    var state by mutableStateOf(TaskScreenState())
        private set

    private val eventChannel = Channel<TaskEvent>()

    val events = eventChannel.receiveAsFlow()

    private val canSaveTask = snapshotFlow { state.taskName.text.toString() }

    private var editedTask: Task? = null

    init {

        taskData.taskId?.let {
            viewModelScope.launch {
                val task = taskLocalDataSource.getTaskById(it)
                editedTask = task

                state = state.copy(
                    taskName = TextFieldState(task?.title ?: ""),
                    taskDescription = TextFieldState(task?.description ?: ""),
                    category = task?.category,
                    isTaskDone = task?.isCompleted ?: false
                )
            }
        }

        canSaveTask.onEach {
            state = state.copy(canSaveTask = it.isNotBlank())
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ActionTask) {
        viewModelScope.launch {
            when (action) {
                is ActionTask.ChangeTaskCategory -> {
                    state = state.copy(category = action.category)
                }

                is ActionTask.ChangeTaskDone -> {
                    state = state.copy(isTaskDone = action.isTaskDone)
                }

                ActionTask.SaveTask -> {

                    editedTask?.let {
                        taskLocalDataSource.updateTask(
                            it.copy(
                                title = state.taskName.text.toString(),
                                description = state.taskDescription.text.toString(),
                                category = state.category ?: Category.WORK,
                                isCompleted = state.isTaskDone
                            )
                        )

                    } ?: run {
                        val task = Task(
                            id = UUID.randomUUID().toString(),
                            title = state.taskName.text.toString(),
                            description = state.taskDescription.text.toString(),
                            category = state.category ?: Category.WORK,
                            isCompleted = state.isTaskDone
                        )
                        taskLocalDataSource.addTask(task)
                    }

                    eventChannel.send(TaskEvent.TaskCreated)

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
                TaskViewModel(
                    taskLocalDataSource = dataSource,
                    saveStateHandle = savedStateHandle
                )
            }
        }
    }
}