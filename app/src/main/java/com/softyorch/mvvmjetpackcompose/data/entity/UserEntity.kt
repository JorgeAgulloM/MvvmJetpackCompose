package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user")
data class UserEntity(@PrimaryKey val id: UUID? = UUID.randomUUID(), val name: String, val age: Int)
