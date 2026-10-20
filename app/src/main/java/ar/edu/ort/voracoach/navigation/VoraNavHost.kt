package ar.edu.ort.voracoach.navigation

// Layout: Box contenedor, fondo y padding provisto por el Scaffold.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
// Scaffold: estructura de pantalla de Material 3 (contenido + bottomBar + insets del sistema).
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
// hiltViewModel(): obtiene un ViewModel @HiltViewModel construido por Hilt.
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
// NavHostController: controla la pila de pantallas (back stack).
import androidx.navigation.NavHostController
// NavHost: contenedor que muestra el destino actual del grafo de navegación.
import androidx.navigation.compose.NavHost
// currentBackStackEntryAsState: observa la pantalla actual para resaltar la BottomBar.
import androidx.navigation.compose.currentBackStackEntryAsState
// rememberNavController: crea y recuerda el NavController a través de recomposiciones.
import androidx.navigation.compose.rememberNavController
// Grafos de cada flujo (un archivo por flujo / integrante).
import ar.edu.ort.voracoach.navigation.graphs.accessGraph
import ar.edu.ort.voracoach.navigation.graphs.coachGraph
import ar.edu.ort.voracoach.navigation.graphs.homeGraph
import ar.edu.ort.voracoach.navigation.graphs.nutritionGraph
import ar.edu.ort.voracoach.navigation.graphs.profileGraph
import ar.edu.ort.voracoach.navigation.graphs.progressGraph
import ar.edu.ort.voracoach.navigation.graphs.workoutGraph
import ar.edu.ort.voracoach.ui.components.VoraBottomBar
import ar.edu.ort.voracoach.ui.components.VoraBottomDestination
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.viewmodel.AuthViewModel

/**
 * VoraNavHost: grafo de navegación COMPLETO de la app (37 rutas, 7 flujos).
 *
 * Responsabilidades:
 *  1. Crear el NavController (único para toda la app).
 *  2. Mostrar la VoraBottomBar SOLO en los 5 destinos raíz (Home, Workout, Nutrition,
 *     Progress, Profile).
 *  3. Delegar el registro de rutas en un grafo por flujo (navigation/graphs/). Cada
 *     integrante edita solo los archivos de sus flujos: menos conflictos en main.
 *
 * Las pantallas nunca reciben el NavController: exponen eventos (lambdas onXClick) y los
 * grafos los traducen en navegación (mismo patrón que AppNavHost del ejemplo de la cátedra).
 *
 * El botón físico "Atrás" lo maneja automáticamente el NavController (NAV04).
 */
@Composable
fun VoraNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Se pide FUERA de los composable(...) para que lo compartan Login, Register y
    // App Settings (cierre de sesión), igual que authViewModel en el ejemplo de la cátedra.
    val authViewModel: AuthViewModel = hiltViewModel()

    // Entrada actual de la pila; "by" desempaqueta el State para recomponer al navegar.
    val backStackEntry by navController.currentBackStackEntryAsState()
    // destination.route devuelve el PATRÓN de la ruta (ej. "workout/detail/{workoutId}").
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in Screen.bottomBarRoutes

    Scaffold(
        modifier = modifier,
        containerColor = VoraBackground,
        bottomBar = {
            if (showBottomBar) {
                VoraBottomBar(
                    selected = currentRoute.toBottomDestination(),
                    onDestinationSelected = { destination ->
                        navController.navigateToBottomDestination(destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        // innerPadding incluye barra de estado, barra de navegación del sistema y BottomBar:
        // aplicarlo evita que el contenido quede tapado (UI03).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoraBackground)
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                // La app siempre arranca en Login (Flujo 1).
                startDestination = Screen.Login.route
            ) {
                accessGraph(navController, authViewModel)     // Flujo 1 - Integrante 1
                homeGraph(navController)                      // Flujo 2 - Integrante 1
                coachGraph(navController)                     // Flujo 3 - Integrante 1
                workoutGraph(navController)                   // Flujo 4 - Integrante 2
                nutritionGraph(navController)                 // Flujo 5 - Integrante 3
                progressGraph(navController)                  // Flujo 6 - Integrante 2
                profileGraph(navController, authViewModel)    // Flujo 7 - Integrante 3
            }
        }
    }
}

/**
 * Convierte la ruta actual en el destino a resaltar en la BottomBar.
 * Función de extensión sobre String?: se lee como currentRoute.toBottomDestination().
 */
private fun String?.toBottomDestination(): VoraBottomDestination? =
    VoraBottomDestination.entries.firstOrNull { it.route() == this }
