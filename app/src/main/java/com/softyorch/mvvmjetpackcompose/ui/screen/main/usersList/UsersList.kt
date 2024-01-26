package com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.User
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UsersList(
    viewModel: UsersListViewModel = hiltViewModel<UsersListViewModel>(),
    onClick: (String) -> Unit
) {
    val uiState: UsersListState by viewModel.uiState.collectAsStateWithLifecycle(UsersListState.Loading)
    val lazyState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = lazyState
    ) {
        when (val state = uiState) {
            UsersListState.Loading -> item { Text(text = "Cargando") }
            is UsersListState.Success -> {
                val orderList = state.users.sortedBy { it.name }
                val userMap: Map<String, List<UserUi>> =
                    orderList.groupBy { it.name[0].toString() }

                userMap.forEach { (name, list) ->
                    stickyHeader {
                        Text(
                            text = name[0].toString(),
                            modifier = Modifier.padding(start = 24.dp, top = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    items(list) { user ->
                        User(user) { id ->
                            val route = NavigationRoutes.UserDetailScreen.createRoute(id)
                            onClick(route)
                        }
                    }
                }
            }

            is UsersListState.Error -> item {
                Text(text = "Error ${(uiState as UsersListState.Error).msg}")
            }
        }
    }
}
