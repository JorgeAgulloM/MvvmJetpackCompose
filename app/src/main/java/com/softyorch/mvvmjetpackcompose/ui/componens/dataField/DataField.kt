package com.softyorch.mvvmjetpackcompose.ui.componens.dataField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DataField(
    label: String,
    text: String,
    enabled: Boolean = true,
    error: Boolean,
    onlyNumbs: Boolean = false,
    supportingText: String,
    leadingIcon: ImageVector,
    onTextChange: (String) -> Unit
){
    val keyboardOptions = if (onlyNumbs) KeyboardOptions(keyboardType = KeyboardType.Number)
    else KeyboardOptions(capitalization = KeyboardCapitalization.Words)
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
        keyboardOptions = keyboardOptions,
        shape = shape
    )

}
