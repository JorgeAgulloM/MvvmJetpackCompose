package com.softyorch.mvvmjetpackcompose.ui.models

import com.softyorch.mvvmjetpackcompose.domain.UserDomain
import java.util.UUID

data class UserUi(val id: UUID, val name: String, val age: String) {
    companion object {
        fun UserUi.toDomain(): UserDomain = UserDomain(id, name, age.toInt())
        fun UserDomain.toUi(): UserUi = UserUi(id, name, age.toString())
    }
}
