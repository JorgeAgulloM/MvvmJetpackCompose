package com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.R
import com.softyorch.mvvmjetpackcompose.core.intents.ActionsImpl.Companion.Actions
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageContactAuto
import com.softyorch.mvvmjetpackcompose.ui.componens.contactFields.FromContact
import com.softyorch.mvvmjetpackcompose.ui.componens.contactFields.StateError
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.util.UUID

@Composable
fun ContactDetails(
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>(),
    contactId: UUID,
    onClick: () -> Unit
) {

    LaunchedEffect(true) {
        viewModel.getContact(contactId)
    }

    val stateDetails: StateDetails by viewModel.stateDetails.collectAsStateWithLifecycle()
    val eventDetails: EventDetails by viewModel.eventDetails.collectAsStateWithLifecycle()
    val stateError: StateError by viewModel.stateError.collectAsStateWithLifecycle()
    val contactError: ContactErrorModel by viewModel.contactError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        when (val state = stateDetails) {
            StateDetails.Loading -> DetailsInfo(text = stringResource(R.string.contact_details_loading))
            is StateDetails.Success -> {
                when (eventDetails) {
                    EventDetails.Edit -> {
                        TopBar(
                            editMode = true,
                            contact = state.contact,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::setFavoriteBlockedOrNone
                        ) {
                            viewModel.eventManager(EventDetails.Read)
                        }
                        FromContact(
                            editContact = true,
                            contact = state.contact,
                            contactErrors = contactError,
                            stateError = stateError,
                            onDataChange = viewModel::onDataChange
                        ) {
                            val isValid = viewModel.setContacts(state.contact)
                            if (isValid) viewModel.eventManager(EventDetails.Read)
                        }
                    }

                    EventDetails.Read -> {
                        TopBar(
                            contact = state.contact,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::onDataChange
                        ) {
                            onClick()
                        }
                        BodyRead(state.contact)
                    }

                    EventDetails.Deleting ->
                        DeleteContact(state.contact.name, onEventManager = viewModel::eventManager)

                    EventDetails.Delete -> {
                        TopBar(
                            contact = state.contact,
                            onEvent = viewModel::eventManager,
                            onDataChange = viewModel::onDataChange
                        ) {
                            onClick()
                        }
                        DetailsInfo(text = stringResource(R.string.contact_details_contact_deleted))
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
    contactName: String,
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
            Text(
                text = stringResource(R.string.contact_details_delete_contact_info, contactName),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = stringResource(R.string.contact_details_sure),
                style = MaterialTheme.typography.bodyLarge
            )
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
                    Text(
                        text = stringResource(R.string.contact_details_btn_no),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Button(
                    onClick = { onEventManager(EventDetails.Delete) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.contact_details_btn_yes),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    editMode: Boolean = false,
    contact: ContactUi,
    onEvent: (EventDetails) -> Unit,
    onDataChange: (ContactUi) -> Unit,
    onBackClick: () -> Unit
) {
    val favorite = contact.favorite ?: false
    val iconStart = if (favorite) Icons.Filled.Star else Icons.Filled.StarBorder

    val blocked = contact.phoneBlocked ?: false
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
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.contact_details_content_desc_go_back)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!editMode) TopIcon(icon = Icons.Outlined.Edit) { onEvent(EventDetails.Edit) }
            if (!blocked) TopIcon(icon = iconStart) {
                onDataChange(contact.copy(favorite = !favorite))
                if (!editMode) onEvent(EventDetails.Read)
            }
            TopIcon(icon = Icons.Outlined.Block, color = colorBlock) {
                onDataChange(contact.copy(favorite = false, phoneBlocked = !blocked))
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
private fun BodyRead(contact: ContactUi) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val surName = if (contact.surName?.contains(" ") == true)
            contact.surName.split(" ")[0]
        else contact.surName

        Row(
            modifier = Modifier
                .background(color = Color.Transparent, shape = CircleShape)
                .clip(shape = CircleShape)
        ) {
            ImageContactAuto(
                contactImage = contact.photoUri,
                contactLogo = contact.logo,
                contactLogoColor = contact.logoColor,
                size = 200.dp
            )
        }
        Text(
            text = "${contact.name} $surName",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconAction(
            icon = Icons.Outlined.Phone,
            contentDesc = stringResource(R.string.contact_details_content_desc_icon_call),
            enabled = contact.phoneNumber != EMPTY_STRING && contact.phoneBlocked != true,
            text = stringResource(R.string.contact_details_btn_call)
        ) {
            context.Actions().sendDial(contact.phoneNumber)
        }
        IconAction(
            icon = Icons.Outlined.Sms,
            contentDesc = stringResource(R.string.contact_details_content_desc_icon_send_sms),
            enabled = contact.phoneNumber != EMPTY_STRING && contact.phoneBlocked != true,
            text = stringResource(R.string.contact_details_btn_sms)
        ) {
            context.Actions().sendSMS(contact.phoneNumber, contact.name)
        }
        IconAction(
            icon = Icons.Outlined.Email,
            contentDesc = stringResource(R.string.contact_details_content_desc_icon_send_email),
            enabled = contact.email != EMPTY_STRING && contact.phoneBlocked != true,
            text = stringResource(R.string.contact_details_btn_email)
        ) {
            context.Actions().sendEmail(contact.phoneNumber, contact.name)
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
            text = stringResource(R.string.contact_details_contact_info),
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelLarge
        )
        TextRead(icon = Icons.Outlined.Person, text = "${contact.name} ${contact.surName}")
        TextRead(icon = Icons.Outlined.Phone, text = contact.phoneNumber)
        TextRead(icon = Icons.Outlined.Email, text = contact.email)
    }
}


@Composable
private fun TextRead(icon: ImageVector, text: String?) {
    if (!text.isNullOrEmpty()) Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.contact_details_content_desc_info)
        )
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
fun IconAction(
    icon: ImageVector,
    contentDesc: String,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val color = if (enabled) MaterialTheme.colorScheme.secondaryContainer
    else MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier.clip(CircleShape).clickable {
            if (enabled) onClick()
        },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier.padding(8.dp).background(
                    color = color,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = contentDesc,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}
