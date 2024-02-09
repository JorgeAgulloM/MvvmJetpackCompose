package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.componens.userFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.emptyUser
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
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
class CreateUserViewModel @Inject constructor(
    private val setUserUseCase: SetUserUseCase,
    private val validator: IUserValidator,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _user = MutableStateFlow(emptyUser())
    val user: StateFlow<UserUi> = _user

    private val _stateError = MutableStateFlow<StateError>(StateError.Working)
    val stateErrors: StateFlow<StateError> = _stateError

    private val _userError = MutableStateFlow(UserErrorModel())
    val userError: StateFlow<UserErrorModel> = _userError

    /*init {
        viewModelScope.launch {
            generate().forEach { user ->
                setUserUseCase.invoke(user.toDomain())
            }
        }
    }*/

    fun setUsers(): Boolean {
        searchError(_user.value)
        val isValid = _stateError.value == StateError.Working
        if (isValid)
            viewModelScope.launch(dispatcherIO) {
                setDataUser(_user.value)
            }
        return isValid
    }

    fun onDataChange(user: UserUi) {
        searchFieldError(user)
        val userTransform = if (user.logo == null)
            createdLogo(user)
        else
            user
        _user.update { userTransform }
    }

    private fun createdLogo(user: UserUi): UserUi {
        val logo = if (user.name.isNotEmpty()) user.name[0].toString() else EMPTY_STRING
        val logoColor = user.logoColor ?: colorList.random()

        return user.copy(logo = logo, logoColor = logoColor)
    }

    private fun searchError(user: UserUi) {
        setErrors(validator.searchError(user))
    }

    private fun searchFieldError(user: UserUi) {
        setErrors(validator.searchFieldError(user, _user.value))
    }

    private fun setErrors(userError: UserErrorModel) {
        _userError.update { userError }
        userError.apply {
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

    private suspend fun setDataUser(user: UserUi) {
        setUserUseCase.invoke(user.toDomain())
    }
}