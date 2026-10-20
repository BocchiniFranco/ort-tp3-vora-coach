// Paquete de componentes reutilizables: piezas de UI compartidas por las 37 pantallas.
package ar.edu.ort.voracoach.ui.components

// Íconos redondeados de Material (librería material-icons-extended).
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Restaurant
// Componentes de Material 3 para la barra inferior.
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
// @Composable: función que emite UI.
import androidx.compose.runtime.Composable
// Modifier: cadena de decoradores (tamaño, padding, fondo, clicks...).
import androidx.compose.ui.Modifier
// ImageVector: representación vectorial de un ícono.
import androidx.compose.ui.graphics.vector.ImageVector
// dp: unidad de medida independiente de la densidad de pantalla.
import androidx.compose.ui.unit.dp
// Colores de la paleta VORA.
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * Los 5 destinos FIJOS de la barra inferior, en el orden exacto de Figma.
 * Usamos un enum porque la cantidad de destinos es cerrada y conocida en compilación.
 *
 * @property label texto en inglés que muestra la barra.
 * @property icon ícono de Material asociado.
 */
enum class VoraBottomDestination(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Rounded.Home),
    WORKOUT("Workout", Icons.Rounded.FitnessCenter),
    NUTRITION("Nutrition", Icons.Rounded.Restaurant),
    PROGRESS("Progress", Icons.Rounded.Insights),
    PROFILE("Profile", Icons.Rounded.Person)
}

/**
 * VoraBottomBar: barra de navegación inferior blanca con los 5 destinos principales.
 *
 * Decisión de diseño: el componente NO conoce rutas ni NavController. Solo informa
 * qué destino se tocó mediante [onDestinationSelected]. Así queda desacoplado de la
 * navegación (lo conecta VoraNavHost) y es reutilizable y fácil de previsualizar.
 *
 * @param selected destino actualmente resaltado (null si la pantalla actual no es raíz).
 * @param onDestinationSelected callback con el destino elegido por el usuario.
 */
@Composable
fun VoraBottomBar(
    selected: VoraBottomDestination?,
    onDestinationSelected: (VoraBottomDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = VoraSurface,
        // Elevación tonal 0: el diseño usa una barra blanca plana, sin tinte de color.
        tonalElevation = 0.dp
    ) {
        // entries: lista de todos los valores del enum en orden de declaración.
        VoraBottomDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination == selected,
                onClick = { onDestinationSelected(destination) },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VoraDarkGreen,
                    selectedTextColor = VoraDarkGreen,
                    indicatorColor = VoraLightMint,
                    unselectedIconColor = VoraTextSecondary,
                    unselectedTextColor = VoraTextSecondary
                )
            )
        }
    }
}
