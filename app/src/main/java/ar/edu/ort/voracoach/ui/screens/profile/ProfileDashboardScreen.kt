package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.model.ProfileSettings
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/** 7.1 Profile Dashboard (Figma "Profile / 01 — Dashboard"). */
@Composable
fun ProfileDashboardScreen(
    settings: ProfileSettings,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onReviewPreferencesClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onConnectedDataClick: () -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextPrimary,
                modifier = Modifier.weight(1f)
            )
            VoraIconCircleButton(
                icon = Icons.Outlined.Settings,
                contentDescription = "Settings",
                onClick = onSettingsClick,
                iconTint = VoraGreen,
                modifier = Modifier.size(36.dp)
            )
        }
        IdentityCard(settings = settings, onEditProfileClick = onEditProfileClick)
        ProfileCard {
            Text("MY SNAPSHOT", style = MaterialTheme.typography.labelSmall, color = VoraTextTertiary)
            LabelValueRow("Goal", "Build strength & improve fitness")
            LabelValueRow("Experience", DemoData.alexMorgan.level)
            LabelValueRow("Training", "${settings.trainingDays}/week")
            LabelValueRow("Preferred Workout", "${settings.workoutPreferences.firstOrNull() ?: "Strength"} training")
        }
        PersonalisationCard(onReviewPreferencesClick = onReviewPreferencesClick)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileNavigationRow(
                title = "Your Goals",
                subtitle = "Build strength & improve fitness",
                icon = Icons.Outlined.TrackChanges,
                onClick = onGoalsClick
            )
            ProfileNavigationRow(
                title = "Your Preferences",
                subtitle = "Training, nutrition and coaching",
                icon = Icons.Outlined.Tune,
                onClick = onPreferencesClick
            )
            ProfileNavigationRow(
                title = "Connected Data",
                subtitle = "Health & activity data",
                icon = Icons.Outlined.MonitorHeart,
                onClick = onConnectedDataClick,
                trailing = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Synced",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier
                                .clip(VoraPillShape)
                                .background(VoraGreen)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                        Icon(
                            Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = VoraTextTertiary
                        )
                    }
                }
            )
            ProfileNavigationRow(
                title = "Account",
                subtitle = "Subscription, notifications & privacy",
                icon = Icons.Outlined.Lock,
                onClick = onAccountClick
            )
        }
    }
}

@Composable
private fun IdentityCard(settings: ProfileSettings, onEditProfileClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.img_avatar_alex),
            contentDescription = "Profile photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(72.dp).clip(CircleShape)
        )
        Spacer(Modifier.size(6.dp))
        Text(
            text = settings.fullName,
            style = MaterialTheme.typography.titleMedium,
            color = VoraTextPrimary
        )
        Text(
            text = "Building strength & improving fitness",
            style = MaterialTheme.typography.bodyMedium,
            color = VoraTextSecondary
        )
        Spacer(Modifier.size(6.dp))
        Text(
            text = "Edit profile",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = VoraTextPrimary,
            modifier = Modifier
                .clip(VoraPillShape)
                .background(VoraBackground)
                .clickable(onClick = onEditProfileClick)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun PersonalisationCard(onReviewPreferencesClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraOrbGraphic(size = 28.dp)
            Spacer(Modifier.width(8.dp))
            Text("VORA Personalisation", style = MaterialTheme.typography.titleSmall, color = VoraMint)
        }
        Text(
            text = "VORA knows your goals",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        Text(
            text = "Your coaching adapts to your training, nutrition and activity preferences.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )
        VoraLimeButton(text = "Review preferences", onClick = onReviewPreferencesClick)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1171)
@Composable
private fun ProfileDashboardScreenPreview() {
    VoraCoachTheme {
        ProfileDashboardScreen(
            settings = ProfileSettings(),
            onSettingsClick = {},
            onEditProfileClick = {},
            onReviewPreferencesClick = {},
            onGoalsClick = {},
            onPreferencesClick = {},
            onConnectedDataClick = {},
            onAccountClick = {}
        )
    }
}
