package com.softyorch.mvvmjetpackcompose.ui.models

import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

data class ContactUi(
    val id: UUID,
    val name: String,
    val surName: String?,
    val phoneNumber: String,
    val email: String?,
    val age: String?,
    val photoUri: String?,
    val logo: String?,
    val logoColor: Long?,
    val lastCall: Long?,
    val typeCall: Int?,
    val favorite: Boolean?,
    val phoneBlocked: Boolean?
) {
    companion object {
        fun ContactUi.toDomain(): ContactDomain = ContactDomain(
            id = id,
            name = name,
            surName = surName,
            phoneNumber = phoneNumber,
            email = email,
            age = age?.toInt(),
            photoUri = photoUri,
            logo = logo,
            logoColor = logoColor,
            lastCall = lastCall,
            typeCall = typeCall,
            favorite = favorite,
            phoneBlocked = phoneBlocked
        )

        fun ContactDomain.toUi(): ContactUi = ContactUi(
            id = id,
            name = name,
            surName = surName,
            phoneNumber = phoneNumber,
            email = email,
            age = age.toString(),
            photoUri = photoUri,
            logo = logo,
            logoColor = logoColor,
            lastCall = lastCall,
            typeCall = typeCall,
            favorite = favorite,
            phoneBlocked = phoneBlocked
        )

        fun emptyContact(): ContactUi = ContactUi(
            id = UUID.randomUUID(),
            name = EMPTY_STRING,
            surName = null,
            phoneNumber = EMPTY_STRING,
            email = null,
            age = null,
            photoUri = null,
            logo = null,
            logoColor = null,
            lastCall = null,
            typeCall = null,
            favorite = null,
            phoneBlocked = null
        )
    }
}
