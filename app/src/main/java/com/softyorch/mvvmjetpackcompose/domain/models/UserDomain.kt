package com.softyorch.mvvmjetpackcompose.domain.models

import androidx.compose.ui.graphics.Color
import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import java.util.UUID

data class UserDomain(
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
        fun UserDomain.toEntity(): UserEntity =
            UserEntity(
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

        fun UserEntity.toDomain(): UserDomain =
            UserDomain(
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
