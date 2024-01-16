package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.mvvmjetpackcompose.domain.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private val _stateDetails = MutableStateFlow<StateDetails>(StateDetails.Loading)
    val stateDetails: StateFlow<StateDetails> = _stateDetails

    fun getUSer(userId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateDetails.update { StateDetails.Success(getDataUSer(userId)) }
        }
    }


    //##########################################################################
    //################################ UseCases ################################
    //##########################################################################

    private suspend fun getDataUSer(userId: UUID): UserUi =
        getUserUseCase.invoke(userId).toUi()

}