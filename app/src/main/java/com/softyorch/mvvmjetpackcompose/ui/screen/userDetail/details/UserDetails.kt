package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.core.intents.ActionsImpl.Companion.Actions
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAuto
import com.softyorch.mvvmjetpackcompose.ui.componens.userFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.componens.userFields.FromContact
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
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
                    EventDetails.Edit -> {
                        TopBar(
                            editMode = true,
                            user = state.user,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::setFavoriteBlockedOrNone
                        ) {
                            viewModel.eventManager(EventDetails.Read)
                        }
                        FromContact(
                            editUser = true,
                            user = state.user,
                            userErrors = userError,
                            stateError = stateError,
                            onDataChange = viewModel::onDataChange
                        ) {
                            val isValid = viewModel.setUsers(state.user)
                            if (isValid) viewModel.eventManager(EventDetails.Read)
                        }
                    }

                    EventDetails.Read -> {
                        TopBar(
                            user = state.user,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::onDataChange
                        ) {
                            onClick()
                        }
                        BodyRead(state.user)
                    }

                    EventDetails.Deleting ->
                        DeleteContact(state.user.name, onEventManager = viewModel::eventManager)

                    EventDetails.Delete -> {
                        TopBar(
                            user = state.user,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::onDataChange
                        ) {
                            onClick()
                        }
                        DetailsInfo(text = "Usuario eliminado!")
                    }
                }
            }

            is StateDetails.Error -> DetailsInfo(text = state.msg)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteContact(
    userName: String,
    onEventManager: (EventDetails) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEventManager(EventDetails.Read) }
    ) {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            ).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Eliminar a $userName de la lista de contactos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "¿Estás seguro?", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onEventManager(EventDetails.Read) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(text = "No", style = MaterialTheme.typography.bodyLarge)
                }
                Button(
                    onClick = { onEventManager(EventDetails.Delete) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(text = "Si", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun TopBar(
    editMode: Boolean = false,
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
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!editMode) TopIcon(icon = Icons.Outlined.Edit) { onEvent(EventDetails.Edit) }
            if (!blocked) TopIcon(icon = iconStart) {
                onDataChange(user.copy(favorite = !favorite))
                if (!editMode) onEvent(EventDetails.Read)
            }
            TopIcon(icon = Icons.Outlined.Block, color = colorBlock) {
                onDataChange(user.copy(favorite = false, phoneBlocked = !blocked))
                if (!editMode) onEvent(EventDetails.Read)
            }
            TopIcon(
                icon = Icons.Outlined.Delete,
                color = MaterialTheme.colorScheme.error
            ) {
                onEvent(EventDetails.Deleting)
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
private fun BodyRead(user: UserUi) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val surName = if (user.surName?.contains(" ") == true)
            user.surName.split(" ")[0]
        else user.surName

        ImageUserAuto(
            userImage = user.photoUri,
            userLogo = user.logo,
            userLogoColor = user.logoColor,
            size = 200.dp
        )
        Text(
            text = "${user.name} $surName",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconAction(icon = Icons.Outlined.Phone, text = "Llamar") {
            context.Actions().sendDial(user.phoneNumber)
        }
        IconAction(icon = Icons.Outlined.Sms, text = "SMS") {
            context.Actions().sendSMS(user.phoneNumber, user.name)
        }
        IconAction(icon = Icons.Outlined.Email, text = "Email") {
            context.Actions().sendEmail(user.phoneNumber, user.name)
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.large
            )
    ) {
        Text(
            text = "Información de contacto",
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelLarge
        )
        TextRead(icon = Icons.Outlined.Person, text = "${user.name} ${user.surName}")
        TextRead(icon = Icons.Outlined.Phone, text = user.phoneNumber)
        TextRead(icon = Icons.Outlined.Email, text = user.email)
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
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun IconAction(icon: ImageVector, text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.clip(CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier.padding(8.dp).background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}
