package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val setUserUseCase: SetUserUseCase
): ViewModel() {

    fun setUsers(user: UserUi) {
        viewModelScope.launch(Dispatchers.IO) {
            setDataUser(user)
        }
    }

    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun setDataUser(user: UserUi) {
        setUserUseCase.invoke(user.toDomain())
    }
}