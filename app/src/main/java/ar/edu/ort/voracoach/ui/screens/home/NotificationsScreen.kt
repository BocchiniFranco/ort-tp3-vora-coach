package ar.edu.ort.voracoach.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.model.NotificationItem
import ar.edu.ort.voracoach.model.NotificationType
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraEmptyView
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.viewmodel.NotificationsUiState

/**
 * 2.2 NotificationsScreen (Demostración).
 *
 * Lista con LazyColumn (UI03: scroll en cualquier tamaño de pantalla). Cada notificación
 * lleva a una pantalla coherente según su tipo (lo decide VoraNavHost), y al tocarla se
 * marca como leída, lo que actualiza el badge del Home (repositorio compartido).
 *
 * @param onNotificationClick evento con la notificación tocada.
 * @param onMarkAllReadClick acción del ícono "✓✓" de la barra superior.
 */
@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onBackClick: () -> Unit,
    onMarkAllReadClick: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            VoraTopBar(title = "Notifications", onBack = onBackClick) {
                if (uiState.hasUnread) {
                    VoraIconCircleButton(icon = Icons.Rounded.DoneAll, contentDescription = "Mark all as read", onClick = onMarkAllReadClick)
                }
            }
        }
        if (uiState.notifications.isEmpty()) {
            item { VoraEmptyView(title = "You're all caught up", message = "New reminders and tips from Vora will appear here.") }
        }
        items(uiState.notifications, key = { it.id }) { notification ->
            NotificationCard(
                notification = notification,
                onClick = { onNotificationClick(notification) },
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }
    }
}

/** Tarjeta de una notificación; las no leídas tienen fondo blanco y punto verde. */
@Composable
private fun NotificationCard(notification: NotificationItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    VoraCard(
        modifier = modifier,
        onClick = onClick,
        containerColor = if (notification.isUnread) androidx.compose.ui.graphics.Color.White else VoraBackground
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(VoraLightMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(notification.type.icon(), contentDescription = null, tint = VoraDarkGreen, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(notification.title, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary, modifier = Modifier.weight(1f))
                    if (notification.isUnread) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(VoraMintProgress))
                    }
                }
                Text(notification.message, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary)
                Text(notification.timeAgo, style = MaterialTheme.typography.labelSmall, color = VoraTextSecondary)
            }
        }
    }
}

/** Ícono asociado a cada tipo de notificación. */
private fun NotificationType.icon(): ImageVector = when (this) {
    NotificationType.WORKOUT -> Icons.Rounded.FitnessCenter
    NotificationType.NUTRITION -> Icons.Rounded.Restaurant
    NotificationType.COACH -> Icons.Rounded.AutoAwesome
    NotificationType.PROGRESS -> Icons.Rounded.Insights
    NotificationType.GOAL -> Icons.Rounded.EmojiEvents
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun NotificationsPreview() {
    VoraCoachTheme {
        NotificationsScreen(NotificationsUiState(DemoData.notifications), {}, {}, {})
    }
}
