package com.softyorch.mvvmjetpackcompose.ui.screen.search

import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

sealed class StateFilter {
    data class Find(val users: List<UserUi>): StateFilter()
    data object Empty: StateFilter()
}
