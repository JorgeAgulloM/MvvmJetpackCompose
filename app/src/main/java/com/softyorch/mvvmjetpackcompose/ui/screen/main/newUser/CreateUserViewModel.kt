package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val setUserUseCase: SetUserUseCase,
    private val validator: IUserValidator
) : ViewModel() {

    private val _user = MutableStateFlow(
        UserUi(
            id = UUID.randomUUID(),
            name = EMPTY_STRING,
            surName = null,
            phoneNumber = EMPTY_STRING,
            email = null,
            age = null,
            lastCall = null,
            typeCall = null,
            favorite = null,
            phoneBlocked = null
        )
    )
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

    fun setUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            setDataUser(_user.value)
        }
    }

    fun onDataChange(user: UserUi) {
        searchError(user)
        _user.update {
            user.copy(
                name = user.name,
                surName = user.surName,
                phoneNumber = user.phoneNumber,
                email = user.email,
                age = user.age
            )
        }
    }

    private fun searchError(user: UserUi) {
        val errorName = !validator.isNameCorrect(user.name, 3)
        val errorSurName = user.surName?.let {
            Log.i("MYAPP", "surname: ${user.surName}")
            if (it.isEmpty()) false else !validator.isNameCorrect(it, 3)
        } ?: false
        val errorPhoneNumber = !validator.isPhoneNumberCorrect(user.phoneNumber)
        val errorEmail =
            user.email?.let { if (it.isEmpty()) false else !validator.isEmailCorrect(it) } ?: false
        val errorAge =
            user.age?.let { if (it.isEmpty()) false else !validator.isAgeCorrect(it) } ?: false


        _userError.update {
            UserErrorModel(
                errorName,
                errorSurName,
                errorPhoneNumber,
                errorEmail,
                errorAge
            )
        }
        _stateError.update {
            if (errorName || errorSurName || errorPhoneNumber || errorEmail || errorAge)
                StateError.Error else StateError.Working
        }
    }

//##########################################################################
//################################ UseCases ################################
//##########################################################################

    private suspend fun setDataUser(user: UserUi) {
        setUserUseCase.invoke(user.toDomain())
    }
}