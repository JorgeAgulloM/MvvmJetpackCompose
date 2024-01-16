package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

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
        UserField(fieldName, "Nombre") { fieldName = it }
        UserField(fieldAge, "Edad") { fieldAge = it }
        Button(
            onClick = {
                viewModel.setUsers(
                    UserUi(
                        id = null,
                        name = fieldName,
                        age = fieldAge.toInt()
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

@Composable
private fun UserField(fieldName: String, label: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = fieldName,
        onValueChange = { onTextChange(it) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        enabled = true,
        label = { Text(text = label) },
        placeholder = { Text(text = label) },
        shape = MaterialTheme.shapes.large
    )
}
