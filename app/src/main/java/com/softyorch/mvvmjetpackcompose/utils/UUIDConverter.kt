package com.softyorch.mvvmjetpackcompose.utils

import androidx.room.TypeConverter
import java.util.UUID

@TypeConverter
fun UUID.ToString(): String = this.toString()

@TypeConverter
fun String.ToUUID(): UUID = UUID.fromString(this)
