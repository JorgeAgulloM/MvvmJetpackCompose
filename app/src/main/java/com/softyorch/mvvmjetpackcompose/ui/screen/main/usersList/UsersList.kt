package com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi

@Composable
fun UsersList(
    viewModel: UsersListViewModel = hiltViewModel<UsersListViewModel>(),
    onClick: (String) -> Unit
) {
    val uiState: UsersListState by viewModel.uiState.collectAsStateWithLifecycle(UsersListState.Loading)

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (uiState) {
            UsersListState.Loading -> item { Text(text = "Cargando") }
            is UsersListState.Success -> {
                items((uiState as UsersListState.Success).users) { user ->
                    User(user) { id ->
                        val route = NavigationRoutes.UserDetailScreen.createRoute(id)
                        onClick(route)
                    }
                }
            }

            is UsersListState.Error -> item {
                Text(text = "Error ${(uiState as UsersListState.Error).msg}")
            }
        }
    }
}

@Composable
private fun User(user: UserUi, onClick: (String) -> Unit) {
    val shape = MaterialTheme.shapes.large

    Column(
        modifier = Modifier.fillMaxWidth().clickable {
            user.id?.let { onClick(it.toString()) }
        }.clip(shape = shape)
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = shape
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        UserField("Nombre", user.name)
        UserField("Edad", user.age.toString())
    }
}

@Composable
private fun UserField(textField: String, textData: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$textField: ")
        Text(text = textData)
    }
}
