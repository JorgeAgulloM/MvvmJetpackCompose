package com.softyorch.mvvmjetpackcompose.domain

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import java.util.UUID

data class UserDomain(val id: UUID, val name: String, val age: Int) {
    companion object {
        fun UserDomain.toEntity(): UserEntity = UserEntity(id, name, age)
        fun UserEntity.toDomain(): UserDomain = UserDomain(id, name, age)
    }
}
