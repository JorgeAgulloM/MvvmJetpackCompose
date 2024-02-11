package com.softyorch.mvvmjetpackcompose.ui.screen.main.newContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetContactUseCase
import com.softyorch.mvvmjetpackcompose.ui.componens.contactFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.emptyContact
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IContactValidator
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import com.softyorch.mvvmjetpackcompose.utils.colorList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateContactViewModel @Inject constructor(
    private val setContactUseCase: SetContactUseCase,
    private val validator: IContactValidator,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _contact = MutableStateFlow(emptyContact())
    val contact: StateFlow<ContactUi> = _contact

    private val _stateError = MutableStateFlow<StateError>(StateError.Working)
    val stateErrors: StateFlow<StateError> = _stateError

    private val _contactError = MutableStateFlow(ContactErrorModel())
    val contactError: StateFlow<ContactErrorModel> = _contactError

    /*init {
        viewModelScope.launch {
            generate().forEach { contact ->
                setContactUseCase.invoke(contact.toDomain())
            }
        }
    }*/

    fun setContacts(): Boolean {
        searchError(_contact.value)
        val isValid = _stateError.value == StateError.Working
        if (isValid)
            viewModelScope.launch(dispatcherIO) {
                setDataContact(_contact.value)
            }
        return isValid
    }

    fun onDataChange(contact: ContactUi) {
        searchFieldError(contact)
        val contactTransform = if (contact.logo == null)
            createdLogo(contact)
        else
            contact
        _contact.update { contactTransform }
    }

    private fun createdLogo(contact: ContactUi): ContactUi {
        val logo = if (contact.name.isNotEmpty()) contact.name[0].toString() else EMPTY_STRING
        val logoColor = contact.logoColor ?: colorList.random()

        return contact.copy(logo = logo, logoColor = logoColor)
    }

    private fun searchError(contact: ContactUi) {
        setErrors(validator.searchError(contact))
    }

    private fun searchFieldError(contact: ContactUi) {
        setErrors(validator.searchFieldError(contact, _contact.value))
    }

    private fun setErrors(contactError: ContactErrorModel) {
        _contactError.update { contactError }
        contactError.apply {
            _stateError.update {
                if (name || surName || phoneNumber || email || age)
                    StateError.Error
                else StateError.Working
            }
        }
    }

//##########################################################################
//################################ UseCases ################################
//##########################################################################

    private suspend fun setDataContact(contact: ContactUi) {
        setContactUseCase.invoke(contact.toDomain())
    }
}