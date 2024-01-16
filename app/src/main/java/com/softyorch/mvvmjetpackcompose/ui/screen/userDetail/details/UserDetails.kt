package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.USER_ID
import java.util.UUID

@Composable
fun UserDetails(
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>(),
    userId: UUID
) {

    LaunchedEffect(true) {
        viewModel.getUSer(userId)
    }

    val stateDetails: StateDetails by viewModel.stateDetails.collectAsStateWithLifecycle(
        StateDetails.Loading
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        when (stateDetails) {
            StateDetails.Loading -> Text(text = "Cargando...")
            is StateDetails.Success -> Body((stateDetails as StateDetails.Success).user)
            is StateDetails.Error -> Text(text = (stateDetails as StateDetails.Error).msg)
        }
    }

}

@Composable
private fun Body(user: UserUi) {
    DataField(label = USER_ID, value = user.id.toString(), enabled = false)
    DataField(label = "Nombre", value = user.name)
    DataField(label = "Edad", value = user.age.toString())
}

@Composable
private fun DataField(label: String, value: String, enabled: Boolean = true) {
    TextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
        enabled = enabled,
        label = { Text(text = label) },
        shape = MaterialTheme.shapes.extraSmall.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        )
    )
}

