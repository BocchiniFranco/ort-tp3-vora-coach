package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraChip
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 3.1 CoachReadyScreen (Demostración): el asistente está listo para escuchar.
 *
 * Eventos:
 *  - Micrófono -> 3.2 Listening (simulado).
 *  - "Type instead" o un chip de sugerencia -> 3.5 Conversation.
 *  - Ícono de micrófono tachado (barra superior) -> 3.6 Mic Error (estado de error del micrófono).
 *  - "X" / Atrás del sistema -> cierra el asistente.
 */
@Composable
fun CoachReadyScreen(
    userFirstName: String,
    suggestions: List<String>,
    onCloseClick: () -> Unit,
    onMicClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    onMicSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CoachStateLayout(
        modifier = modifier,
        orbState = OrbState.IDLE,
        statusText = "AI Online",
        title = "Hi $userFirstName, how can I help you today?",
        onCloseClick = onCloseClick,
        topActions = {
            VoraIconCircleButton(icon = Icons.Rounded.MicOff, contentDescription = "Microphone status", onClick = onMicSettingsClick)
        },
        content = {
            Text(
                "Tap the microphone and ask me anything about your training, nutrition or recovery.",
                style = MaterialTheme.typography.bodyLarge,
                color = VoraTextSecondary,
                textAlign = TextAlign.Center
            )
            // LazyRow: chips desplazables horizontalmente; no se cortan en pantallas angostas (UI03).
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(suggestions) { suggestion ->
                    VoraChip(text = suggestion, selected = false, onClick = { onSuggestionClick(suggestion) })
                }
            }
        },
        actions = {
            CoachMicButton(onClick = onMicClick)
            TextButton(onClick = onTypeInsteadClick) {
                Text("Type instead", color = VoraDarkGreen, style = MaterialTheme.typography.titleSmall)
            }
            CoachDemoCaption()
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun CoachReadyPreview() {
    VoraCoachTheme {
        CoachReadyScreen("Alex", DemoData.coachSuggestions, {}, {}, {}, {}, {})
    }
}
