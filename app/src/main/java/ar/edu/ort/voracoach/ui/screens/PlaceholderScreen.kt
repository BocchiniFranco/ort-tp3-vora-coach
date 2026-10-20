// Paquete de pantallas: cada flujo tendrá su subpaquete (login, home, coach, workout...).
package ar.edu.ort.voracoach.ui.screens

// LazyColumn: lista con scroll que solo compone los elementos visibles (UI03: nada se corta).
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Catálogo tipado de rutas (data objects de la sealed class Screen).
import ar.edu.ort.voracoach.navigation.Screen
// Componentes reutilizables del sistema de diseño VORA.
import ar.edu.ort.voracoach.ui.components.VoraBadge
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraListRow
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
// LocalDate: fecha de hoy para armar la ruta de "Daily Meals" de ejemplo.
import java.time.LocalDate

/**
 * PlaceholderScreen: pantalla TEMPORAL (mismo criterio que el ejemplo de la cátedra).
 * Se elimina cuando todas las rutas tengan su pantalla real.
 *
 * A diferencia de un placeholder que solo muestra texto, este lista las 37 rutas
 * agrupadas por flujo para poder recorrer TODO el grafo desde el primer commit y
 * verificar argumentos tipados y botón "Atrás" (NAV01-NAV04).
 *
 * Patrón "state hoisting" (igual que LoginScreen del ejemplo): la pantalla NO recibe el
 * NavController; solo expone eventos como lambdas y VoraNavHost decide a dónde navegar.
 * Así la pantalla es reutilizable, testeable y no depende de la librería de navegación.
 *
 * @param screen pantalla que representa esta instancia (se muestra su código y título).
 * @param onNavigate evento: el usuario eligió una ruta concreta a la cual ir.
 * @param onBackClick evento "volver"; null si no hay pantalla anterior en la pila.
 */
@Composable
fun PlaceholderScreen(
    screen: Screen,
    onNavigate: (route: String) -> Unit,
    onBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            VoraTopBar(title = "${screen.code} ${screen.title}", onBack = onBackClick)
        }
        item {
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VoraBadge(text = "Flow ${screen.flow.number} · ${screen.flow.title}")
                    VoraBadge(text = screen.status.name)
                }
                Text(
                    text = "Route: ${screen.route}",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
            }
        }
        // groupBy: agrupa las 37 pantallas por flujo para listarlas en 7 secciones.
        Screen.all.groupBy { it.flow }.forEach { (flow, screens) ->
            item(key = flow.name) {
                Text(
                    text = "${flow.number}. ${flow.title}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = VoraScreenPadding, vertical = 4.dp)
                )
            }
            items(screens, key = { it.route }) { target ->
                VoraListRow(
                    title = "${target.code} ${target.title}",
                    subtitle = target.route,
                    modifier = Modifier.padding(horizontal = VoraScreenPadding),
                    onClick = { onNavigate(target.sampleRoute()) }
                )
            }
        }
    }
}

/**
 * Devuelve una ruta navegable concreta: las rutas con argumentos se completan con
 * valores de ejemplo (en las pantallas reales los argumentos vienen de los datos).
 */
private fun Screen.sampleRoute(): String = when (this) {
    Screen.WorkoutDetail -> Screen.WorkoutDetail.createRoute("demo-workout")
    Screen.DailyMeals -> Screen.DailyMeals.createRoute(LocalDate.now().toString())
    Screen.AddFood -> Screen.AddFood.createRoute()
    Screen.MealDetail -> Screen.MealDetail.createRoute(1L)
    else -> route
}
