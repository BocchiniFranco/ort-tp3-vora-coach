package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import kotlinx.coroutines.delay

/**
 * 3.2 CoachListeningScreen (Demostración) — réplica de "Voice / 02 — Listening".
 *
 * Simulación (sin micrófono real - NAV05): tras una breve espera la transcripción aparece
 * letra por letra y, al terminar, se avanza sola a 3.3 Processing.
 * Tocar el micrófono:
 *  - con transcripción → termina de escuchar y pasa a Processing;
 *  - antes de que haya texto → simula que no se entendió nada y abre 3.6 Error.
 * "Cancel" o "<" vuelven al estado anterior.
 */
@Composable
fun CoachListeningScreen(
    transcript: String,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onListeningFinished: () -> Unit,
    onNothingHeard: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cantidad de caracteres visibles de la transcripción (estado local de la animación).
    var visibleChars by remember { mutableIntStateOf(0) }
    val currentOnFinished by rememberUpdatedState(onListeningFinished)

    // LaunchedEffect(Unit): corre una sola vez al entrar. Si el usuario sale antes,
    // la corrutina se cancela y NO navega (evita navegaciones "fantasma").
    LaunchedEffect(Unit) {
        // Silencio inicial: si el usuario toca el micrófono en esta ventana, "no se oyó nada"
        // y se muestra el estado Error. Es amplia para poder mostrarlo en la demo.
        delay(SILENCE_WINDOW_MS)
        while (visibleChars < transcript.length) {
            delay(40)
            visibleChars++
        }
        delay(1_200)
        currentOnFinished()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraGreen)
            .verticalScroll(rememberScrollState())
    ) {
        VoiceHeader(title = "Listening", onBackClick = onBackClick, dark = true) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                VoiceDot(color = VoraMint, size = 8)
                Text("LIVE AUDIO", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraMint)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            VoraOrbGraphic(state = OrbState.LISTENING, size = 170.dp)
            // Tarjeta translúcida "LIVE TRANSCRIPT".
            val shape = RoundedCornerShape(24.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), shape)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("LIVE TRANSCRIPT", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraMint)
                Text(
                    text = if (visibleChars == 0) "“...”" else "\"${transcript.take(visibleChars).trimEnd('.')}...\"",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium, lineHeight = 27.sp),
                    color = Color.White
                )
            }
            Text(
                "Speak naturally. I'm listening.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
        // ---------------- Micrófono activo + Cancel ----------------
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 56.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(VoraMint)
                    .clickable(onClickLabel = "Stop listening") {
                        if (visibleChars == 0) onNothingHeard() else onListeningFinished()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_mic_large), contentDescription = "Stop listening", tint = VoraGreen, modifier = Modifier.size(28.dp))
            }
            Text(
                "Cancel",
                style = MaterialTheme.typography.titleSmall,
                color = VoraMint,
                modifier = Modifier.clickable(onClick = onCancelClick).padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun CoachListeningPreview() {
    VoraCoachTheme {
        CoachListeningScreen(DemoData.COACH_DEMO_QUESTION, {}, {}, {}, {})
    }
}

/** Milisegundos de silencio antes de que empiece a aparecer la transcripción simulada. */
private const val SILENCE_WINDOW_MS = 2_500L
