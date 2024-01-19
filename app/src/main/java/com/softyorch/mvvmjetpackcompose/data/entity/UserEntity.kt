package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sur_name") val surName: String?,
    @ColumnInfo(name = "phone_number") val phoneNumber: Int,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "age") val age: Int?
)
