package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.ProfileSettings
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintBorderTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraMintTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

private val responseStyles = listOf(
    "Concise" to "Quick answers",
    "Balanced" to "Useful detail without overwhelming",
    "Detailed" to "More explanation and context"
)

/**
 * 7.5 Voice & AI Settings (Figma "Profile / 05 — Voice & AI Settings", título "VORA Coach").
 * El Figma no tiene botón de guardar: cada ajuste se aplica apenas se cambia.
 */
@Composable
fun VoiceAiSettingsScreen(
    settings: ProfileSettings,
    onSettingsChange: ((ProfileSettings) -> ProfileSettings) -> Unit,
    onPreviewVoiceClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ProfileSubScreen(title = "VORA Coach", onBackClick = onBackClick) {
        ProfileDescription("Choose how VORA communicates and coaches you.")

        ProfileSectionLabel("Coaching style")
        SegmentedOptions(
            options = listOf("Supportive", "Balanced", "Direct"),
            selected = settings.coachingStyle,
            onSelect = { style -> onSettingsChange { it.copy(coachingStyle = style) } }
        )

        ProfileCard {
            Text("ACTIVE VOICE", style = MaterialTheme.typography.labelMedium, color = VoraTextTertiary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(VoraGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "VORA Voice 01",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = VoraTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .clip(VoraPillShape)
                        .border(1.dp, VoraBorder, VoraPillShape)
                        .clickable(onClick = onPreviewVoiceClick)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(painterResource(R.drawable.ic_sparkles), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(12.dp))
                    Text("Preview", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraTextPrimary)
                }
            }
        }

        ProfileSectionLabel("Speaking behaviour")
        ProfileCard {
            ToggleRow("Speak responses aloud", settings.speakResponsesAloud) { on -> onSettingsChange { it.copy(speakResponsesAloud = on) } }
            ToggleRow("Auto-play coaching", settings.autoPlayCoaching) { on -> onSettingsChange { it.copy(autoPlayCoaching = on) } }
            ToggleRow("Allow interruptions", settings.allowInterruptions) { on -> onSettingsChange { it.copy(allowInterruptions = on) } }
            Text("Interrupt VORA anytime while it's speaking.", style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
        }

        ProfileSectionLabel("Response style")
        responseStyles.forEach { (style, description) ->
            SelectableOptionCard(
                title = style,
                subtitle = description,
                selected = style == settings.responseStyle,
                onClick = { onSettingsChange { it.copy(responseStyle = style) } }
            )
        }

        ProfileCard {
            ToggleRow("Voice feedback", settings.voiceFeedback) { on -> onSettingsChange { it.copy(voiceFeedback = on) } }
            ToggleRow("Haptic feedback", settings.hapticFeedback) { on -> onSettingsChange { it.copy(hapticFeedback = on) } }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VoraMintTranslucent)
                .border(1.dp, VoraMintBorderTranslucent, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Your control matters", style = MaterialTheme.typography.titleSmall, color = VoraGreen)
            Text(
                text = "Manage microphone permissions and connected data from Settings.",
                style = MaterialTheme.typography.bodySmall,
                color = VoraTextSecondary
            )
            Text(
                text = "Privacy & permissions",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline
                ),
                color = VoraGreen,
                modifier = Modifier.clickable(onClick = onPrivacyClick)
            )
        }
        Spacer(Modifier.size(4.dp))
    }
}
