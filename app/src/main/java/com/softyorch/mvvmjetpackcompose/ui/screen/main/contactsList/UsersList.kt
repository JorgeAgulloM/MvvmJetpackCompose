package com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList

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
import com.softyorch.mvvmjetpackcompose.ui.componens.Contact
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsList(
    viewModel: ContactsListViewModel = hiltViewModel<ContactsListViewModel>(),
    onClick: (String) -> Unit
) {
    val uiState: ContactsListState by viewModel.uiState.collectAsStateWithLifecycle(ContactsListState.Loading)
    val lazyState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = lazyState
    ) {
        when (val state = uiState) {
            ContactsListState.Loading -> item { Text(text = "Cargando") }
            is ContactsListState.Success -> {
                val orderList = state.contacts.sortedBy { it.name }
                val contactMap: Map<String, List<ContactUi>> =
                    orderList.groupBy { it.name[0].toString() }

                contactMap.forEach { (name, list) ->
                    stickyHeader {
                        Text(
                            text = name[0].toString(),
                            modifier = Modifier.padding(start = 24.dp, top = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    items(list) { contact ->
                        Contact(contact) { id ->
                            val route = NavigationRoutes.ContactDetailScreen.createRoute(id)
                            onClick(route)
                        }
                    }
                }
            }

            is ContactsListState.Error -> item {
                Text(text = "Error ${(uiState as ContactsListState.Error).msg}")
            }
        }
    }
}
