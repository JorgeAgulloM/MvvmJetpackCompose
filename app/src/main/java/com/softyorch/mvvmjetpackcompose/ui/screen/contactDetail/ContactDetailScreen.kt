package com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details.ContactDetails
import java.util.UUID

@Composable
fun ContactDetailScreen(contactId: UUID, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ContactDetails(contactId = contactId) { onClick() }
    }
}
