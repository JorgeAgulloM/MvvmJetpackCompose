package com.softyorch.mvvmjetpackcompose.ui.componens.userFields

sealed class StateError {
    data object Working: StateError()
    data object Error: StateError()
}