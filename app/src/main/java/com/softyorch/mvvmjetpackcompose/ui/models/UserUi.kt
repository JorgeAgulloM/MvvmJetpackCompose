package com.softyorch.mvvmjetpackcompose.ui.models

import com.softyorch.mvvmjetpackcompose.domain.UserDomain
import java.util.UUID

data class UserUi(
    val id: UUID,
    val name: String,
    val surName: String?,
    val phoneNumber: String,
    val email: String?,
    val age: String?,
    val lastCall: Long?,
    val typeCall: Int?,
    val favorite: Boolean?,
    val phoneBlocked: Boolean?
) {
    companion object {
        fun UserUi.toDomain(): UserDomain = UserDomain(id, name, surName, phoneNumber, email, age?.toInt(), lastCall, typeCall, favorite, phoneBlocked)

        fun UserDomain.toUi(): UserUi = UserUi(id, name, surName, phoneNumber, email, age.toString(), lastCall, typeCall, favorite, phoneBlocked)
    }
}
