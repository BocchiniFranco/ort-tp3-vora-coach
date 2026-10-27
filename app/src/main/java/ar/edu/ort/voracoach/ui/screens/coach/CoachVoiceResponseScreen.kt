package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint
import kotlinx.coroutines.delay

/**
 * 3.4 CoachVoiceResponseScreen (Demostración) — réplica de "Voice / 04 — Speaking".
 *
 * VORA "lee" la respuesta: la barra avanza segundo a segundo (reproducción simulada, no hay
 * audio real). Controles del Figma:
 *  - Repetir: vuelve a 0:00 · Pausa / reanudar · Stop: termina y abre la conversación (3.5).
 *  - "Tap to Interrupt": corta la respuesta y vuelve a escuchar (3.2).
 * Al terminar la reproducción también se pasa a la conversación ([onFinished]).
 */
@Composable
fun CoachVoiceResponseScreen(
    answer: String,
    durationSeconds: Int,
    onBackClick: () -> Unit,
    onInterruptClick: () -> Unit,
    onStopClick: () -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Segundos reproducidos y estado de reproducción (sobreviven a la rotación).
    var elapsed by rememberSaveable { mutableIntStateOf(0) }
    var isPlaying by rememberSaveable { mutableStateOf(true) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    // Mientras reproduce, avanza un segundo por vez; al llegar al final, termina.
    LaunchedEffect(isPlaying, elapsed) {
        if (!isPlaying) return@LaunchedEffect
        if (elapsed >= durationSeconds) {
            delay(600)
            currentOnFinished()
        } else {
            delay(1_000)
            elapsed++
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraGreen)
            .verticalScroll(rememberScrollState())
    ) {
        VoiceHeader(title = "VORA Speaking", onBackClick = onBackClick, dark = true) {
            VoiceStatusTag(text = "SPEAKING", textColor = VoraMint)
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            VoraOrbGraphic(state = if (isPlaying) OrbState.SPEAKING else OrbState.IDLE, size = 140.dp)
            val shape = RoundedCornerShape(20.dp)
            Text(
                text = "\"$answer\"",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium, lineHeight = 24.sp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), shape)
                    .padding(20.dp)
            )
            // ---------------- Reproductor ----------------
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(elapsed.toFloat() / durationSeconds)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(2.dp))
                            .background(VoraMint)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(formatSeconds(elapsed), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal), color = Color.White.copy(alpha = 0.6f))
                    Text("-${formatSeconds(durationSeconds - elapsed)}", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal), color = Color.White.copy(alpha = 0.6f))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    PlayerButton(R.drawable.ic_repeat_one, "Replay", 40, 18, Color.White.copy(alpha = 0.08f), Color.White) {
                        elapsed = 0
                        isPlaying = true
                    }
                    PlayerButton(
                        if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                        if (isPlaying) "Pause" else "Resume",
                        56, 20, VoraMint, VoraGreen
                    ) { isPlaying = !isPlaying }
                    PlayerButton(R.drawable.ic_stop_circle, "Stop", 40, 18, Color.White.copy(alpha = 0.08f), Color.White, onStopClick)
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tap to Interrupt",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                color = VoraGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(VoraPillShape)
                    .background(VoraLime)
                    .clickable(onClick = onInterruptClick)
                    .padding(horizontal = 32.dp, vertical = 14.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

/** Botón circular del reproductor (repetir, pausa y stop). */
@Composable
private fun PlayerButton(
    iconRes: Int,
    description: String,
    sizeDp: Int,
    iconDp: Int,
    background: Color,
    tint: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(painterResource(iconRes), contentDescription = description, tint = tint, modifier = Modifier.size(iconDp.dp))
    }
}

/** Formatea segundos como "m:ss" (ej. 8 → "0:08"). */
private fun formatSeconds(seconds: Int): String = "%d:%02d".format(seconds / 60, seconds % 60)

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun CoachVoiceResponsePreview() {
    VoraCoachTheme {
        CoachVoiceResponseScreen(DemoData.COACH_DEMO_ANSWER, DemoData.COACH_ANSWER_DURATION_SECONDS, {}, {}, {}, {})
    }
}
