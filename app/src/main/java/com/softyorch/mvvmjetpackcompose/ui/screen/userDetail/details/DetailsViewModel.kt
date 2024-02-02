package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.componens.userFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
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
    private val validator: IUserValidator,
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
                if (_eventDetails.value != EventDetails.Delete)
                    _stateDetails.update { StateDetails.Error(flowT.message.toString()) }
            }.collect { user ->
                _stateDetails.update { StateDetails.Success(user) }
            }
        }
    }

    fun setUsers(user: UserUi): Boolean {
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> searchError(user, state.user)
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
                        updateDataUser(state.user)
                    }
                    EventDetails.Edit -> {}
                    EventDetails.Delete -> viewModelScope.launch(dispatcherIo) {
                        deleteDataUser(state.user)
                    }
                }
            }

            is StateDetails.Error -> {}
        }
        _eventDetails.value = newEvent
    }

    fun onDataChange(user: UserUi) {
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> {
                searchFieldError(user, state.user)
                val userEdit = state.user.copy(
                    name = user.name,
                    surName = user.surName,
                    phoneNumber = user.phoneNumber,
                    email = user.email,
                    age = user.age,
                    photoUri = user.photoUri,
                    favorite = user.favorite,
                    phoneBlocked = user.phoneBlocked
                )
                _stateDetails.value = StateDetails.Success(userEdit)
            }

            else -> {}
        }
    }

    fun setFavoriteBlockedOrNone(user: UserUi) {
        when (val state = _stateDetails.value) {
            is StateDetails.Success -> {
                val userEdit = state.user.copy(
                    favorite = user.favorite,
                    phoneBlocked = user.phoneBlocked
                )
                _stateDetails.value = StateDetails.Success(userEdit)
            }
            else -> {}
        }
    }

    private fun searchError(user: UserUi, oldDataUser: UserUi) {
        setErrors(validator.searchError(user, oldDataUser))
    }

    private fun searchFieldError(user: UserUi, oldDataUser: UserUi) {
        setErrors(validator.searFieldError(user, oldDataUser))
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

    private suspend fun getDataUSer(userId: UUID): Flow<UserUi> =
        getUserUseCase.invoke(userId).map { it.toUi() }

    private suspend fun updateDataUser(user: UserUi) {
        updateUserUseCase.invoke(user.toDomain())
    }

    private suspend fun deleteDataUser(user: UserUi) {
        deleteUserUseCase.invoke(user.toDomain())
    }
}
