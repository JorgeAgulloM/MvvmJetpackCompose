package com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList

import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

sealed class UsersListState {
    data object Loading : UsersListState()
    data class Success(val users: List<UserUi>) : UsersListState()
    data class Error(val msg: String) : UsersListState()
}