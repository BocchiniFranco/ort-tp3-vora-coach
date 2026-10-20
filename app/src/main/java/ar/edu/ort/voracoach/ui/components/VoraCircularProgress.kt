package ar.edu.ort.voracoach.ui.components

// animateFloatAsState: anima suavemente el cambio de progreso (ej. al agregar una comida).
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
// Canvas: lienzo para dibujar formas libres (arcos del anillo).
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
// StrokeCap.Round: extremos redondeados del arco, como en Figma.
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTrack

/**
 * VoraCircularProgress: anillo de progreso circular para porcentajes
 * ("72% Daily Goal", "67% Calories Consumed").
 *
 * Se dibuja con Canvas en lugar de CircularProgressIndicator para controlar exactamente
 * grosor, colores, extremos redondeados y el texto central (fidelidad UI01).
 *
 * @param progress valor entre 0f y 1f (se recorta si se excede).
 * @param centerText texto grande del centro (ej. "72%").
 * @param label texto pequeño debajo (ej. "Daily Goal").
 * @param valueColor color del texto central (blanco si el anillo va sobre una Hero Card).
 */
@Composable
fun VoraCircularProgress(
    progress: Float,
    centerText: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    size: Dp = 140.dp,
    strokeWidth: Dp = 12.dp,
    progressColor: Color = VoraMintProgress,
    trackColor: Color = VoraTrack,
    valueColor: Color = VoraTextPrimary,
    labelColor: Color = VoraTextSecondary
) {
    // coerceIn evita dibujar arcos negativos o de más de 360°.
    val target = progress.coerceIn(0f, 1f)
    // "by" delega en el State animado: cada frame recompone con el valor intermedio.
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 800),
        label = "voraCircularProgress"
    )
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize().padding(strokeWidth / 2)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            // Pista gris completa (360°).
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            // Arco de progreso: arranca arriba (-90°) y recorre progress * 360°.
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                style = stroke
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = centerText,
                style = if (size >= 120.dp) MaterialTheme.typography.headlineMedium
                else MaterialTheme.typography.titleMedium,
                color = valueColor
            )
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = labelColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
