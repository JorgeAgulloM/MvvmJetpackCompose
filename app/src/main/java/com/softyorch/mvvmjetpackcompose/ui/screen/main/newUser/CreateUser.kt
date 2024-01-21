package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.dataField.DataField
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

@Composable
fun CreateUser(
    viewModel: CreateUserViewModel = hiltViewModel<CreateUserViewModel>()
) {

    val user: UserUi by viewModel.user.collectAsStateWithLifecycle()
    val stateError: StateError by viewModel.stateErrors.collectAsStateWithLifecycle()
    val userErrors: UserErrorModel by viewModel.userError.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        DataField(
            label = "Nombre",
            text = user.name,
            error = userErrors.name,
            supportingText = "Debe contener al menos 4 caracteres",
            leadingIcon = Icons.Default.Person
        ) { name -> viewModel.onDataChange(user.copy(name = name)) }
        DataField(
            label = "Apellido",
            text = user.surName ?: EMPTY_STRING,
            error = userErrors.surName,
            supportingText = "Debe contener al menos 4 caracteres",
            leadingIcon = Icons.Default.Person
        ) { surName -> viewModel.onDataChange(user.copy(surName = surName)) }
        DataField(
            label = "Teléfono",
            text = user.phoneNumber,
            error = userErrors.phoneNumber,
            supportingText = "Solo puede contener número",
            leadingIcon = Icons.Default.Person
        ) { phoneNumber -> viewModel.onDataChange(user.copy(phoneNumber = phoneNumber)) }
        DataField(
            label = "Email",
            text = user.email ?: EMPTY_STRING,
            error = userErrors.email,
            supportingText = "El email no es correcto",
            leadingIcon = Icons.Default.Person
        ) { email -> viewModel.onDataChange(user.copy(email = email)) }
        DataField(
            label = "Edad",
            text = user.age ?: EMPTY_STRING,
            error = userErrors.age,
            supportingText = "Edad no puede estar vacío",
            leadingIcon = Icons.Default.CalendarMonth
        ) { age -> viewModel.onDataChange(user.copy(age = age)) }
        Button(
            onClick = {
                viewModel.setUsers()
                focusManager.clearFocus()
            },
            modifier = Modifier.padding(horizontal = 16.dp),
            enabled = stateError == StateError.Working
        ) {
            Text(text = "Crear usuario")
        }
    }
}
