package com.juandgaines.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juandgaines.todoapp.presentation.screens.detail.TaskScreen
import com.juandgaines.todoapp.presentation.screens.detail.TaskScreenState
import com.juandgaines.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoAppTheme() {
//                HomeScreenRoot()
                TaskScreen(
                    state = TaskScreenState()
                )
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

