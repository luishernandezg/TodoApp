package com.juandgaines.todoapp.data

import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.domain.TaskLocalDataSource
import com.juandgaines.todoapp.presentation.screens.home.providers.completedTask
import com.juandgaines.todoapp.presentation.screens.home.providers.pendingTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object FakeTaskLocalDataSource : TaskLocalDataSource {
    private val _tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    init {
        _tasksFlow.value = completedTask + pendingTask
    }

    override val tasksFlow: Flow<List<Task>>
        get() = _tasksFlow

    override suspend fun addTask(task: Task) {
        val tasks = _tasksFlow.value.toMutableList()
        tasks.add(task)
        delay(100)
        _tasksFlow.value = tasks
    }

    override suspend fun updateTask(task: Task) {
        val tasks = _tasksFlow.value.toMutableList()
        val taskIndex = tasks.indexOfFirst { it.id == task.id }
        if (taskIndex != -1) {
            tasks[taskIndex] = task
            delay(100)
            _tasksFlow.value = tasks
        }
    }

    override suspend fun removeTask(task: Task) {
        val tasks = _tasksFlow.value.toMutableList()
        tasks.remove(task)
        delay(100)
        _tasksFlow.value = tasks
    }

    override suspend fun deleteAllTasks() {
        _tasksFlow.value = emptyList()
    }

    override suspend fun getTaskById(id: String): Task? {
        return _tasksFlow.value.firstOrNull { it.id == id }
    }
}