package com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateContactUseCase
import com.softyorch.mvvmjetpackcompose.ui.componens.contactFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IContactValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getContactUseCase: GetContactUseCase,
    private val updateContactUseCase: UpdateContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val validator: IContactValidator,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _stateDetails = MutableStateFlow<StateDetails>(StateDetails.Loading)
    val stateDetails: StateFlow<StateDetails> = _stateDetails

    private val _eventDetails = MutableStateFlow<EventDetails>(EventDetails.Read)
    val eventDetails: StateFlow<EventDetails> = _eventDetails

    private val _stateError = MutableStateFlow<StateError>(StateError.Working)
    val stateError: StateFlow<StateError> = _stateError

    private val _contactError = MutableStateFlow(ContactErrorModel())
    val contactError: StateFlow<ContactErrorModel> = _contactError

    fun getContact(contactId: UUID) {
        viewModelScope.launch(dispatcherIo) {
            getDataContact(contactId).catch { flowT ->
                if (_eventDetails.value != EventDetails.Delete)
                    _stateDetails.update { StateDetails.Error(flowT.message.toString()) }
            }.collect { contact ->
                _stateDetails.update { StateDetails.Success(contact) }
            }
        }
    }

    fun setContacts(contact: ContactUi): Boolean {
        when (_stateDetails.value) {
            is StateDetails.Success -> searchError(contact)
            else -> {}
        }
        return _stateError.value == StateError.Working
    }

    fun eventManager(newEvent: EventDetails) {
        when (val state = _stateDetails.value) {
            StateDetails.Loading -> {}
            is StateDetails.Success -> {
                when (newEvent) {
                    EventDetails.Read -> viewModelScope.launch(dispatcherIo) {
                        updateDataContact(state.contact)
                    }
                    EventDetails.Delete -> viewModelScope.launch(dispatcherIo) {
                        deleteDataContact(state.contact)
                    }
                    else -> {}
                }
            }

            is StateDetails.Error -> {}
        }
        _eventDetails.value = newEvent
    }

    fun onDataChange(contact: ContactUi) {
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> {
                searchFieldError(contact, state.contact)
                val contactEdit = state.contact.copy(
                    name = contact.name,
                    surName = contact.surName,
                    phoneNumber = contact.phoneNumber,
                    email = contact.email,
                    age = contact.age,
                    photoUri = contact.photoUri,
                    favorite = contact.favorite,
                    phoneBlocked = contact.phoneBlocked
                )
                _stateDetails.value = StateDetails.Success(contactEdit)
            }

            else -> {}
        }
    }

    fun setFavoriteBlockedOrNone(contact: ContactUi) {
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> {
                val contactEdit = state.contact.copy(
                    favorite = contact.favorite,
                    phoneBlocked = contact.phoneBlocked
                )
                _stateDetails.value = StateDetails.Success(contactEdit)
            }
            else -> {}
        }
    }

    private fun searchError(contact: ContactUi) {
        setErrors(validator.searchError(contact))
    }

    private fun searchFieldError(contact: ContactUi, oldDataContact: ContactUi) {
        setErrors(validator.searchFieldError(contact, oldDataContact))
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

    private suspend fun getDataContact(contactId: UUID): Flow<ContactUi> =
        getContactUseCase.invoke(contactId).map { it.toUi() }

    private suspend fun updateDataContact(contact: ContactUi) {
        updateContactUseCase.invoke(contact.toDomain())
    }

    private suspend fun deleteDataContact(contact: ContactUi) {
        deleteContactUseCase.invoke(contact.toDomain())
    }
}
