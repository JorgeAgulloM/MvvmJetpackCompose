package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

sealed class StateError {
    data object Working: StateError()
    data object Error: StateError()
}