package com.softyorch.mvvmjetpackcompose.ui.componens.userFields

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.mvvmjetpackcompose.ui.componens.DataField
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAuto
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING

@Composable
fun FromContact(
    editUser: Boolean = false,
    user: UserUi,
    userErrors: UserErrorModel,
    stateError: StateError,
    onDataChange: (UserUi) -> Unit,
    setUsers: () -> Unit
) {
    val title = if (editUser) "Editar datos de ${user.name}" else "Crear nuevo contacto"
    val textButton = if (editUser) "Guardar" else "Crear"

    val focusManager = LocalFocusManager.current
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
                ImageUserAuto(
                    userImage = user.photoUri,
                    userLogo = user.logo,
                    userLogoColor = user.logoColor,
                    size = 200.dp
                )
            }
        }
        DataField(
            label = "Nombre",
            text = user.name,
            error = userErrors.name,
            supportingText = "Debe contener al menos 4 caracteres",
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        ) { name -> onDataChange(user.copy(name = name)) }
        DataField(
            label = "Apellidos",
            text = user.surName ?: EMPTY_STRING,
            error = userErrors.surName,
            supportingText = "Debe contener al menos 4 caracteres",
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        ) { surName -> onDataChange(user.copy(surName = surName)) }
        DataField(
            label = "Teléfono",
            text = user.phoneNumber,
            error = userErrors.phoneNumber,
            supportingText = "Solo puede contener número",
            keyboardType = KeyboardType.Phone,
            leadingIcon = Icons.Default.Phone
        ) { phoneNumber -> onDataChange(user.copy(phoneNumber = phoneNumber)) }
        DataField(
            label = "Email",
            text = user.email ?: EMPTY_STRING,
            error = userErrors.email,
            supportingText = "El email no es correcto",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Mail
        ) { email -> onDataChange(user.copy(email = email)) }
        DataField(
            label = "Edad",
            text = user.age ?: EMPTY_STRING,
            isLast = true,
            error = userErrors.age,
            supportingText = "Edad no puede estar vacío",
            keyboardType = KeyboardType.Number,
            leadingIcon = Icons.Default.CalendarMonth
        ) { age -> onDataChange(user.copy(age = age)) }
        Button(
            onClick = {
                setUsers()
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