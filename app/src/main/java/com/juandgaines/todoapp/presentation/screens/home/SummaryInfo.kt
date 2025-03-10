package com.juandgaines.todoapp.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SummaryInfo(
    modifier: Modifier = Modifier,
    date: String = "March 9",
    taskSummary: String = "3 incomplete, 3 compelted",
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = date,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = taskSummary,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, name = "Summary Info Preview")
@Composable
fun SummaryInfoPreview() {
    MaterialTheme { SummaryInfo() }
}