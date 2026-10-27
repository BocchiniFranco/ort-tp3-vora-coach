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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * VoraCircularProgress: anillo de avance global ("72% Daily Goal", "67% Calories Consumed").
 *
 * Valores del Figma (lámina "Indicadores" y Home): anillo de 64–72 dp, grosor ≈ 7,5 % del
 * diámetro, avance verde #123D35 sobre pista #E4E9E6 y porcentaje 16 Bold en el centro.
 * Se dibuja con Canvas para controlar exactamente grosor, colores y extremos redondeados.
 *
 * @param progress valor entre 0f y 1f (se recorta si se excede).
 * @param centerText texto del centro (ej. "72%").
 * @param label texto opcional debajo del porcentaje (dentro del anillo).
 * @param strokeWidth grosor; si es null se calcula como 7,5 % del tamaño.
 */
@Composable
fun VoraCircularProgress(
    progress: Float,
    centerText: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    size: Dp = 64.dp,
    strokeWidth: Dp? = null,
    progressColor: Color = VoraGreen,
    trackColor: Color = VoraBorder,
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
    val stroke = strokeWidth ?: (size * 0.075f)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize().padding(stroke / 2)) {
            val style = Stroke(width = stroke.toPx(), cap = StrokeCap.Round)
            // Pista completa (360°).
            drawArc(color = trackColor, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = style)
            // Arco de progreso: arranca arriba (-90°) y recorre progress * 360°.
            drawArc(color = progressColor, startAngle = -90f, sweepAngle = 360f * animated, useCenter = false, style = style)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = centerText,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = if (size >= 120.dp) 28.sp else 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = valueColor
            )
            if (label != null) {
                Text(label, style = MaterialTheme.typography.bodySmall, color = labelColor, textAlign = TextAlign.Center)
            }
        }
    }
}
