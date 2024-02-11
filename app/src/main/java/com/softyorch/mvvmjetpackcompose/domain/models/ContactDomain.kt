package com.softyorch.mvvmjetpackcompose.domain.models

import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import java.util.UUID

data class ContactDomain(
    val id: UUID,
    val name: String,
    val surName: String?,
    val phoneNumber: String,
    val email: String?,
    val age: Int?,
    val photoUri: String?,
    val logo: String?,
    val logoColor: Long?,
    val lastCall: Long?,
    val typeCall: Int?,
    val favorite: Boolean?,
    val phoneBlocked: Boolean?

) {
    companion object {
        fun ContactDomain.toEntity(): ContactEntity =
            ContactEntity(
                id = id,
                name = name,
                surName = surName,
                phoneNumber = phoneNumber,
                email = email,
                age = age,
                photoUri = photoUri,
                logo = logo,
                logoColor = logoColor,
                lastCall = lastCall,
                typeCall = typeCall,
                favorite = favorite,
                phoneBlocked = phoneBlocked
            )

        fun ContactEntity.toDomain(): ContactDomain =
            ContactDomain(
                id = id,
                name = name,
                surName = surName,
                phoneNumber = phoneNumber,
                email = email,
                age = age,
                photoUri = photoUri,
                logo = logo,
                logoColor = logoColor,
                lastCall = lastCall,
                typeCall = typeCall,
                favorite = favorite,
                phoneBlocked = phoneBlocked
            )
    }
}
