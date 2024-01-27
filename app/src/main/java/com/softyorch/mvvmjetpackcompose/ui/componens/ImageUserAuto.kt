package com.softyorch.mvvmjetpackcompose.ui.componens

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import kotlin.random.Random

@Composable
fun ImageUserAuto(
    userImage: String? = null,
    userLogo: String?,
    userLogoColor: Long?,
    size: Dp = 40.dp
) {
    val logo = if (!userLogo.isNullOrEmpty()) userLogo else EMPTY_STRING
    val color =
        if (userLogoColor != null) Color(userLogoColor.toInt()) else MaterialTheme.colorScheme.background

    if (userImage == null) Box(
        modifier = Modifier.size(size)
            .background(color = color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = logo,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.background,
                fontSize = (size.value / 2).sp
            )
        )
    }
    //else Image(painter = userImage, contentDescription = "User Image")
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
