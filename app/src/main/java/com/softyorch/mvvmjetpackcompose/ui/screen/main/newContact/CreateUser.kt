package com.softyorch.mvvmjetpackcompose.ui.screen.main.newContact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.contactFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.componens.contactFields.FromContact
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi

@Composable
fun CreateContact(
    viewModel: CreateContactViewModel = hiltViewModel<CreateContactViewModel>(),
    onFinishCreateContact: () -> Unit
) {

    val contact: ContactUi by viewModel.contact.collectAsStateWithLifecycle()
    val stateError: StateError by viewModel.stateErrors.collectAsStateWithLifecycle()
    val contactErrors: ContactErrorModel by viewModel.contactError.collectAsStateWithLifecycle()

    FromContact(
        contact = contact,
        contactErrors = contactErrors,
        stateError = stateError,
        onDataChange = viewModel::onDataChange
    ) {
        val isDataValid = viewModel.setContacts()
        if (isDataValid) onFinishCreateContact()
    }
}
