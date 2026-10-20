package ar.edu.ort.voracoach.navigation

// NamedNavArgument: argumento de ruta ya configurado (nombre + tipo + default).
import androidx.navigation.NamedNavArgument
// NavGraphBuilder: el "constructor" del grafo; cada flujo le agrega sus destinos.
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.ui.components.VoraBottomDestination
import ar.edu.ort.voracoach.ui.screens.PlaceholderScreen

// =====================================================================================
// Extensiones de navegación COMPARTIDAS por los grafos de los 7 flujos.
// Son "funciones de extensión" de Kotlin: agregan métodos a NavHostController /
// NavGraphBuilder sin heredar de ellos, y se llaman como navController.xxx().
// =====================================================================================

/**
 * Navegación estándar de barra inferior (patrón recomendado por Google):
 *  - popUpTo(Home) con saveState: no se apilan infinitas copias de las pestañas al
 *    cambiar entre ellas. Usamos Home (y no el startDestination) porque Login se quita
 *    de la pila al iniciar sesión: Home es la verdadera raíz mientras el usuario está logueado.
 *  - launchSingleTop: tocar la pestaña actual no crea otra instancia.
 *  - restoreState: al volver a una pestaña se restaura su estado (scroll, etc.).
 */
fun NavHostController.navigateToBottomDestination(destination: VoraBottomDestination) {
    navigate(destination.route()) {
        popUpTo(Screen.HomeDashboard.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Ruta raíz asociada a cada pestaña de la barra inferior. */
fun VoraBottomDestination.route(): String = when (this) {
    VoraBottomDestination.HOME -> Screen.HomeDashboard.route
    VoraBottomDestination.WORKOUT -> Screen.WorkoutDashboard.route
    VoraBottomDestination.NUTRITION -> Screen.NutritionDashboard.route
    VoraBottomDestination.PROGRESS -> Screen.ProgressDashboard.route
    VoraBottomDestination.PROFILE -> Screen.ProfileDashboard.route
}

/**
 * Tras iniciar sesión: va al Home y QUITA Login/Register de la pila
 * (popUpTo Login inclusive). Así el botón Atrás en el Home sale de la app y no vuelve al login.
 */
fun NavHostController.navigateToHomeAfterLogin() {
    navigate(Screen.HomeDashboard.route) {
        popUpTo(Screen.Login.route) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Cierre de sesión (7.6 App Settings): vuelve a Login y VACÍA toda la pila
 * (popUpTo del grafo raíz inclusive). Atrás desde Login sale de la app.
 */
fun NavHostController.navigateToLoginClearingBackStack() {
    navigate(Screen.Login.route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Navega reemplazando la pantalla actual (la saca de la pila). Se usa en estados
 * transitorios del asistente (Listening -> Processing -> Response) para que "Atrás"
 * no vuelva a un estado intermedio que se auto-avanza.
 */
fun NavHostController.navigateReplacing(route: String, current: Screen) {
    navigate(route) {
        popUpTo(current.route) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Cierra el asistente VORA ("X"): vuelve a la pantalla que lo abrió (ej. Home).
 * Si 3.1 Coach Ready está en la pila se quita junto con todo el flujo; si no
 * (ej. se abrió el chat directo desde el Home), simplemente vuelve una pantalla.
 */
fun NavHostController.closeCoach() {
    val popped = popBackStack(Screen.CoachReady.route, inclusive = true)
    if (!popped) popBackStack()
}

/**
 * Registra una ruta mostrando [PlaceholderScreen] mientras su pantalla real no exista.
 * Lo usan los grafos de los flujos que aún no fueron maquetados.
 */
fun NavGraphBuilder.placeholderDestination(
    screen: Screen,
    navController: NavHostController,
    arguments: List<NamedNavArgument> = emptyList()
) {
    composable(route = screen.route, arguments = arguments) {
        PlaceholderScreen(
            screen = screen,
            onNavigate = { route -> navController.navigate(route) },
            onBackClick = navController.backActionOrNull()
        )
    }
}

/**
 * Devuelve la acción "volver" solo si hay una pantalla anterior en la pila;
 * si no, null (la TopBar oculta la flecha).
 */
fun NavHostController.backActionOrNull(): (() -> Unit)? =
    if (previousBackStackEntry != null) ({ popBackStack() }) else null
