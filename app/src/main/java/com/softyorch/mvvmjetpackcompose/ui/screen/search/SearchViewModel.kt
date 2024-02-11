package com.softyorch.mvvmjetpackcompose.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetSearchContactsUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchContactsUseCase: GetSearchContactsUseCase,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    private val _stateFilter = MutableStateFlow<StateFilter>(StateFilter.Empty)
    val stateFilter: StateFlow<StateFilter> = _stateFilter

    private val _filter = MutableStateFlow(EMPTY_STRING)
    val filter: StateFlow<String> = _filter

    fun searchEvent(newFilter: String) {
        newFilter.lowercase().let { filterLow ->
            _filter.update { filterLow }
            searchContact(filterLow)
        }
    }

    private fun searchContact(filter: String) {
        viewModelScope.launch(dispatcherIO) {
            searContactsData(filter).collect { list ->
                if (list.isEmpty() || filter.isEmpty())
                    _stateFilter.update { StateFilter.Empty }
                else
                    _stateFilter.update { StateFilter.Find(list) }
            }
        }
    }

    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun searContactsData(filter: String): Flow<List<ContactUi>> =
        searchContactsUseCase.invoke(filter).map { list -> list.map { it.toUi() } }

}