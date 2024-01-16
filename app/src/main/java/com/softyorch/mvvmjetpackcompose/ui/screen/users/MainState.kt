package com.softyorch.mvvmjetpackcompose.ui.screen.users

sealed class MainState {
    data object Loading : MainState()
    data class Success(val users: List<UserUi>) : MainState()
    data class Error(val msg: String) : MainState()
}