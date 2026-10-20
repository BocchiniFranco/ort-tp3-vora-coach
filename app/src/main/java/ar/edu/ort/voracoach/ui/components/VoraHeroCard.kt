package ar.edu.ort.voracoach.ui.components

// Layouts básicos de Compose: Box (apilar), Column (vertical), Row (horizontal), Spacer.
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// clip: recorta el contenido a una forma (esquinas redondeadas).
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen

/**
 * VoraHeroCard: tarjeta destacada verde oscuro (#103B31) con esquinas de 24 dp.
 * Se usa en Home ("VORA Coach / AI ONLINE"), Workout ("READY FOR TODAY?") y
 * Nutrition ("Tell Vora what you ate").
 *
 * Recibe un "slot" de contenido ([content]) con receptor ColumnScope: cada pantalla
 * decide qué textos y botones poner adentro, pero el estilo queda unificado (UI02).
 *
 * @param onClick si no es null, toda la tarjeta es clickeable (NAV01).
 */
@Composable
fun VoraHeroCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    // Modifier condicional: solo agrega clickable si hay acción.
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(VoraDarkGreen)
            .then(clickModifier)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

/**
 * Etiqueta superior de las Hero Cards (ej. "AI ONLINE", "READY FOR TODAY?").
 * Muestra un punto lima opcional que simula el indicador "en línea".
 */
@Composable
fun VoraHeroLabel(text: String, showDot: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (showDot) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(VoraAccentLime)
            )
            androidx.compose.foundation.layout.Spacer(Modifier.size(6.dp))
        }
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = VoraAccentLime
        )
    }
}

/**
 * Píldora secundaria dentro de una Hero Card (fondo #225046), p. ej. "45 min" o "Intermediate".
 */
@Composable
fun VoraHeroPill(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(VoraSecondaryGreen)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
