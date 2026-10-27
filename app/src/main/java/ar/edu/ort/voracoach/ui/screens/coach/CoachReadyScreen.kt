package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
// FlowRow: los chips de sugerencias pasan a otra línea si no entran (UI03).
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraChip
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/**
 * 3.1 CoachReadyScreen (Demostración) — réplica de "Voice / 01 — Idle".
 *
 * Eventos (NAV01):
 *  - "TAP TO TALK" → 3.2 Listening (simulado, sin micrófono real - NAV05).
 *  - "Type instead", una sugerencia o "LAST CONVERSATION" → 3.5 Conversation.
 *  - "<" / Atrás del sistema → cierra el asistente.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoachReadyScreen(
    suggestions: List<String>,
    lastConversationText: String,
    lastConversationTime: String,
    onBackClick: () -> Unit,
    onMicClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    onLastConversationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
    ) {
        VoiceHeader(title = "VORA Coach", onBackClick = onBackClick, dark = false) {
            VoiceStatusTag(text = "ONLINE", textColor = VoraGreen)
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            VoraOrbGraphic(state = OrbState.IDLE, size = 140.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("How can I help?", style = MaterialTheme.typography.headlineLarge, color = VoraTextPrimary, textAlign = TextAlign.Center)
                Text(
                    "Talk naturally about your workout, nutrition or progress.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.titleSmall.fontSize),
                    color = VoraTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("SUGGESTED QUESTIONS", style = MaterialTheme.typography.labelMedium, color = VoraTextTertiary)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestions.forEach { suggestion ->
                        VoraChip(text = suggestion, selected = false, onClick = { onSuggestionClick(suggestion) })
                    }
                }
            }
            VoraCard(onClick = onLastConversationClick, contentPadding = PaddingValues(14.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("LAST CONVERSATION", style = MaterialTheme.typography.labelMedium, color = VoraTextTertiary)
                        Text(lastConversationTime, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal), color = VoraTextTertiary)
                    }
                    Text(
                        "“$lastConversationText”",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = VoraTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        // ---------------- Bloque del micrófono ----------------
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(72.dp)
                        .shadow(12.dp, CircleShape, ambientColor = VoraMint.copy(alpha = 0.15f), spotColor = VoraMint.copy(alpha = 0.3f))
                        .clip(CircleShape)
                        .background(VoraGreen)
                        .clickable(onClickLabel = "Tap to talk", onClick = onMicClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_mic_large), contentDescription = "Tap to talk", tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Text("TAP TO TALK", style = MaterialTheme.typography.titleSmall, color = VoraGreen)
            }
            Text(
                "Type instead",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize, textDecoration = TextDecoration.Underline),
                color = VoraTextSecondary,
                modifier = Modifier.clickable(onClick = onTypeInsteadClick).padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun CoachReadyPreview() {
    VoraCoachTheme {
        CoachReadyScreen(DemoData.coachSuggestions, DemoData.LAST_CONVERSATION_TEXT, DemoData.LAST_CONVERSATION_TIME, {}, {}, {}, {}, {})
    }
}
