package com.softyorch.mvvmjetpackcompose.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel<MainViewModel>()) {

    val uiState: MainState by viewModel.uiState.collectAsStateWithLifecycle(MainState.Loading)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        InsertUser { viewModel.setUsers(it) }
        Text(
            text = "Lista de usuarios guardados",
            modifier = Modifier.padding(horizontal = (24 + 4).dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            when (uiState) {
                MainState.Loading -> item { Text(text = "Cargando") }
                is MainState.Success -> {
                    items((uiState as MainState.Success).users) { user ->
                        User(user)
                    }
                }

                is MainState.Error -> item {
                    Text(text = "Error ${(uiState as MainState.Error).msg}")
                }
            }
        }
    }
}

@Composable
fun InsertUser(onClick: (UserUi) -> Unit) {
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
                onClick(
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
fun UserField(fieldName: String, label: String, onTextChange: (String) -> Unit) {
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

@Composable
fun User(user: UserUi) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.large
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        UserField("Nombre", user.name)
        UserField("Edad", user.age.toString())
    }
}

@Composable
fun UserField(textField: String, textData: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$textField: ")
        Text(text = textData)
    }
}
