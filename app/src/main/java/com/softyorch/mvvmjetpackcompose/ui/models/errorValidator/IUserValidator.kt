package com.softyorch.mvvmjetpackcompose.ui.models.errorValidator

import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

interface IUserValidator {
    fun searchError(user: UserUi, oldDataUser: UserUi): UserErrorModel
    fun searchFieldError(user: UserUi, oldDataUser: UserUi): UserErrorModel
}
