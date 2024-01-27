package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.ui.componens.DataField
import com.softyorch.mvvmjetpackcompose.ui.componens.ImageUserAuto
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING

@Composable
fun CreateUser(
    viewModel: CreateUserViewModel = hiltViewModel<CreateUserViewModel>(),
    onCreateUser: () -> Unit
) {

    val user: UserUi by viewModel.user.collectAsStateWithLifecycle()
    val stateError: StateError by viewModel.stateErrors.collectAsStateWithLifecycle()
    val userErrors: UserErrorModel by viewModel.userError.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            Log.i("MYAPP", "Image: $uri")
            viewModel.onDataChange(user.copy(photoUri = uri.toString()))
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Crear nuevo contacto",
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            style = MaterialTheme.typography.headlineSmall.copy(
                textAlign = TextAlign.Center
            )
        )
        Row(
            modifier = Modifier.background(color = Color.Transparent, shape = CircleShape)
                .clip(shape = CircleShape)
                .clickable {
                    launcher.launch("image/*")
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
            error = userErrors.name,
            supportingText = "Debe contener al menos 4 caracteres",
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        ) { name -> viewModel.onDataChange(user.copy(name = name)) }
        DataField(
            label = "Apellidos",
            text = user.surName ?: EMPTY_STRING,
            error = userErrors.surName,
            supportingText = "Debe contener al menos 4 caracteres",
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        ) { surName -> viewModel.onDataChange(user.copy(surName = surName)) }
        DataField(
            label = "Teléfono",
            text = user.phoneNumber,
            error = userErrors.phoneNumber,
            supportingText = "Solo puede contener número",
            keyboardType = KeyboardType.Phone,
            leadingIcon = Icons.Default.Phone
        ) { phoneNumber -> viewModel.onDataChange(user.copy(phoneNumber = phoneNumber)) }
        DataField(
            label = "Email",
            text = user.email ?: EMPTY_STRING,
            error = userErrors.email,
            supportingText = "El email no es correcto",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Mail
        ) { email -> viewModel.onDataChange(user.copy(email = email)) }
        DataField(
            label = "Edad",
            text = user.age ?: EMPTY_STRING,
            isLast = true,
            error = userErrors.age,
            supportingText = "Edad no puede estar vacío",
            keyboardType = KeyboardType.Number,
            leadingIcon = Icons.Default.CalendarMonth
        ) { age -> viewModel.onDataChange(user.copy(age = age)) }
        Button(
            onClick = {
                val isValid = viewModel.setUsers()
                if (isValid) {
                    focusManager.clearFocus()
                    onCreateUser()
                }
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            enabled = stateError == StateError.Working,
            elevation = ButtonDefaults.buttonElevation()
        ) {
            Text(text = "Crear", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun selectImage(): ManagedActivityResultLauncher<String, Uri?> {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            Log.i("MYAPP", "Image: $uri")
        }
    }
    return launcher
}
