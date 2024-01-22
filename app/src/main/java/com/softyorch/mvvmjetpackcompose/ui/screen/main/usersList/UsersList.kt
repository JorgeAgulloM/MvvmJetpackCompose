package com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material3.Icon
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
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAuto
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

@Composable
private fun User(user: UserUi, onClick: (String) -> Unit) {
    val shape = MaterialTheme.shapes.large

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { user.id?.let { onClick(it.toString()) } }
            .clip(shape = shape)
            .padding(horizontal = 64.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageUserAuto(userImage = null, user.name)
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "${user.name} ${user.surName}", style = MaterialTheme.typography.bodyLarge)
            if (user.lastCall != null) Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = when (user.typeCall) {
                    0 -> Icons.Default.CallMissed
                    1 -> Icons.Default.CallReceived
                    else -> Icons.Default.CallMade
                }
                val color = when (user.typeCall) {
                    0 -> MaterialTheme.colorScheme.error
                    1 -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.secondaryContainer
                }
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 2.dp),
                    tint = color
                )
                Text(
                    text = dateLongToString(user.lastCall),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

fun dateLongToString(timeInMillis: Long): String {
    val format = SimpleDateFormat("dd-MM - HH:mm", Locale.getDefault())
    val date = Date(timeInMillis)
    return format.format(date)
}
