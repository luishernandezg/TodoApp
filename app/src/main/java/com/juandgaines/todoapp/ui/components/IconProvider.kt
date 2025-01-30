package com.juandgaines.todoapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class IconProvider : PreviewParameterProvider<IconContainer> {
    override val values: Sequence<IconContainer>
        get() = sequenceOf(
            IconContainer(Icons.Default.Add),
            IconContainer(Icons.Default.MoreVert),
            IconContainer(Icons.Default.Star),
            IconContainer(Icons.Default.Search)
        )
}