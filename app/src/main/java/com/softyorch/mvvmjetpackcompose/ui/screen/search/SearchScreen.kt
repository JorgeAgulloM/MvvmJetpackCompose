package com.softyorch.mvvmjetpackcompose.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.R
import com.softyorch.mvvmjetpackcompose.ui.componens.Contact
import com.softyorch.mvvmjetpackcompose.ui.componens.DataView
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onDetailNav: (String) -> Unit,
    onBack: () -> Unit
) {

    val stateFilter: StateFilter by viewModel.stateFilter.collectAsStateWithLifecycle()
    val filter: String by viewModel.filter.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val fieldColor = MaterialTheme.colorScheme.surfaceVariant
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = filter,
            placeholder = { Text(text = stringResource(R.string.search_screen_search_contacts)) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp).focusRequester(focusRequester),
            onValueChange = { newFilter -> viewModel.searchEvent(newFilter) },
            leadingIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.search_screen_content_desc_go_back)
                    )
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = fieldColor,
                unfocusedContainerColor = fieldColor,
                focusedBorderColor = fieldColor,
                focusedContainerColor = fieldColor
            )
        )
        when (val state = stateFilter) {
            is StateFilter.Find -> {
                LazyColumn(
                    state = lazyListState
                ) {
                    items(state.contacts) { contact ->
                        Contact(contact, DataView.NumberAndEmail) { id ->
                            val route = NavigationRoutes.ContactDetailScreen.createRoute(id)
                            onDetailNav(route)
                        }
                    }
                }
            }

            StateFilter.Empty -> Text(
                text = stringResource(R.string.search_screen_filter_options),
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}
