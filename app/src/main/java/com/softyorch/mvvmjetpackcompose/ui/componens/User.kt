package com.softyorch.mvvmjetpackcompose.ui.componens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun User(user: UserUi, dataView: DataView = DataView.LastCall, onClick: (String) -> Unit) {
    val shape = MaterialTheme.shapes.large

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick(user.id.toString()) }
            .clip(shape = shape)
            .padding(start = 64.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageUserAuto(userImage = null, userLogo = user.logo, userLogoColor = user.logoColor)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            DataUser(user, dataView)
            FavoriteOrBlocked(user.favorite, user.phoneBlocked)
        }
    }
}

@Composable
private fun DataUser(user: UserUi, dataView: DataView) {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${user.name} ${user.surName}",
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis
        )
        when (dataView) {
            DataView.LastCall -> user.typeCall?.let { LastCall(it, user.lastCall!!) }
            else -> {
                UserSubData(data = user.phoneNumber, icon = Icons.Outlined.Phone)
            }
        }
    }
}

@Composable
private fun FavoriteOrBlocked(favorite: Boolean?, blocked: Boolean?) {
    val contentDescription = if (blocked == true) "Usuario bloqueado"
    else if (favorite == true) "Usuario favorito"
    else EMPTY_STRING

    if (blocked == true) Icon(
        imageVector = Icons.Default.Block,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.error
    )

    if (favorite == true) Icon(
        imageVector = Icons.Default.Star,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun LastCall(typeCall: Int, lastCall: Long) {
    val icon = when (typeCall) {
        0 -> Icons.Default.CallMissed
        1 -> Icons.Default.CallReceived
        else -> Icons.Default.CallMade
    }
    val color = when (typeCall) {
        0 -> MaterialTheme.colorScheme.error
        1 -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    UserSubData(data = dateLongToString(lastCall), icon = icon, color = color)
}

@Composable
fun UserSubData(
    data: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 2.dp),
            tint = color
        )
        Text(
            text = data,
            style = MaterialTheme.typography.labelMedium
        )
    }

}

private fun dateLongToString(timeInMillis: Long): String {
    val format = SimpleDateFormat("dd-MM - HH:mm", Locale.getDefault())
    val date = Date(timeInMillis)
    return format.format(date)
}

sealed class DataView {
    data object LastCall : DataView()
    data object NumberAndEmail : DataView()
}
