package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.DeleteUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.UpdateUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
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
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _stateDetails = MutableStateFlow<StateDetails>(StateDetails.Loading)
    val stateDetails: StateFlow<StateDetails> = _stateDetails

    private val _eventDetails = MutableStateFlow<EventDetails>(EventDetails.Read)
    val eventDetails: StateFlow<EventDetails> = _eventDetails

    private val _stateError = MutableStateFlow<StateError>(StateError.Working)
    val stateError: StateFlow<StateError> = _stateError

    private val _userError = MutableStateFlow(UserErrorModel())
    val userError: StateFlow<UserErrorModel> = _userError

    fun getUSer(userId: UUID) {
        viewModelScope.launch(dispatcherIo) {
            getDataUSer(userId).catch { flowT ->
                //if (_stateDetails.value != StateDetails.Deleted)
                if (_eventDetails.value != EventDetails.Delete)
                    _stateDetails.update { StateDetails.Error(flowT.message.toString()) }
            }.collect { user ->
                _stateDetails.update { StateDetails.Success(user) }
            }
        }
    }

    fun eventManager(newEvent: EventDetails) {
        when (val state = _stateDetails.value) {
            StateDetails.Loading -> {}
            is StateDetails.Success -> {
                when (newEvent) {
                    EventDetails.Read -> {
                        viewModelScope.launch(dispatcherIo) {
                            updateDataUser(state.user)
                        }
                    }
                    EventDetails.Edit -> {}
                    EventDetails.Delete -> {
                        viewModelScope.launch(dispatcherIo) {
                            deleteDataUser(state.user)
                        }
                    }
                }
            }
            is StateDetails.Error -> {}
        }
        _eventDetails.value = newEvent
    }

    fun onDataChange(user: UserUi) {
        searchError(user)
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> {
                val userEdit = state.user.copy(
                    name = user.name,
                    surName = user.surName,
                    phoneNumber = user.phoneNumber,
                    email = user.email,
                    age = user.age
                )
                _stateDetails.value = StateDetails.Success(userEdit)
            }

            else -> {}
        }
    }

    private fun searchError(user: UserUi) {
        val errorName = !isNameCorrect(user.name)
        val errorSurName = false
        val errorPhoneNumber = false
        val errorEmail = false
        val errorAge = user.age?.let { !isAgeCorrect(it) } ?: false

        _userError.update { UserErrorModel(errorName, errorSurName, errorPhoneNumber, errorEmail, errorAge) }
        _stateError.update { if (errorName || errorAge) StateError.Error else StateError.Working }

    }

    private fun isNameCorrect(name: String): Boolean {
        return name.length > 3 && !name.isDigitsOnly()
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

    private suspend fun deleteDataUser(user: UserUi) {
        deleteUserUseCase.invoke(user.toDomain())
    }
}
