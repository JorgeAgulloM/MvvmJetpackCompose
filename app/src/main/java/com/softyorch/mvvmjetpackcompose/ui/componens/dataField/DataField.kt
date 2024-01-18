package com.softyorch.mvvmjetpackcompose.ui.componens.dataField

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING

@Composable
fun DataField(
    label: String,
    text: String,
    enabled: Boolean = true,
    error: Boolean,
    onlyNumbs: Boolean = false,
    supportingText: String,
    onTextChange: (String) -> Unit
){
    val keyboardOptions = if (onlyNumbs) KeyboardOptions(keyboardType = KeyboardType.Number)
    else KeyboardOptions(capitalization = KeyboardCapitalization.Words)

    OutlinedTextField(
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
        enabled = enabled,
        label = { Text(text = label) },
        placeholder = { Text(text = label) },
        supportingText = { if (error) Text(text = supportingText) },
        isError = error,
        keyboardOptions = keyboardOptions,
        shape = MaterialTheme.shapes.large
    )
}
