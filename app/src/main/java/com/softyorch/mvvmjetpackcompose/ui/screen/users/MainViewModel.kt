package com.softyorch.mvvmjetpackcompose.ui.screen.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.GetListUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.SetListUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.screen.users.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.screen.users.UserUi.Companion.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetListUserUseCase,
    private val setUserUseCase: SetListUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState

    init {
        getUsers()
    }

    fun setUsers(user: UserUi) {
        viewModelScope.launch(Dispatchers.IO) {
            val users = mutableListOf<UserUi>()

            when (_uiState.value) {
                is MainState.Success -> {
                    users.addAll((_uiState.value as MainState.Success).users)
                    users.add(user)
                }
                else -> users.add(user)
            }

            setDataUser(users)
        }
    }

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getDataUsers().collect { list ->
                _uiState.update { MainState.Success(list) }
            }
        }
    }

    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun getDataUsers(): Flow<List<UserUi>> {
        return getUserUseCase.invoke().map { list -> list.map { it.toUi()} }
    }

    private suspend fun setDataUser(users: List<UserUi>) {
        setUserUseCase.invoke(users.map { it.toDomain() })
    }
}