package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details.UserDetails
import java.util.UUID

@Composable
fun UserDetailScreen(userId: UUID, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        UserDetails(userId = userId) { onClick() }
    }
}
