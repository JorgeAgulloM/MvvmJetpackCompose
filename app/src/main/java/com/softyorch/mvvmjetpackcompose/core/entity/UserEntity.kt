package com.softyorch.mvvmjetpackcompose.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(@PrimaryKey val id: Int, val name: String, val age: Int)
