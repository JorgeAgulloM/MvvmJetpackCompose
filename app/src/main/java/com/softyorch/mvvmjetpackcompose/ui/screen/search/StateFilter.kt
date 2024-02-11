package com.softyorch.mvvmjetpackcompose.ui.screen.search

import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi

sealed class StateFilter {
    data class Find(val contacts: List<ContactUi>): StateFilter()
    data object Empty: StateFilter()
}
