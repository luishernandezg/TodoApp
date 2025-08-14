package com.juandgaines.todoapp.presentation.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.todoapp.R
import com.juandgaines.todoapp.ui.theme.TodoAppTheme

@Composable
fun SummaryInfo(
    modifier: Modifier = Modifier,
    date: String = "March 9",
    taskSummary: String = "3 incomplete, 3 completed",
    completedTasks: Int,
    totalTasks: Int,
) {
    val angleRatio = remember {
        Animatable(0f)
    }
    LaunchedEffect(completedTasks, totalTasks) {
        if (totalTasks == 0) return@LaunchedEffect
        angleRatio.animateTo(
            targetValue = completedTasks / totalTasks.toFloat(),
            animationSpec = tween(durationMillis = 600)
        )

    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1.5f)
                .padding(16.dp)

        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.summary_info, taskSummary),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(1f)
                .weight(1f)
        ) {
            val colorBase = MaterialTheme.colorScheme.inversePrimary
            val progress = MaterialTheme.colorScheme.primary
            val strokeWidth = 16.dp

            Canvas(
                modifier = Modifier.aspectRatio(1f)
            ) {
                drawArc(
                    color = colorBase,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                if (completedTasks <= totalTasks) {
                    drawArc(
                        color = progress,
                        startAngle = 90f,
                        sweepAngle = 360f * angleRatio.value,
                        useCenter = false,
                        size = size,
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            Text(
                text = "${(completedTasks / totalTasks.toFloat()).times(100).toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, name = "Summary Info Preview")
@Composable
fun SummaryInfoPreview() {
    TodoAppTheme { SummaryInfo(completedTasks = 5, totalTasks = 10) }
}