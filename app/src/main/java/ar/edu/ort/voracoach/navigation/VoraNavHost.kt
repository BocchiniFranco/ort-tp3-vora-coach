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
// NavHostController: controla la pila de pantallas (back stack).
import androidx.navigation.NavHostController
// NavType: tipos de argumentos de navegación (String, Long...).
import androidx.navigation.NavType
// NavHost / composable: definen el grafo de navegación declarativo de Compose.
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// currentBackStackEntryAsState: observa la pantalla actual para resaltar la BottomBar.
import androidx.navigation.compose.currentBackStackEntryAsState
// rememberNavController: crea y recuerda el NavController a través de recomposiciones.
import androidx.navigation.compose.rememberNavController
// navArgument: declara nombre, tipo y valor por defecto de cada argumento de ruta.
import androidx.navigation.navArgument
import ar.edu.ort.voracoach.ui.components.VoraBottomBar
import ar.edu.ort.voracoach.ui.components.VoraBottomDestination
import ar.edu.ort.voracoach.ui.screens.PlaceholderScreen
import ar.edu.ort.voracoach.ui.theme.VoraBackground

/**
 * VoraNavHost: grafo de navegación COMPLETO de la app (37 rutas, 7 flujos).
 *
 * Responsabilidades:
 *  1. Crear el NavController (único para toda la app).
 *  2. Mostrar la VoraBottomBar SOLO en los 5 destinos raíz (Home, Workout, Nutrition,
 *     Progress, Profile).
 *  3. Registrar cada ruta de [Screen] con sus argumentos tipados.
 *  4. Traducir los EVENTOS de cada pantalla (lambdas onXClick) en navegación. Las pantallas
 *     nunca reciben el NavController (mismo patrón que AppNavHost del ejemplo de la cátedra).
 *
 * El botón físico "Atrás" lo maneja automáticamente el NavController (NAV04): saca la
 * pantalla actual de la pila. Por eso siempre navegamos con navigate() / popBackStack().
 *
 * ENTREGA 1: cada ruta muestra [PlaceholderScreen]. En las entregas siguientes cada
 * integrante reemplaza la llamada de SU flujo por la pantalla real.
 */
@Composable
fun VoraNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Entrada actual de la pila; "by" desempaqueta el State para recomponer al navegar.
    val backStackEntry by navController.currentBackStackEntryAsState()
    // destination.route devuelve el PATRÓN de la ruta (ej. "workout/detail/{workoutId}").
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in Screen.bottomBarRoutes

    // Eventos comunes que reciben las pantallas como lambdas.
    val navigateTo: (String) -> Unit = { route -> navController.navigate(route) }
    // Solo hay "volver" si existe una pantalla anterior en la pila.
    val backOrNull: () -> (() -> Unit)? = {
        if (navController.previousBackStackEntry != null) ({ navController.popBackStack() }) else null
    }

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
                // ----------------------------------------------------------------
                // Rutas SIN argumentos: se registran todas con el mismo bloque.
                // filter { ... } excluye las 4 rutas con argumentos, definidas abajo.
                // ----------------------------------------------------------------
                Screen.all
                    .filter { it !in screensWithArguments }
                    .forEach { screen ->
                        composable(route = screen.route) {
                            PlaceholderScreen(screen = screen, onNavigate = navigateTo, onBackClick = backOrNull())
                        }
                    }

                // 4.3 Detalle de entrenamiento: {workoutId} obligatorio (String).
                composable(
                    route = Screen.WorkoutDetail.route,
                    arguments = listOf(navArgument(Screen.ARG_WORKOUT_ID) { type = NavType.StringType })
                ) {
                    PlaceholderScreen(screen = Screen.WorkoutDetail, onNavigate = navigateTo, onBackClick = backOrNull())
                }

                // 5.2 Comidas del día: {date} obligatorio con formato yyyy-MM-dd.
                composable(
                    route = Screen.DailyMeals.route,
                    arguments = listOf(navArgument(Screen.ARG_DATE) { type = NavType.StringType })
                ) {
                    PlaceholderScreen(screen = Screen.DailyMeals, onNavigate = navigateTo, onBackClick = backOrNull())
                }

                // 5.3 Alta de comida: date y mealType OPCIONALES (nullable + defaultValue null).
                composable(
                    route = Screen.AddFood.route,
                    arguments = listOf(
                        navArgument(Screen.ARG_DATE) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                        navArgument(Screen.ARG_MEAL_TYPE) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) {
                    PlaceholderScreen(screen = Screen.AddFood, onNavigate = navigateTo, onBackClick = backOrNull())
                }

                // 5.8 Detalle de comida: {mealId} obligatorio (Long = clave primaria de Room).
                composable(
                    route = Screen.MealDetail.route,
                    arguments = listOf(navArgument(Screen.ARG_MEAL_ID) { type = NavType.LongType })
                ) {
                    PlaceholderScreen(screen = Screen.MealDetail, onNavigate = navigateTo, onBackClick = backOrNull())
                }
            }
        }
    }
}

/** Pantallas cuya ruta lleva argumentos y por eso se registran por separado. */
private val screensWithArguments: Set<Screen>
    get() = setOf(Screen.WorkoutDetail, Screen.DailyMeals, Screen.AddFood, Screen.MealDetail)

/**
 * Convierte la ruta actual en el destino a resaltar en la BottomBar.
 * Función de extensión sobre String?: se lee como currentRoute.toBottomDestination().
 */
private fun String?.toBottomDestination(): VoraBottomDestination? = when (this) {
    Screen.HomeDashboard.route -> VoraBottomDestination.HOME
    Screen.WorkoutDashboard.route -> VoraBottomDestination.WORKOUT
    Screen.NutritionDashboard.route -> VoraBottomDestination.NUTRITION
    Screen.ProgressDashboard.route -> VoraBottomDestination.PROGRESS
    Screen.ProfileDashboard.route -> VoraBottomDestination.PROFILE
    else -> null
}

/**
 * Navegación estándar de barra inferior (patrón recomendado por Google):
 *  - popUpTo(Home) con saveState: no se apilan infinitas copias de las pestañas al
 *    cambiar entre ellas. Usamos Home (y no el startDestination) porque Login se quita
 *    de la pila al iniciar sesión: Home es la verdadera raíz mientras el usuario está logueado.
 *  - launchSingleTop: tocar la pestaña actual no crea otra instancia.
 *  - restoreState: al volver a una pestaña se restaura su estado (scroll, etc.).
 */
fun NavHostController.navigateToBottomDestination(destination: VoraBottomDestination) {
    val route = when (destination) {
        VoraBottomDestination.HOME -> Screen.HomeDashboard.route
        VoraBottomDestination.WORKOUT -> Screen.WorkoutDashboard.route
        VoraBottomDestination.NUTRITION -> Screen.NutritionDashboard.route
        VoraBottomDestination.PROGRESS -> Screen.ProgressDashboard.route
        VoraBottomDestination.PROFILE -> Screen.ProfileDashboard.route
    }
    navigate(route) {
        popUpTo(Screen.HomeDashboard.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
