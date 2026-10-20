package ar.edu.ort.voracoach.ui.components

// Canvas: dibujo libre para los gráficos de Progreso (Flujo 6) con datos de demostración.
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTrack

/**
 * Gráfico de barras vertical simple (ej. minutos de entrenamiento por día).
 * La barra en [highlightIndex] se pinta verde oscuro; el resto en menta.
 *
 * @param values valores numéricos (se normalizan contra el máximo).
 * @param labels etiquetas del eje X (misma cantidad que values).
 */
@Composable
fun VoraBarChart(
    values: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    highlightIndex: Int = -1,
    chartHeight: Dp = 140.dp
) {
    // maxOrNull puede ser null si la lista está vacía; usamos 1f para no dividir por cero.
    val max = (values.maxOrNull() ?: 1f).coerceAtLeast(1f)
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(chartHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            values.forEachIndexed { index, value ->
                Box(
                    modifier = Modifier
                        .width(26.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(VoraTrack),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight((value / max).coerceIn(0.02f, 1f))
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (index == highlightIndex) VoraDarkGreen else VoraMintProgress)
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            labels.forEach { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraTextSecondary,
                    modifier = Modifier.width(26.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

/**
 * Gráfico de línea con relleno degradado (tendencias de peso, sueño, frecuencia cardíaca).
 */
@Composable
fun VoraLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = VoraMintProgress,
    chartHeight: Dp = 120.dp
) {
    Canvas(modifier = modifier.fillMaxWidth().height(chartHeight)) {
        if (values.size < 2) return@Canvas
        val max = values.max()
        val min = values.min()
        // Rango mínimo de 1 para evitar división por cero si todos los valores son iguales.
        val range = (max - min).coerceAtLeast(1f)
        val stepX = size.width / (values.size - 1)
        // Convierte cada valor en un punto (x, y); y se invierte porque el Canvas crece hacia abajo.
        val points = values.mapIndexed { i, v ->
            Offset(i * stepX, size.height - ((v - min) / range) * (size.height * 0.8f) - size.height * 0.1f)
        }
        val line = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
        }
        val fill = Path().apply {
            addPath(line)
            lineTo(points.last().x, size.height)
            lineTo(points.first().x, size.height)
            close()
        }
        drawPath(fill, Brush.verticalGradient(listOf(lineColor.copy(alpha = 0.3f), Color.Transparent)))
        drawPath(line, lineColor, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
        points.forEach { drawCircle(Color.White, radius = 4.dp.toPx(), center = it) }
        points.forEach { drawCircle(lineColor, radius = 4.dp.toPx(), center = it, style = Stroke(2.dp.toPx())) }
    }
}
