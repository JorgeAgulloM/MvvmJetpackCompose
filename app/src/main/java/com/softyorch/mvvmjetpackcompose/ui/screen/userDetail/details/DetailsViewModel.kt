package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.UpdateUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _stateDetails = MutableStateFlow<StateDetails>(StateDetails.Loading)
    val stateDetails: StateFlow<StateDetails> = _stateDetails

    private val _eventDetails = MutableStateFlow<EventDetails>(EventDetails.Read)
    val eventDetails: StateFlow<EventDetails> = _eventDetails

    private val _stateError = MutableStateFlow<StateError>(StateError.Working)
    val stateError: StateFlow<StateError> = _stateError

    private val _userError = MutableStateFlow(UserErrorModel(name = false, age = false))
    val userError: StateFlow<UserErrorModel> = _userError

    fun getUSer(userId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            getDataUSer(userId).catch { flowT ->
                _stateDetails.update { StateDetails.Error(flowT.message.toString()) }
            }.collect { user ->
                _stateDetails.update { StateDetails.Success(user) }
            }
        }
    }

    fun eventManager(newEvent: EventDetails) {
        when (newEvent) {
            EventDetails.Read -> {
                if (_eventDetails.value == EventDetails.Edit) {
                    when (val state = _stateDetails.value) {
                        is StateDetails.Success -> viewModelScope.launch(Dispatchers.IO) {
                            updateDataUser(state.user)
                        }

                        else -> {}
                    }
                }
            }

            EventDetails.Edit -> {}
            EventDetails.Delete -> {}
        }
        _eventDetails.value = newEvent
    }

    fun onDataChange(name: String, age: String) {
        searchError(name, age)
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> {
                val userEdit = state.user.copy(
                    name = name,
                    age = age
                )
                _stateDetails.value = StateDetails.Success(userEdit)
            }

            else -> {}
        }
    }

    private fun searchError(name: String, age: String) {
        val errorName = !isNameCorrect(name)
        val errorAge = !isAgeCorrect(age)

        _userError.update { UserErrorModel(errorName, errorAge) }
        _stateError.update { if (errorName || errorAge) StateError.Error else StateError.Working }

    }

    private fun isNameCorrect(name: String): Boolean {
        return name.length < 4 || name.isDigitsOnly()
    }

    private fun isAgeCorrect(age: String): Boolean {
        return try {
            if (age.isEmpty()) return false
            if (!age.isDigitsOnly()) return false
            if (age[0].toString() == "0") return false
            val intAge = age.toInt()
            intAge in 1..110
        } catch (e: Exception) {
            false
        }
    }

    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun getDataUSer(userId: UUID): Flow<UserUi> =
        getUserUseCase.invoke(userId).map { it.toUi() }

    private suspend fun updateDataUser(user: UserUi) {
        updateUserUseCase.invoke(user.toDomain())
    }

}
