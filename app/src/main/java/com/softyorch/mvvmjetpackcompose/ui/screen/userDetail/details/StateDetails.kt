package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

sealed class StateDetails {
    data object Loading : StateDetails()
    data class Success(val user: UserUi) : StateDetails()
    data class Error(val msg: String) : StateDetails()
}