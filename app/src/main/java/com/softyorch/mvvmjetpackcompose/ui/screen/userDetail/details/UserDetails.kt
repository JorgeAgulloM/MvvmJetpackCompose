package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Sms
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.core.intents.ActionsImpl.Companion.Actions
import com.softyorch.mvvmjetpackcompose.ui.componens.DataField
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAuto
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
                    EventDetails.Edit -> {
                        TopBar(
                            editMode = true,
                            user = state.user,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::setFavoriteBlockedOrNone
                        ) {
                            viewModel.eventManager(EventDetails.Read)
                        }
                        BodyEdit(
                            user = state.user,
                            userError = userError,
                            stateError,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::onDataChange
                        )
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

                    EventDetails.Delete -> DetailsInfo(text = "Usuario eliminado!")
                }
            }

            is StateDetails.Error -> DetailsInfo(text = state.msg)
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
                //Introducir pregunta aàra reafirmar la eliminación
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

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                onDataChange(user.copy(photoUri = uri.toString()))
            }
        }

    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Color.Transparent, shape = CircleShape)
            .clip(shape = CircleShape)
            .clickable {
                launcher.launch(arrayOf("image/*"))
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageUserAuto(
            userImage = user.photoUri,
            userLogo = user.logo,
            userLogoColor = user.logoColor,
            size = 200.dp
        )
    }
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
        Text(text = "Editar")
    }

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

        ImageUserAuto(userImage = user.photoUri, userLogo = user.logo, userLogoColor = user.logoColor, size = 200.dp)
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
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
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
