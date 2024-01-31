package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.userFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.componens.userFields.FromContact
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

@Composable
fun CreateUser(
    viewModel: CreateUserViewModel = hiltViewModel<CreateUserViewModel>(),
    onFinishCreateUser: () -> Unit
) {

    val user: UserUi by viewModel.user.collectAsStateWithLifecycle()
    val stateError: StateError by viewModel.stateErrors.collectAsStateWithLifecycle()
    val userErrors: UserErrorModel by viewModel.userError.collectAsStateWithLifecycle()

    FromContact(
        user = user,
        userErrors = userErrors,
        stateError = stateError,
        onDataChange = viewModel::onDataChange
    ) {
        val isDataValid = viewModel.setUsers()
        if (isDataValid) onFinishCreateUser()
    }
}
