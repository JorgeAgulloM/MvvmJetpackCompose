package com.softyorch.mvvmjetpackcompose.ui.componens.contactFields

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.mvvmjetpackcompose.R
import com.softyorch.mvvmjetpackcompose.ui.componens.DataField
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageContactAuto
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import com.softyorch.mvvmjetpackcompose.utils.MIN_NAME_LENGTH

@Composable
fun FromContact(
    editContact: Boolean = false,
    contact: ContactUi,
    contactErrors: ContactErrorModel,
    stateError: StateError,
    onDataChange: (ContactUi) -> Unit,
    setContact: () -> Unit
) {
    val title = if (editContact)
        stringResource(R.string.form_contact_data_edit, contact.name)
    else
        stringResource(R.string.form_contact_data_create)
    val textButton = if (editContact)
        stringResource(R.string.form_contact_save)
    else
        stringResource(R.string.form_contact_create)
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                onDataChange(contact.copy(photoUri = uri.toString()))
            }
        }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            style = MaterialTheme.typography.headlineSmall.copy(
                textAlign = TextAlign.Center
            )
        )
        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center){
            Row(
                modifier = Modifier
                    .background(color = Color.Transparent, shape = CircleShape)
                    .clip(shape = CircleShape)
                    .clickable {
                        launcher.launch(arrayOf("image/*"))
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageContactAuto(
                    contactImage = contact.photoUri,
                    contactLogo = contact.logo,
                    contactLogoColor = contact.logoColor,
                    size = 200.dp
                )
            }
        }
        DataField(
            label = stringResource(R.string.form_contact_name),
            text = contact.name,
            error = contactErrors.name,
            supportingText = stringResource(R.string.form_contact_support_char_min, MIN_NAME_LENGTH),
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        ) { name -> onDataChange(contact.copy(name = name)) }
        DataField(
            label = stringResource(R.string.form_contact_surname),
            text = contact.surName ?: EMPTY_STRING,
            error = contactErrors.surName,
            supportingText = stringResource(R.string.form_contact_support_char_min, MIN_NAME_LENGTH),
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        ) { surName -> onDataChange(contact.copy(surName = surName)) }
        DataField(
            label = stringResource(R.string.form_contact_phone),
            text = contact.phoneNumber,
            error = contactErrors.phoneNumber,
            supportingText = stringResource(R.string.form_contact_support_only_numbs),
            keyboardType = KeyboardType.Phone,
            leadingIcon = Icons.Default.Phone
        ) { phoneNumber -> onDataChange(contact.copy(phoneNumber = phoneNumber)) }
        DataField(
            label = stringResource(R.string.form_contact_email),
            text = contact.email ?: EMPTY_STRING,
            error = contactErrors.email,
            supportingText = stringResource(R.string.form_contact_support_email),
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Mail
        ) { email -> onDataChange(contact.copy(email = email)) }
        DataField(
            label = stringResource(R.string.form_contact_age),
            text = contact.age ?: EMPTY_STRING,
            isLast = true,
            error = contactErrors.age,
            supportingText = stringResource(R.string.form_contact_support_age),
            keyboardType = KeyboardType.Number,
            leadingIcon = Icons.Default.CalendarMonth
        ) { age -> onDataChange(contact.copy(age = age)) }
        Button(
            onClick = {
                setContact()
                focusManager.clearFocus()
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            enabled = stateError == StateError.Working,
            elevation = ButtonDefaults.buttonElevation()
        ) {
            Text(text = textButton, style = MaterialTheme.typography.bodyLarge)
        }
    }
}