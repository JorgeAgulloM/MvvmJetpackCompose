package com.softyorch.mvvmjetpackcompose.domain

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import java.util.UUID

data class UserDomain(
    val id: UUID?,
    val name: String,
    val surName: String?,
    val phoneNumber: String,
    val email: String?,
    val age: Int?,
    val lastCall: Long?,
    val typeCall: Int?

) {
    companion object {
        fun UserDomain.toEntity(): UserEntity =
            UserEntity(name = name, surName = surName, phoneNumber = phoneNumber, email = email, age = age, lastCall = lastCall, typeCall = typeCall)
        fun UserEntity.toDomain(): UserDomain = UserDomain(id, name, surName, phoneNumber, email, age, lastCall, typeCall)
    }
}
