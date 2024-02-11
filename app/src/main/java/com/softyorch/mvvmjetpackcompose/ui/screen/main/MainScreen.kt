package com.softyorch.mvvmjetpackcompose.ui.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes
import com.softyorch.mvvmjetpackcompose.ui.screen.main.newContact.CreateContact
import com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList.ContactsList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()
    val bottomSheetStat = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetStat)

    Scaffold(
        floatingActionButton = {
            if (!bottomSheetScaffoldState.bottomSheetState.isVisible) FloatingActionButton(
                onClick = { scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { sPd ->
        BottomSheetScaffold(
            sheetContent = {
                CreateContact {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.hide()
                    }
                }
            },
            sheetShadowElevation = 8.dp,
            sheetPeekHeight = 0.dp,
            sheetSwipeEnabled = true,
            scaffoldState = bottomSheetScaffoldState,
            modifier = Modifier.padding(sPd)
        ) { bssPd ->
            Column(
                modifier = Modifier.fillMaxSize().padding(bssPd),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SearchButton { navController.navigate(route = NavigationRoutes.Routes.SEARCH.name)}
                ContactsList { route -> navController.navigate(route = route) }
            }
        }
    }
}

@Composable
private fun SearchButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(0.dp, Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(text = "Buscar contactos", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
