package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.softyorch.mvvmjetpackcompose.ui.componens.dataField.DataField
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

@Composable
fun CreateUser(
    viewModel: CreateUserViewModel = hiltViewModel<CreateUserViewModel>()
) {
    var fieldName by remember { mutableStateOf("") }
    var fieldAge by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        DataField(
            label = "Nombre",
            text = fieldName,
            error = false,
            supportingText = EMPTY_STRING
        ) { fieldName = it }
        DataField(
            label = "Edad",
            text = fieldAge,
            error = false,
            supportingText = EMPTY_STRING
        ) { fieldAge = it }
        Button(
            onClick = {
                viewModel.setUsers(
                    UserUi(
                        id = UUID.randomUUID(),
                        name = fieldName,
                        surName = null,
                        phoneNumber = EMPTY_STRING,
                        email = null,
                        age = fieldAge
                    )
                )
                fieldName = ""
                fieldAge = ""
                focusManager.clearFocus()
            },
            modifier = Modifier.padding(4.dp)
        ) {
            Text(text = "Crear usuario")
        }
    }
}
