package ar.edu.ort.voracoach.ui.components

// API de animaciones infinitas: el orbe "respira" y las ondas de audio oscilan.
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
// scale: agranda/achica el orbe sin recalcular el layout.
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
// sin: función seno para generar alturas ondulantes de las barras de audio.
import kotlin.math.PI
import kotlin.math.sin

/**
 * Estados visuales del orbe del asistente VORA (Flujo 3).
 * Cada estado cambia velocidad, colores y si se muestran las ondas de audio.
 */
enum class OrbState { IDLE, LISTENING, PROCESSING, SPEAKING, ERROR }

/**
 * VoraOrbGraphic: círculo con degradado radial verde/menta y ondas de audio.
 * Representa al asistente VORA Coach en Home, Coach Ready/Listening/Processing/Response.
 *
 * Es 100% gráfico: NO usa el micrófono (NAV05). Las ondas son una animación simulada.
 *
 * @param state estado del asistente; define animación y colores.
 * @param size diámetro del orbe.
 */
@Composable
fun VoraOrbGraphic(
    modifier: Modifier = Modifier,
    state: OrbState = OrbState.IDLE,
    size: Dp = 200.dp
) {
    // rememberInfiniteTransition: crea animaciones que se repiten mientras el orbe esté visible.
    val transition = rememberInfiniteTransition(label = "orb")

    // Pulso de escala ("respiración"): más rápido al escuchar o procesar.
    val pulseDuration = when (state) {
        OrbState.LISTENING -> 700
        OrbState.PROCESSING -> 500
        OrbState.SPEAKING -> 900
        else -> 1800
    }
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(pulseDuration, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    // Fase de la onda: avanza de 0 a 2π en bucle para desplazar las barras de audio.
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "phase"
    )

    // Colores del degradado según estado (rojo suave en error de micrófono).
    val colors = when (state) {
        OrbState.ERROR -> listOf(Color(0xFFF7D6D5), VoraStatusError.copy(alpha = 0.75f), Color(0xFF7A2E2B))
        else -> listOf(VoraAccentLime, VoraMintProgress, VoraDarkGreen)
    }
    val showWaves = state == OrbState.LISTENING || state == OrbState.SPEAKING || state == OrbState.IDLE

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize().scale(pulse)) {
            val radius = this.size.minDimension / 2f
            // Halo exterior traslúcido.
            drawCircle(color = VoraLightMint.copy(alpha = 0.55f), radius = radius)
            // Cuerpo del orbe con degradado radial (centro claro -> borde oscuro).
            drawCircle(
                brush = Brush.radialGradient(
                    colors = colors,
                    center = Offset(center.x - radius * 0.25f, center.y - radius * 0.25f),
                    radius = radius * 1.1f
                ),
                radius = radius * 0.82f
            )
            if (showWaves) {
                // 7 barras verticales cuya altura sigue una onda senoidal desfasada.
                val bars = 7
                val barWidth = radius * 0.07f
                val gap = radius * 0.06f
                val totalWidth = bars * barWidth + (bars - 1) * gap
                val startX = center.x - totalWidth / 2f
                val amplitude = if (state == OrbState.IDLE) 0.12f else 0.32f
                for (i in 0 until bars) {
                    val h = radius * (0.12f + amplitude * ((sin(phase + i * 0.9f) + 1f) / 2f))
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.92f),
                        topLeft = Offset(startX + i * (barWidth + gap), center.y - h / 2f),
                        size = Size(barWidth, h),
                        cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                    )
                }
            } else {
                // PROCESSING / ERROR: tres puntos centrales que "laten".
                val dot = radius * 0.07f
                for (i in -1..1) {
                    val alpha = 0.4f + 0.6f * ((sin(phase + i) + 1f) / 2f)
                    drawCircle(
                        color = Color.White.copy(alpha = alpha),
                        radius = dot,
                        center = Offset(center.x + i * dot * 3f, center.y)
                    )
                }
            }
        }
    }
}
