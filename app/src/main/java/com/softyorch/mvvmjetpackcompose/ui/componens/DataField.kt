package com.softyorch.mvvmjetpackcompose.ui.componens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DataField(
    label: String,
    text: String,
    enabled: Boolean = true,
    isLast: Boolean = false,
    error: Boolean,
    supportingText: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    onTextChange: (String) -> Unit
) {
    val shape = MaterialTheme.shapes.small.copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize
    )

    TextField(
        value = text,
        onValueChange = { onTextChange(it) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        enabled = enabled,
        label = { Text(text = label) },
        placeholder = { Text(text = label, color = MaterialTheme.colorScheme.surfaceVariant) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        supportingText = { if (error) Text(text = supportingText) },
        isError = error,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.Words,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        singleLine = true,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}
