package com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList

import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi

sealed class ContactsListState {
    data object Loading : ContactsListState()
    data class Success(val contacts: List<ContactUi>) : ContactsListState()
    data class Error(val msg: String) : ContactsListState()
}