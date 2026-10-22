package ar.edu.ort.voracoach.ui.components

// Layouts básicos de Compose: Box (apilar), Column (vertical), Row (horizontal), Spacer.
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint

/**
 * VoraHeroCard: panel destacado verde (#123D35) con radio de 20 dp (patrón "Tarjetas y
 * paneles" del Figma). Se usa en Home ("VORA Coach / AI ONLINE"), Workout ("READY FOR TODAY?")
 * y Nutrition ("Tell Vora what you ate").
 *
 * Recibe un "slot" de contenido ([content]) con receptor ColumnScope: cada pantalla decide
 * qué textos y botones poner adentro, pero el estilo queda unificado (UI02).
 *
 * @param contentPadding 20 dp por defecto (Workout); el Home usa 16 dp.
 * @param onClick si no es null, toda la tarjeta es clickeable (NAV01).
 */
@Composable
fun VoraHeroCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    // Modifier condicional: solo agrega clickable si hay acción.
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraGreen)
            .then(clickModifier)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

/**
 * Etiqueta superior de los paneles (ej. "READY FOR TODAY?"): 12 Bold en menta (#65D6B0).
 * Con [showDot] agrega un punto lima que indica "en línea".
 */
@Composable
fun VoraHeroLabel(text: String, showDot: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (showDot) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(VoraLime)
            )
            Spacer(Modifier.size(6.dp))
        }
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
            color = VoraMint
        )
    }
}

/**
 * Píldora sobre panel oscuro (ej. "AI ONLINE", "45 min"): fondo blanco al 12 %,
 * texto blanco 11 SemiBold, padding 10/4.
 */
@Composable
fun VoraHeroPill(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = Color.White,
        modifier = modifier
            .clip(VoraPillShape)
            .background(Color.White.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
