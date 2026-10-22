package ar.edu.ort.voracoach.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.model.NotificationItem
import ar.edu.ort.voracoach.model.NotificationSection
import ar.edu.ort.voracoach.model.NotificationType
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraEmptyView
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.NotificationsUiState

/**
 * 2.2 NotificationsScreen (Demostración) — réplica de "Home / 02 — Notifications".
 *
 * Como en el Figma, se muestra con la barra inferior (destino Home activo) y sin flecha:
 * el botón Atrás del sistema vuelve al Home. Las notificaciones se agrupan en TODAY y
 * YESTERDAY; cada una lleva a una pantalla coherente según su tipo (lo decide el grafo) y
 * al tocarla se marca leída, lo que actualiza la campana del Home (repositorio compartido).
 *
 * @param onMarkAllReadClick acción "Mark all as read".
 * @param onNotificationClick evento con la notificación tocada.
 */
@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onMarkAllReadClick: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    // groupBy conserva el orden de aparición: primero TODAY, después YESTERDAY.
    val sections = uiState.notifications.groupBy { it.section }
    LazyColumn(
        modifier = modifier.fillMaxSize().background(VoraBackground),
        contentPadding = PaddingValues(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notifications", style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
                Text(
                    "Mark all as read",
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                    color = if (uiState.hasUnread) VoraGreen else VoraTextTertiary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = uiState.hasUnread, onClick = onMarkAllReadClick)
                        .padding(4.dp)
                )
            }
        }
        if (uiState.notifications.isEmpty()) {
            item { VoraEmptyView(title = "You're all caught up", message = "New reminders and tips from VORA will appear here.") }
        }
        NotificationSection.entries.forEach { section ->
            val items = sections[section].orEmpty()
            if (items.isNotEmpty()) {
                item(key = section.name) {
                    Text(
                        section.label,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = VoraTextSecondary,
                        modifier = Modifier.padding(top = if (section == NotificationSection.TODAY) 0.dp else 10.dp, bottom = 2.dp)
                    )
                }
                items(items, key = { it.id }) { notification ->
                    NotificationCard(notification = notification, onClick = { onNotificationClick(notification) })
                }
            }
        }
    }
}

/**
 * Tarjeta de notificación: ícono de 16 dp en un recuadro de 36 dp, categoría (12 SemiBold
 * gris) + tiempo (11 tenue), título 14 SemiBold y detalle 13 Regular. Las no leídas muestran
 * un punto verde de 6 dp arriba a la derecha.
 */
@Composable
private fun NotificationCard(notification: NotificationItem, onClick: () -> Unit) {
    Box {
        VoraCard(onClick = onClick, contentPadding = PaddingValues(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
                    if (notification.type == NotificationType.COACH) {
                        // Image: el ícono "sparkles" del Figma tiene relleno blanco y trazo verde.
                        Image(painterResource(R.drawable.ic_sparkles), contentDescription = null, modifier = Modifier.size(16.dp))
                    } else {
                        Icon(painterResource(notification.type.iconRes()), contentDescription = null, tint = VoraGreen, modifier = Modifier.size(16.dp))
                    }
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(end = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(notification.category, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraTextSecondary)
                        Text(notification.timeAgo, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal), color = VoraTextTertiary)
                    }
                    Text(notification.title, style = MaterialTheme.typography.titleSmall, color = VoraTextPrimary)
                    Text(notification.message, style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp), color = VoraTextSecondary)
                }
            }
        }
        if (notification.isUnread) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 11.dp, end = 11.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(VoraGreen)
            )
        }
    }
}

/** Ícono asociado a cada tipo de notificación (exportados del Figma). */
private fun NotificationType.iconRes(): Int = when (this) {
    NotificationType.WORKOUT -> R.drawable.ic_nav_dumbbell
    NotificationType.NUTRITION -> R.drawable.ic_nav_apple
    NotificationType.COACH -> R.drawable.ic_sparkles
    NotificationType.PROGRESS -> R.drawable.ic_nav_chart
    NotificationType.GOAL -> R.drawable.ic_flame
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun NotificationsPreview() {
    VoraCoachTheme {
        NotificationsScreen(NotificationsUiState(DemoData.notifications), {}, {})
    }
}
