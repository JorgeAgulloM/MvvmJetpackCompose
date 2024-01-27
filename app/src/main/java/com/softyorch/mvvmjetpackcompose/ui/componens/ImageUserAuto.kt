package com.softyorch.mvvmjetpackcompose.ui.componens

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING

@Composable
fun ImageUserAuto(
    userImage: String?,
    userLogo: String?,
    userLogoColor: Long?,
    size: Dp = 40.dp
) {
    val context = LocalContext.current

    val image: ImageBitmap? = userImage?.let { uriString ->
        val uri = Uri.parse(uriString)
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    Log.i("MYAPP", "ImageUserAuto - Image: $image")
    if (image == null) {
        val logo = if (!userLogo.isNullOrEmpty()) userLogo else EMPTY_STRING
        val color =
            if (userLogoColor != null) Color(userLogoColor.toInt()) else MaterialTheme.colorScheme.background

        Box(
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
    } else Image(
        bitmap = image,
        contentDescription = "User Image",
        modifier = Modifier.size(size),
        contentScale = ContentScale.Crop
    )
}
