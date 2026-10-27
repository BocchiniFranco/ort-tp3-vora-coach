package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ProfileSettings
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/** 7.6 App Settings (Figma "Profile / 06 — Settings", título "Settings"). */
@Composable
fun AppSettingsScreen(
    settings: ProfileSettings,
    showLogoutDialog: Boolean,
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onDemoOptionClick: (String) -> Unit,
    onLogoutClick: () -> Unit,
    onConfirmLogout: () -> Unit,
    onDismissLogout: () -> Unit
) {
    ProfileSubScreen(title = "Settings", onBackClick = onBackClick) {
        ProfileSectionLabel("Account")
        ProfileNavigationRow(settings.fullName, Icons.Outlined.Person, onAccountClick, subtitle = "alex@morgan.com")
        ProfileNavigationRow("VORA Premium", Icons.Outlined.StarOutline, { onDemoOptionClick("VORA Premium") }, subtitle = "Renews 14 June 2026")
        ProfileNavigationRow("Manage account", Icons.Outlined.EditNote, onAccountClick)

        ProfileSectionLabel("App preferences")
        ProfileNavigationRow("Notifications", Icons.Outlined.Notifications, onNotificationsClick, subtitle = "Workout and meal reminders")
        ProfileNavigationRow("Units", Icons.Outlined.Explore, { onDemoOptionClick("Units") }, subtitle = "Metric (kg, cm, km)")
        ProfileNavigationRow("Appearance", Icons.Outlined.Visibility, { onDemoOptionClick("Appearance") }, subtitle = "System default")

        ProfileSectionLabel("Connected data")
        ProfileNavigationRow("Health & Activity", Icons.Outlined.MonitorHeart, { onDemoOptionClick("Health & Activity") }, subtitle = "Apple Watch & Health connected")

        ProfileSectionLabel("Privacy & security")
        ProfileNavigationRow("Privacy Policy", Icons.Outlined.Lock, { onDemoOptionClick("Privacy Policy") })
        ProfileNavigationRow("Permissions & Data Controls", Icons.Outlined.Shield, { onDemoOptionClick("Permissions & Data Controls") })

        ProfileSectionLabel("Support")
        ProfileNavigationRow("Help Centre", Icons.Outlined.HelpOutline, { onDemoOptionClick("Help Centre") })
        ProfileNavigationRow("Contact Support", Icons.Outlined.ContactSupport, { onDemoOptionClick("Contact Support") })

        Text(
            text = "Log out",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = VoraStatusError,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .clip(VoraPillShape)
                .border(1.dp, VoraStatusError, VoraPillShape)
                .clickable(onClick = onLogoutClick)
                .padding(vertical = 12.dp)
        )
        Text(
            text = "VORA v1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = VoraTextTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = onDismissLogout,
            title = { Text("Log out?") },
            text = { Text("You'll need to sign in again to use VORA.") },
            confirmButton = {
                TextButton(onClick = onConfirmLogout) { Text("Log out", color = VoraStatusError) }
            },
            dismissButton = {
                TextButton(onClick = onDismissLogout) { Text("Cancel", color = VoraTextSecondary) }
            },
            containerColor = VoraSurface
        )
    }
}
