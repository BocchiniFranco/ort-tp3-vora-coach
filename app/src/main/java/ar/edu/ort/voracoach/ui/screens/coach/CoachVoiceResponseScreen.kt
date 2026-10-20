package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraOutlinedButton
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 3.4 CoachVoiceResponseScreen (Demostración): VORA "habla" la respuesta.
 *
 * Eventos:
 *  - "Log this meal" -> 5.3 Add Food (conecta el asistente con el módulo funcional de Nutrición).
 *  - "Continue in chat" -> 3.5 Conversation.
 *  - "Ask again" -> 3.2 Listening.
 */
@Composable
fun CoachVoiceResponseScreen(
    question: String,
    answer: String,
    onCloseClick: () -> Unit,
    onLogMealClick: () -> Unit,
    onContinueInChatClick: () -> Unit,
    onAskAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CoachStateLayout(
        modifier = modifier,
        orbState = OrbState.SPEAKING,
        statusText = "Speaking",
        title = "Here's my suggestion",
        orbSize = 160.dp,
        onCloseClick = onCloseClick,
        content = {
            Text(
                text = "“$question”",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = VoraTextSecondary,
                textAlign = TextAlign.Center
            )
            VoraCard {
                Text(answer, style = MaterialTheme.typography.bodyLarge, color = VoraTextPrimary)
            }
            VoraTipBanner(message = "Protein-rich dinners help muscle recovery overnight.", title = "Why it matters")
        },
        actions = {
            VoraPrimaryButton(text = "Log this meal", icon = Icons.Rounded.Restaurant, onClick = onLogMealClick)
            VoraOutlinedButton(text = "Continue in chat", icon = Icons.AutoMirrored.Rounded.Chat, onClick = onContinueInChatClick)
            VoraOutlinedButton(text = "Ask again", icon = Icons.Rounded.Mic, onClick = onAskAgainClick)
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7, heightDp = 1100)
@Composable
private fun CoachVoiceResponsePreview() {
    VoraCoachTheme {
        CoachVoiceResponseScreen(DemoData.COACH_DEMO_QUESTION, DemoData.COACH_DEMO_ANSWER, {}, {}, {}, {})
    }
}
