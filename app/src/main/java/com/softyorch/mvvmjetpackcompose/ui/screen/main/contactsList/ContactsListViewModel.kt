package com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactsUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContactsListState>(ContactsListState.Loading)
    val uiState: StateFlow<ContactsListState> = _uiState

    init {
        onCreate()
    }

    fun onCreate() {
        getContacts()
    }

    private fun getContacts() {
        viewModelScope.launch(dispatcherIO) {
            getDataContacts()
                .catch { flowT ->
                    _uiState.update { ContactsListState.Error(flowT.message ?: "Error") }
                }
                .flowOn(dispatcherIO)
                .collect { list ->
                    _uiState.update { ContactsListState.Success(list) }
                }
        }
    }

    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun getDataContacts(): Flow<List<ContactUi>> =
        getContactsUseCase.invoke().map { list -> list.map { it.toUi() } }
}
