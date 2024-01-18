package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

sealed class StateError {
    data object Working: StateError()
    data object Error: StateError()
}