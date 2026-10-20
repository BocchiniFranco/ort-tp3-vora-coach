package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraOutlinedButton
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import kotlinx.coroutines.delay

/**
 * 3.3 CoachProcessingScreen (Demostración): VORA "piensa" la respuesta.
 *
 * Marca los pasos de [steps] uno por uno y, al completar todos, avanza solo a
 * 3.4 Voice Response mediante [onProcessingFinished]. "Cancel" vuelve atrás.
 */
@Composable
fun CoachProcessingScreen(
    question: String,
    steps: List<String>,
    onCloseClick: () -> Unit,
    onCancelClick: () -> Unit,
    onProcessingFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var completedSteps by remember { mutableIntStateOf(0) }
    val currentOnFinished by rememberUpdatedState(onProcessingFinished)

    LaunchedEffect(Unit) {
        while (completedSteps < steps.size) {
            delay(850)
            completedSteps++
        }
        delay(500)
        currentOnFinished()
    }

    CoachStateLayout(
        modifier = modifier,
        orbState = OrbState.PROCESSING,
        statusText = "Thinking",
        title = "Working on it...",
        orbSize = 180.dp,
        onCloseClick = onCloseClick,
        content = {
            Text(
                text = "“$question”",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = VoraTextSecondary,
                textAlign = TextAlign.Center
            )
            VoraCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    // forEachIndexed: necesitamos el índice para saber si el paso ya terminó.
                    steps.forEachIndexed { index, step ->
                        CoachStepRow(text = step, done = index < completedSteps)
                    }
                }
            }
        },
        actions = {
            VoraOutlinedButton(text = "Cancel", onClick = onCancelClick)
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun CoachProcessingPreview() {
    VoraCoachTheme {
        CoachProcessingScreen(DemoData.COACH_DEMO_QUESTION, DemoData.coachProcessingSteps, {}, {}, {})
    }
}
