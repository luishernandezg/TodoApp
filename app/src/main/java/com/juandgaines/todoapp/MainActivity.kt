package com.juandgaines.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juandgaines.todoapp.data.FakeTaskLocalDataSource
import com.juandgaines.todoapp.domain.Task
import com.juandgaines.todoapp.ui.theme.TodoAppTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoAppTheme() {
                var text by remember { mutableStateOf("") }
                val fakeLocalDataSource = FakeTaskLocalDataSource
                LaunchedEffect(true) {
                    fakeLocalDataSource.taskFlow.collect {
                        text = it.toString()
                    }
                }
                LaunchedEffect(true) {
                    fakeLocalDataSource.addTask(
                        Task(
                            id = UUID.randomUUID().toString(),
                            title = "Task 01",
                            description = "Description 01",
                        )
                    )
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun HelloWorldView() {
    var isShown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(56.dp)
    ) {
        MessageText(isShown)
        HelloWorldText {
            isShown = !isShown
        }
    }
}

@Composable
fun MessageText(isShown: Boolean) {
    if (isShown) {
        Text("This is a message")
    }
}

@Composable
fun HelloWorldText(onClick: () -> Unit) {
    Text(
        "Hello, world!",
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

