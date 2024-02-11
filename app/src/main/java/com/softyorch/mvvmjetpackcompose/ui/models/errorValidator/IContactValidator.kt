package com.softyorch.mvvmjetpackcompose.ui.models.errorValidator

import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi

interface IContactValidator {
    fun searchError(contact: ContactUi): ContactErrorModel
    fun searchFieldError(contact: ContactUi, oldDataContact: ContactUi): ContactErrorModel
}
