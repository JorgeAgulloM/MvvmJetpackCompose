package com.softyorch.mvvmjetpackcompose.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.text.Normalizer

fun String.deleteAccents(): String {
    val normalizedText = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalizedText.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}

fun Context.getBitmapFromUriString(image: String?): ImageBitmap? = image?.let { uriString ->
    val uri = Uri.parse(uriString)
    try {
        val inputStream = this.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
