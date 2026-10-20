package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOutlinedButton
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 3.6 CoachMicErrorScreen (Demostración): estado de error del micrófono.
 *
 * Muestra el orbe en rojo y ofrece dos salidas coherentes (NAV02: nunca bloquear al usuario):
 *  - "Try again" -> 3.2 Listening.
 *  - "Type instead" -> 3.5 Conversation.
 * No abre los ajustes del sistema ni pide permisos: la app no declara RECORD_AUDIO (NAV05).
 */
@Composable
fun CoachMicErrorScreen(
    onCloseClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CoachStateLayout(
        modifier = modifier,
        orbState = OrbState.ERROR,
        statusText = "Mic unavailable",
        title = "I can't hear you right now",
        orbSize = 180.dp,
        onCloseClick = onCloseClick,
        content = {
            Text(
                "Your microphone seems to be off or used by another app. Check it and try again, or type your question instead.",
                style = MaterialTheme.typography.bodyLarge,
                color = VoraTextSecondary,
                textAlign = TextAlign.Center
            )
            VoraTipBanner(message = "In this demo the voice assistant is simulated, so no microphone permission is ever requested.")
        },
        actions = {
            VoraPrimaryButton(text = "Try again", icon = Icons.Rounded.Refresh, onClick = onTryAgainClick)
            VoraOutlinedButton(text = "Type instead", icon = Icons.AutoMirrored.Rounded.Chat, onClick = onTypeInsteadClick)
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun CoachMicErrorPreview() {
    VoraCoachTheme {
        CoachMicErrorScreen({}, {}, {})
    }
}
