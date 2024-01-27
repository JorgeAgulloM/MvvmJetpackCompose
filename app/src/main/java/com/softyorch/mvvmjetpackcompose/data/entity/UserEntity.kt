package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: UUID,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sur_name") val surName: String?,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "photo") val photoUri: String?,
    @ColumnInfo(name = "logo") val logo: String?,
    @ColumnInfo(name = "logo_color") val logoColor: Long?,
    @ColumnInfo(name = "last_call") val lastCall: Long?,
    @ColumnInfo(name = "type_call") val typeCall: Int?,
    @ColumnInfo(name = "favorite") val favorite: Boolean?,
    @ColumnInfo(name = "phone_blocked") val phoneBlocked: Boolean?
)
