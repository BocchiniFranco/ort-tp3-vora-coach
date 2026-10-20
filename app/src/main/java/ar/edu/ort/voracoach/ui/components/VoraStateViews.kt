package ar.edu.ort.voracoach.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

// =====================================================================================
// VISTAS DE ESTADO (RF05): cada estado de UiState/Resource tiene su representación
// visual clara: Loading, Empty, Error (con reintento) y banner de datos offline.
// =====================================================================================

/** Estado Loading: indicador circular + texto. */
@Composable
fun VoraLoadingView(modifier: Modifier = Modifier, message: String = "Loading...") {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(color = VoraDarkGreen)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary)
    }
}

/**
 * Estado Error: ícono, título, detalle y botón "Try again" (reintento obligatorio RF05).
 */
@Composable
fun VoraErrorView(
    title: String,
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.ErrorOutline,
    retryText: String = "Try again"
) {
    VoraMessageView(
        modifier = modifier,
        icon = icon,
        iconTint = VoraStatusError,
        title = title,
        message = message
    ) {
        VoraPrimaryButton(text = retryText, onClick = onRetry, icon = Icons.Rounded.Refresh)
    }
}

/** Estado Empty: el servidor respondió bien pero sin resultados. */
@Composable
fun VoraEmptyView(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    VoraMessageView(
        modifier = modifier,
        icon = Icons.Rounded.Inbox,
        iconTint = VoraDarkGreen,
        title = title,
        message = message
    ) {
        if (actionText != null && onAction != null) {
            VoraOutlinedButton(text = actionText, onClick = onAction)
        }
    }
}

/** Plantilla común de los estados Empty/Error. */
@Composable
private fun VoraMessageView(
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(VoraLightMint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(36.dp))
        }
        Text(title, style = MaterialTheme.typography.titleLarge, color = VoraTextPrimary, textAlign = TextAlign.Center)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary, textAlign = TextAlign.Center)
        action()
    }
}

/**
 * Banner que avisa que se están mostrando datos guardados en Room porque la red falló.
 * Cumple RF02/RF05: el usuario ve la caché y puede reintentar sin perderla.
 */
@Composable
fun VoraOfflineBanner(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraStatusError.copy(alpha = 0.08f))
            .border(1.dp, VoraStatusError.copy(alpha = 0.3f), shape)
            .padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Rounded.CloudOff, contentDescription = null, tint = VoraStatusError, modifier = Modifier.size(20.dp))
        Text(message, style = MaterialTheme.typography.bodySmall, color = VoraTextPrimary, modifier = Modifier.weight(1f))
        TextButton(onClick = onRetry) { Text("Retry", color = VoraDarkGreen) }
    }
}
