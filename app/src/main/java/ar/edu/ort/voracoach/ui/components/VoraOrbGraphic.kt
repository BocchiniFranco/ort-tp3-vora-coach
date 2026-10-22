package ar.edu.ort.voracoach.ui.components

// API de animaciones infinitas: el orbe "respira", la onda se expande y el anillo gira.
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/**
 * Estados visuales del asistente VORA (Flujo 3 del Figma: Idle, Listening, Processing,
 * Speaking y Error). Cada estado cambia brillo, onda, animación e ícono central.
 */
enum class OrbState { IDLE, LISTENING, PROCESSING, SPEAKING, ERROR }

/**
 * VoraOrbGraphic: orbe del asistente, reconstruido en capas a partir de los SVG del Figma.
 *
 * Proporciones relativas al tamaño [size] (= diámetro del brillo exterior):
 *  - Brillo ("orb-glow"): degradado radial menta → verde, con opacidad según el estado.
 *  - Onda ("orb-ripple"): aro menta al 30 % de 1,15× el tamaño (solo Listening y Speaking).
 *  - Anillo ("orb-ring"): 0,9× el tamaño, grosor 6 %, degradado lima → menta → verde al 80 %.
 *  - Núcleo ("orb-core"): 0,75× el tamaño, degradado lineal menta → verde.
 *  - Ícono central: ondas de audio (o "reintentar" en el estado de error).
 * En Error todo pasa a la escala de grises del Figma.
 *
 * Es 100 % gráfico: NO usa el micrófono (NAV05). La animación es una simulación.
 *
 * @param showIcon muestra el ícono central; por defecto solo en orbes de 64 dp o más
 *                 (los orbes chicos del Home y del chat no tienen ícono).
 * @param haloColor se conserva por compatibilidad; ya no se usa (el brillo sale del Figma).
 */
@Composable
fun VoraOrbGraphic(
    modifier: Modifier = Modifier,
    state: OrbState = OrbState.IDLE,
    size: Dp = 140.dp,
    @Suppress("UNUSED_PARAMETER") haloColor: Color = Color.Unspecified,
    showIcon: Boolean = size >= 64.dp
) {
    // rememberInfiniteTransition: animaciones que se repiten mientras el orbe esté visible.
    val transition = rememberInfiniteTransition(label = "orb")
    // Pulso suave del brillo (más rápido al escuchar).
    val pulse by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(if (state == OrbState.LISTENING) 700 else 1600, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )
    // Expansión de la onda exterior (Listening / Speaking).
    val ripple by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            tween(if (state == OrbState.SPEAKING) 1100 else 800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "ripple"
    )
    // Giro del anillo mientras VORA "piensa" (Processing).
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation"
    )

    val isError = state == OrbState.ERROR
    // Opacidad del brillo por estado (valores de cada frame del Figma).
    val glowAlpha = when (state) {
        OrbState.IDLE -> 0.25f
        OrbState.LISTENING -> 0.45f
        OrbState.PROCESSING -> 0.35f
        OrbState.SPEAKING -> 0.40f
        OrbState.ERROR -> 0.10f
    }

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val diameter = this.size.minDimension
            val c = center
            // 1) Brillo exterior.
            drawCircle(
                brush = Brush.radialGradient(
                    colors = if (isError) listOf(VoraTextTertiary, VoraGreen) else listOf(VoraMint, VoraGreen),
                    center = c,
                    radius = diameter / 2f
                ),
                radius = diameter / 2f * if (state == OrbState.IDLE) pulse else 1f,
                alpha = glowAlpha
            )
            // 2) Onda exterior (Listening / Speaking).
            if (state == OrbState.LISTENING || state == OrbState.SPEAKING) {
                val rippleDiameter = diameter * 1.15f * ripple
                drawCircle(
                    color = VoraMint.copy(alpha = 0.3f),
                    radius = rippleDiameter / 2f,
                    center = c,
                    style = Stroke(width = rippleDiameter * 0.02f)
                )
            }
            // 3) Anillo con degradado horizontal lima → menta → verde (gira en Processing).
            val ringDiameter = diameter * 0.9f
            val ringStroke = ringDiameter * 0.06f
            rotate(if (state == OrbState.PROCESSING) rotation else 0f, pivot = c) {
                drawRing(
                    diameter = ringDiameter,
                    stroke = ringStroke,
                    colors = if (isError) listOf(VoraBorder, VoraTextTertiary, VoraGreen) else listOf(VoraLime, VoraMint, VoraGreen)
                )
            }
            // 4) Núcleo con degradado lineal (de arriba-izquierda a la derecha).
            val coreRadius = diameter * 0.75f / 2f
            val left = c.x - coreRadius
            val top = c.y - coreRadius
            drawCircle(
                brush = Brush.linearGradient(
                    colors = if (isError) listOf(VoraTextTertiary, Color(0xFF4A5250)) else listOf(VoraMint, VoraGreen),
                    start = Offset(left + coreRadius * 2 * 0.2f, top),
                    end = Offset(left + coreRadius * 2 * 0.969f, top + coreRadius * 2 * 0.154f)
                ),
                radius = coreRadius,
                center = c
            )
        }
        // 5) Ícono central (vector exportado del Figma).
        if (showIcon) {
            Icon(
                painter = painterResource(if (isError) R.drawable.ic_refresh else R.drawable.ic_voice_wave),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * if (isError) 0.283f else 0.2f)
            )
        }
    }
}

/** Dibuja el anillo del orbe como un trazo circular con degradado horizontal al 80 %. */
private fun DrawScope.drawRing(diameter: Float, stroke: Float, colors: List<Color>) {
    val radius = diameter / 2f - stroke / 2f
    drawCircle(
        brush = Brush.horizontalGradient(
            colors = colors,
            startX = center.x - diameter / 2f,
            endX = center.x + diameter / 2f
        ),
        radius = radius,
        center = center,
        style = Stroke(width = stroke),
        alpha = 0.8f
    )
}
