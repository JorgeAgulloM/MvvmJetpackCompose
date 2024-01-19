package com.softyorch.mvvmjetpackcompose.ui.models

import com.softyorch.mvvmjetpackcompose.domain.UserDomain
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

data class UserUi(
    val id: UUID?,
    val name: String,
    val surName: String?,
    val phoneNumber: String,
    val email: String?,
    val age: String?
) {
    companion object {
        fun UserUi.toDomain(): UserDomain = UserDomain(
            id = id,
            name = name,
            surName = surName,
            phoneNumber = if (phoneNumber.isEmpty()) 0 else phoneNumber.toInt(),
            email = email,
            age = age?.toInt()
        )

        fun UserDomain.toUi(): UserUi = UserUi(
            id = id,
            name = name, surName = surName,
            phoneNumber = if (phoneNumber == 0) EMPTY_STRING else phoneNumber.toString(),
            email = email,
            age = age.toString()
        )
    }
}
