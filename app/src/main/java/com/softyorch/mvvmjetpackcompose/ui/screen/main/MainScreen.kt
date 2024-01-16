package com.softyorch.mvvmjetpackcompose.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser.CreateUser
import com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList.UsersList

@Composable
fun MainScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CreateUser()
        Text(
            text = "Lista de usuarios guardados",
            modifier = Modifier.padding(horizontal = (24 + 4).dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        UsersList { route -> navController.navigate(route = route) }
    }
}
