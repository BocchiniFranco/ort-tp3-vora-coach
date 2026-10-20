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
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTrack
// roundToInt: redondea Double a Int para mostrar gramos enteros.
import kotlin.math.roundToInt

/**
 * VoraMacroProgressBar: barra horizontal de un macronutriente con etiqueta y gramos.
 * Ejemplos de Figma: "Protein 104g / 140g", "Carbs 152g / 220g", "Fat 48g / 65g".
 *
 * En Nutrición (RF04) recibe los totales calculados desde Room, por lo que la barra
 * se actualiza sola al agregar o borrar una comida.
 *
 * @param label nombre del macro ("Protein").
 * @param current gramos consumidos.
 * @param goal objetivo diario en gramos (si es 0 la barra queda vacía, sin dividir por cero).
 */
@Composable
fun VoraMacroProgressBar(
    label: String,
    current: Double,
    goal: Double,
    modifier: Modifier = Modifier,
    unit: String = "g",
    barColor: Color = VoraMintProgress,
    trackColor: Color = VoraTrack,
    labelColor: Color = VoraTextPrimary,
    valueColor: Color = VoraTextSecondary
) {
    // Protección contra división por cero si el objetivo no está configurado.
    val fraction = if (goal > 0) (current / goal).toFloat().coerceIn(0f, 1f) else 0f
    val animated by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(700),
        label = "macroBar_$label"
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.titleSmall, color = labelColor)
            Text(
                text = "${current.roundToInt()}$unit / ${goal.roundToInt()}$unit",
                style = MaterialTheme.typography.bodySmall,
                color = valueColor
            )
        }
        // Pista + relleno proporcional (fillMaxWidth(fraction)).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animated)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(barColor)
            )
        }
    }
}
