package com.softyorch.mvvmjetpackcompose.ui.models

import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain
import java.util.UUID

data class UserUi(
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
        fun UserUi.toDomain(): UserDomain = UserDomain(
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

        fun UserDomain.toUi(): UserUi = UserUi(
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
    }
}
