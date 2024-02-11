package com.softyorch.mvvmjetpackcompose.utils

import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import java.util.UUID

val contactID: UUID = UUID.randomUUID()
val testContact = ContactEntity(
    id = contactID,
    name = "Test",
    surName = "Contact",
    phoneNumber = "1234567890",
    email = "test.contact@example.com",
    age = 30,
    photoUri = null,
    logo = null,
    logoColor = null,
    lastCall = null,
    typeCall = null,
    favorite = false,
    phoneBlocked = false
)