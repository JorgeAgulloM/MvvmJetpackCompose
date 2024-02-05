package com.softyorch.mvvmjetpackcompose.utils

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import java.util.UUID

val userID: UUID = UUID.randomUUID()
val testContact = UserEntity(
    id = userID,
    name = "Test",
    surName = "User",
    phoneNumber = "1234567890",
    email = "test.user@example.com",
    age = 30,
    photoUri = null,
    logo = null,
    logoColor = null,
    lastCall = null,
    typeCall = null,
    favorite = false,
    phoneBlocked = false
)

val userID2: UUID = UUID.randomUUID()
val testContact2 = UserEntity(
    id = userID2,
    name = "Test",
    surName = "User",
    phoneNumber = "1234567890",
    email = "test.user@example.com",
    age = 30,
    photoUri = null,
    logo = null,
    logoColor = null,
    lastCall = null,
    typeCall = null,
    favorite = false,
    phoneBlocked = false
)