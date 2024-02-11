package com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details

import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi

sealed class StateDetails {
    data object Loading : StateDetails()
    data class Success(val contact: ContactUi) : StateDetails()
    data class Error(val msg: String) : StateDetails()
}