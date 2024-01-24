package com.softyorch.mvvmjetpackcompose.ui.componens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun ImageUserAuto(
    userImage: Painter? = null,
    userName: String,
    size: Dp = 40.dp
) {
    if (userImage == null) Box(
        modifier = Modifier.size(size).background(color = randomColor(), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = userName[0].toString(),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.background,
                fontSize = (size.value / 2).sp
            )
        )
    }
    else Image(painter = userImage, contentDescription = "User Image")
}

@Composable
fun randomColor(): Color {
    MaterialTheme.colorScheme.apply {
        val colorList = listOf(
            primaryContainer,
            secondaryContainer,
            tertiaryContainer
        )
        val random = Random.nextInt(colorList.size)
        return colorList[random]
    }
}
