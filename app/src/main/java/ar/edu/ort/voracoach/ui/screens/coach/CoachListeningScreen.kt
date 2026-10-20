package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
// LaunchedEffect: ejecuta una corrutina atada a la vida del composable; se cancela al salir.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
// rememberUpdatedState: usa siempre la versión más reciente del callback dentro del efecto.
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOutlinedButton
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import kotlinx.coroutines.delay

/**
 * 3.2 CoachListeningScreen (Demostración): VORA "escucha" al usuario.
 *
 * Simulación (sin micrófono real - NAV05): la transcripción [transcript] aparece letra por
 * letra y, al terminar, se avanza solo a 3.3 Processing. El usuario también puede tocar
 * "Done" para avanzar o "Cancel" para volver.
 *
 * @param onListeningFinished se invoca al terminar la transcripción o al tocar "Done".
 */
@Composable
fun CoachListeningScreen(
    transcript: String,
    onCloseClick: () -> Unit,
    onCancelClick: () -> Unit,
    onListeningFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cantidad de caracteres visibles de la transcripción (estado local de la animación).
    var visibleChars by remember { mutableIntStateOf(0) }
    val currentOnFinished by rememberUpdatedState(onListeningFinished)

    // LaunchedEffect(Unit): corre una sola vez al entrar. Si el usuario sale antes,
    // la corrutina se cancela y NO navega (evita navegaciones "fantasma").
    LaunchedEffect(Unit) {
        delay(600)
        while (visibleChars < transcript.length) {
            delay(45)
            visibleChars++
        }
        delay(1_200)
        currentOnFinished()
    }

    CoachStateLayout(
        modifier = modifier,
        orbState = OrbState.LISTENING,
        statusText = "Listening",
        title = "I'm listening...",
        onCloseClick = onCloseClick,
        content = {
            Text(
                text = if (visibleChars == 0) "Go ahead, ask me anything." else "“${transcript.take(visibleChars)}”",
                style = MaterialTheme.typography.titleLarge,
                fontStyle = if (visibleChars == 0) FontStyle.Normal else FontStyle.Italic,
                color = VoraTextPrimary,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VoraOutlinedButton(text = "Cancel", onClick = onCancelClick, modifier = Modifier.weight(1f))
                VoraPrimaryButton(text = "Done", icon = Icons.Rounded.Stop, onClick = onListeningFinished, modifier = Modifier.weight(1f))
            }
            CoachDemoCaption()
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun CoachListeningPreview() {
    VoraCoachTheme {
        CoachListeningScreen(DemoData.COACH_DEMO_QUESTION, {}, {}, {})
    }
}
