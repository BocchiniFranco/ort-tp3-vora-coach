package ar.edu.ort.voracoach.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
// buildAnnotatedString / withStyle: texto con dos colores ("104g" fuerte + "/ 140g" tenue).
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
// roundToInt: redondea Double a Int para mostrar gramos enteros.
import kotlin.math.roundToInt

/**
 * VoraMacroProgressBar: barra horizontal de un macronutriente (Figma: Home y "Indicadores").
 * Fila superior: etiqueta 12 Medium gris y valor "104g / 140g" (12 SemiBold; el objetivo en
 * texto tenue). Debajo, barra de 6 dp con radio 3 sobre pista #E4E9E6.
 * Colores por macro según el Figma: proteína verde, carbohidratos menta, grasas lima.
 *
 * En Nutrición (RF04) recibe los totales calculados desde Room, por lo que la barra
 * se actualiza sola al agregar o borrar una comida.
 *
 * @param goal objetivo diario en gramos (si es 0 la barra queda vacía, sin dividir por cero).
 */
@Composable
fun VoraMacroProgressBar(
    label: String,
    current: Double,
    goal: Double,
    modifier: Modifier = Modifier,
    unit: String = "g",
    barColor: Color = VoraGreen,
    trackColor: Color = VoraBorder,
    labelColor: Color = VoraTextSecondary,
    valueColor: Color = VoraTextPrimary,
    barHeight: Dp = 6.dp
) {
    // Protección contra división por cero si el objetivo no está configurado.
    val fraction = if (goal > 0) (current / goal).toFloat().coerceIn(0f, 1f) else 0f
    val animated by animateFloatAsState(targetValue = fraction, animationSpec = tween(700), label = "macroBar_$label")
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = labelColor)
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = valueColor)) { append("${current.roundToInt()}$unit ") }
                    withStyle(SpanStyle(color = VoraTextTertiary)) { append("/ ${goal.roundToInt()}$unit") }
                },
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        // Pista + relleno proporcional (fillMaxWidth(fraction)).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(barHeight / 2))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animated)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(barHeight / 2))
                    .background(barColor)
            )
        }
    }
}
