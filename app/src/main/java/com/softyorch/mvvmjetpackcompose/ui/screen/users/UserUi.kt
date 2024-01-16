package com.softyorch.mvvmjetpackcompose.ui.screen.users

import com.softyorch.mvvmjetpackcompose.domain.UserDomain
import java.util.UUID

data class UserUi(val id: UUID?, val name: String, val age: Int) {
    companion object {
        fun UserUi.toDomain(): UserDomain = UserDomain(id, name, age)
        fun UserDomain.toUi(): UserUi = UserUi(id, name, age)
    }
}
