package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAuto
import com.softyorch.mvvmjetpackcompose.ui.componens.dataField.DataField
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

@Composable
fun UserDetails(
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>(),
    userId: UUID,
    onClick: () -> Unit
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
        when (val state = stateDetails) {
            StateDetails.Loading -> DetailsInfo(text = "Cargando...")
            is StateDetails.Success -> {
                when (eventDetails) {
                    EventDetails.Edit -> BodyEdit(
                        user = state.user,
                        userError = userError,
                        stateError,
                        onEvent = viewModel::eventManager,
                        onDataChange = viewModel::onDataChange
                    )

                    EventDetails.Read -> {
                        TopBar(
                            state.user,
                            viewModel::eventManager,
                            viewModel::onDataChange
                        ) {
                            onClick()
                        }
                        BodyRead(state.user)
                    }

                    EventDetails.Delete -> DetailsInfo(text = "Usuario eliminado!")
                }
            }

            is StateDetails.Error -> DetailsInfo(text = state.msg)
        }
    }
}

@Composable
fun TopBar(
    user: UserUi,
    onEvent: (EventDetails) -> Unit,
    onDataChange: (UserUi) -> Unit,
    onBackClick: () -> Unit
) {
    val favorite = user.favorite ?: false
    val iconStart = if (favorite) Icons.Filled.Star else Icons.Filled.StarBorder

    val blocked = user.phoneBlocked ?: false
    val colorBlock = if (blocked) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackClick() }
        ) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = null)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopIcon(icon = Icons.Outlined.Edit) { onEvent(EventDetails.Edit) }
            if (!blocked) TopIcon(icon = iconStart) {
                onDataChange(user.copy(favorite = !favorite))
                onEvent(EventDetails.Read)
            }
            TopIcon(icon = Icons.Outlined.Block, color = colorBlock) {
                onDataChange(user.copy(favorite = false, phoneBlocked = !blocked))
                onEvent(EventDetails.Read)
            }
            TopIcon(
                icon = Icons.Outlined.Delete,
                color = MaterialTheme.colorScheme.error
            ) {
                onEvent(EventDetails.Delete)
            }
        }
    }
}

@Composable
fun TopIcon(
    icon: ImageVector,
    contDescription: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Icon(
        imageVector = icon,
        contentDescription = contDescription,
        modifier = Modifier.padding(horizontal = 8.dp).clip(shape = CircleShape).clickable {
            onClick()
        },
        tint = color
    )
}

@Composable
private fun BodyEdit(
    user: UserUi,
    userError: UserErrorModel,
    stateError: StateError,
    onEvent: (EventDetails) -> Unit,
    onDataChange: (UserUi) -> Unit
) {
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    DataField(
        label = "Nombre",
        text = user.name,
        error = userError.name,
        supportingText = "Debe contener al menos 4 caracteres",
        keyboardType = KeyboardType.Text,
        leadingIcon = Icons.Outlined.Person
    ) { name -> onDataChange(user.copy(name = name)) }
    DataField(
        label = "Apellido",
        text = user.surName ?: EMPTY_STRING,
        error = userError.name,
        supportingText = "Debe contener al menos 4 caracteres",
        keyboardType = KeyboardType.Text,
        leadingIcon = Icons.Outlined.Person
    ) { surName -> onDataChange(user.copy(surName = surName)) }
    DataField(
        label = "Teléfono",
        text = user.phoneNumber,
        error = userError.name,
        supportingText = "Solo puede contener número",
        keyboardType = KeyboardType.Phone,
        leadingIcon = Icons.Outlined.Phone
    ) { phoneNumber -> onDataChange(user.copy(phoneNumber = phoneNumber)) }
    DataField(
        label = "Email",
        text = user.email ?: EMPTY_STRING,
        error = userError.name,
        supportingText = "El email no es correcto",
        keyboardType = KeyboardType.Email,
        leadingIcon = Icons.Outlined.Email
    ) { email -> onDataChange(user.copy(email = email)) }
    DataField(
        label = "Edad",
        text = user.age ?: EMPTY_STRING,
        error = userError.age,
        isLast = true,
        supportingText = "Edad no puede estar vacío",
        keyboardType = KeyboardType.Number,
        leadingIcon = Icons.Outlined.CalendarMonth
    ) { age -> onDataChange(user.copy(age = age)) }
    Button(
        onClick = { onEvent(EventDetails.Read) },
        modifier = Modifier.padding(16.dp),
        enabled = stateError == StateError.Working
    ) {
        Text(text = "Guardar")
    }

}

@Composable
private fun BodyRead(user: UserUi) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageUserAuto(userImage = null, userName = user.name, size = 200.dp)
        Text(
            text = "${user.name} ${user.surName}",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center)
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.large
            )
    ) {
        Text(
            text = "Información de contacto",
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelLarge
        )
        TextRead(icon = Icons.Outlined.Phone, text = user.phoneNumber)
        TextRead(icon = Icons.Outlined.Email, text = user.email)
        TextRead(icon = Icons.Outlined.CalendarMonth, text = user.age)
    }
}


@Composable
private fun TextRead(icon: ImageVector, text: String?) {
    if (!text.isNullOrEmpty()) Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = "info")
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun DetailsInfo(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}
