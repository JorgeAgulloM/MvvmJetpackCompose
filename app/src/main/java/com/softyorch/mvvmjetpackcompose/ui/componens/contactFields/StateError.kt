package com.softyorch.mvvmjetpackcompose.ui.componens.contactFields

sealed class StateError {
    data object Working: StateError()
    data object Error: StateError()
}