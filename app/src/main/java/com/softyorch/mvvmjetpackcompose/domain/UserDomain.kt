package com.softyorch.mvvmjetpackcompose.domain

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import java.util.UUID

data class UserDomain(
    val id: UUID?,
    val name: String,
    val surName: String?,
    val phoneNumber: Int,
    val email: String?,
    val age: Int?
) {
    companion object {
        fun UserDomain.toEntity(): UserEntity =
            UserEntity(name = name, surName = surName, phoneNumber = phoneNumber, email = email, age = age)
        fun UserEntity.toDomain(): UserDomain = UserDomain(id, name, surName, phoneNumber, email, age)
    }
}
