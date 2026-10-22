// Paquete de componentes reutilizables: piezas de UI compartidas por las 37 pantallas.
package ar.edu.ort.voracoach.ui.components

// @DrawableRes: indica que el Int es el id de un recurso drawable (ícono del Figma).
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
// @Composable: función que emite UI.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
// Colores de la paleta VORA.
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/**
 * Los 5 destinos FIJOS de la barra inferior, en el orden exacto de Figma.
 * Usamos un enum porque la cantidad de destinos es cerrada y conocida en compilación.
 *
 * @property label texto en inglés que muestra la barra.
 * @property iconRes ícono exportado del Figma (Lucide: home, dumbbell, apple, chart-line, user).
 */
enum class VoraBottomDestination(val label: String, @param:DrawableRes val iconRes: Int) {
    HOME("Home", R.drawable.ic_nav_home),
    WORKOUT("Workout", R.drawable.ic_nav_dumbbell),
    NUTRITION("Nutrition", R.drawable.ic_nav_apple),
    PROGRESS("Progress", R.drawable.ic_nav_chart),
    PROFILE("Profile", R.drawable.ic_nav_user)
}

/**
 * VoraBottomBar: barra de navegación inferior según el Figma ("Navegación"):
 * fondo blanco, borde superior de 1 dp, alto de 80 dp (padding 12 arriba / 16 abajo),
 * íconos de 20 dp y etiquetas de 11 sp. El destino activo usa verde #123D35 SemiBold;
 * los inactivos, texto tenue #9AA39F Medium.
 *
 * Decisión de diseño: el componente NO conoce rutas ni NavController. Solo informa
 * qué destino se tocó mediante [onDestinationSelected]; lo conecta VoraNavHost.
 *
 * @param selected destino actualmente resaltado (null si la pantalla actual no es raíz).
 */
@Composable
fun VoraBottomBar(
    selected: VoraBottomDestination?,
    onDestinationSelected: (VoraBottomDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().background(VoraSurface)) {
        // Borde superior de 1 dp que separa la barra del contenido.
        Box(Modifier.fillMaxWidth().height(1.dp).background(VoraBorder))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // deja libre la barra de gestos/botones del sistema
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // entries: lista de todos los valores del enum en orden de declaración.
            VoraBottomDestination.entries.forEach { destination ->
                val isSelected = destination == selected
                val color = if (isSelected) VoraGreen else VoraTextTertiary
                Column(
                    modifier = Modifier
                        .width(64.dp)
                        // Sin "ripple" para imitar el diseño plano del Figma.
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onDestinationSelected(destination) }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(destination.iconRes),
                        contentDescription = destination.label,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        ),
                        color = color
                    )
                }
            }
        }
    }
}
