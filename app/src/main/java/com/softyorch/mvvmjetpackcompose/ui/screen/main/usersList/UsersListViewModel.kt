package com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetListUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
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
class UsersListViewModel @Inject constructor(
    private val getListUserUseCase: GetListUserUseCase,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersListState>(UsersListState.Loading)
    val uiState: StateFlow<UsersListState> = _uiState

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch(dispatcherIO) {
            getDataUsers()
                .catch { flowT ->
                    _uiState.update { UsersListState.Error(flowT.message ?: "Error") }
                }
                .flowOn(dispatcherIO)
                .collect { list ->
                    _uiState.update { UsersListState.Success(list) }
                }
        }
    }

    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun getDataUsers(): Flow<List<UserUi>> =
        getListUserUseCase.invoke().map { list -> list.map { it.toUi() } }
}
