package com.softyorch.mvvmjetpackcompose.utils

import java.text.Normalizer

fun String.deleteAccents(): String {
    val normalizedText = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalizedText.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}
