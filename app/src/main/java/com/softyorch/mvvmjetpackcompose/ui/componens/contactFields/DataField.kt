package com.softyorch.mvvmjetpackcompose.ui.componens.contactFields

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.softyorch.mvvmjetpackcompose.R
import com.softyorch.mvvmjetpackcompose.utils.TEXT_FIELD

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
    val backColor = MaterialTheme.colorScheme.background
    val bottomDP = if (error) 8.dp else 4.dp

    TextField(
        value = text,
        onValueChange = { onTextChange(it) },
        modifier = Modifier.fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = bottomDP)
            .testTag(TEXT_FIELD),
        enabled = enabled,
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(text = label) },
        placeholder = { Text(text = label, color = MaterialTheme.colorScheme.surfaceVariant) },
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = stringResource(R.string.data_field_content_desc_field, label)
            )
        },
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
            focusedContainerColor = backColor,
            unfocusedContainerColor = backColor,
            errorContainerColor = backColor
        )
    )
}
