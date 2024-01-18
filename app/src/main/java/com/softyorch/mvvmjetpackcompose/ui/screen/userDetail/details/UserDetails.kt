package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.dataField.DataField
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

@Composable
fun UserDetails(
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>(),
    userId: UUID
) {

    LaunchedEffect(true) {
        viewModel.getUSer(userId)
    }

    val stateDetails: StateDetails by viewModel.stateDetails.collectAsStateWithLifecycle()
    val eventDetails: EventDetails by viewModel.eventDetails.collectAsStateWithLifecycle()
    val stateError: StateError by viewModel.stateError.collectAsStateWithLifecycle()
    val userError: UserErrorModel by viewModel.userError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        when (stateDetails) {
            StateDetails.Loading -> Text(text = "Cargando...")
            is StateDetails.Success -> {
                when (eventDetails) {
                    EventDetails.Edit -> BodyEdit(
                        user = (stateDetails as StateDetails.Success).user,
                        userError = userError,
                        onDataChange = viewModel::onDataChange
                    )

                    else -> BodyRead((stateDetails as StateDetails.Success).user)
                }
            }

            is StateDetails.Error -> Text(text = (stateDetails as StateDetails.Error).msg)
        }
        DetailsButton(eventDetails, stateError) { event ->
            viewModel.eventManager(event)
        }
    }
}

@Composable
private fun BodyEdit(
    user: UserUi,
    userError: UserErrorModel,
    onDataChange: (String, String) -> Unit
) {
    DataField(
        text = user.id.toString(),
        label = "Id de usuario",
        enabled = false,
        error = false,
        supportingText = EMPTY_STRING,
        onTextChange = {}
    )
    DataField(
        text = user.name,
        label = "Nombre",
        error = userError.name,
        supportingText = "Debe contener al menos 3 caracteres",
        onTextChange = { name -> onDataChange(name, user.age) }
    )
    DataField(
        text = user.age,
        label = "Edad",
        error = userError.age,
        supportingText = "Edad no puede estar vacío",
        onlyNumbs = true,
        onTextChange = { age -> onDataChange(user.name, age) }
    )
}

@Composable
private fun BodyRead(user: UserUi) {
    TextRead(label = "Id de usuario", text = user.id.toString())
    TextRead(label = "Nombre", text = user.name)
    TextRead(label = "Edad", text = user.age)
}


@Composable
private fun TextRead(label: String, text: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.large
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DetailsButton(
    eventDetails: EventDetails,
    stateError: StateError,
    onClick: (EventDetails) -> Unit
) {
    val nextEvent: EventDetails
    val text: String
    val color: Color

    when (eventDetails) {
        EventDetails.Edit -> {
            nextEvent = EventDetails.Read
            text = "Salvar"
            color = MaterialTheme.colorScheme.primary
        }

        else -> {
            nextEvent = EventDetails.Edit
            text = "Editar"
            color = MaterialTheme.colorScheme.secondary
        }
    }

    Button(
        onClick = { onClick(nextEvent) },
        modifier = Modifier.padding(horizontal = 24.dp),
        enabled = stateError == StateError.Working,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text = text)
    }
}
