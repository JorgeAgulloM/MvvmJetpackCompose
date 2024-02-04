package com.softyorch.mvvmjetpackcompose.utils.textUtilsWrapper

import android.text.TextUtils
import javax.inject.Inject

class TextUtilsWrapper @Inject constructor() {
    fun isDigitsOnly(str: CharSequence): Boolean = TextUtils.isDigitsOnly(str)
}
