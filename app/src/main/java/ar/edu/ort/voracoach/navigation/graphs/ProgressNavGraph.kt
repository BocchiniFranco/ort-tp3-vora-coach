package ar.edu.ort.voracoach.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination
import ar.edu.ort.voracoach.ui.screens.progress.ProgressDashboardScreen
import ar.edu.ort.voracoach.ui.screens.progress.TodayOverviewScreen

/**
 * FLUJO 6 - PROGRESO (Integrante 2). Pantallas de demostración con métricas simuladas.
 * Cada composable(...) asocia una ruta con su pantalla y decide a dónde va cada botón.
 */
fun NavGraphBuilder.progressGraph(navController: NavHostController) {

    // 6.1 Today's Overview (se abre desde el Home)
    composable(Screen.DailySummary.route) {
        TodayOverviewScreen(
            onBackClick = { navController.popBackStack() },
            onStartWorkoutClick = { navController.navigate(Screen.WorkoutPrep.route) },
            onWeeklyTargetClick = { navController.navigate(Screen.WeeklyPlan.route) }
        )
    }

    // 6.2 Progress Dashboard (pestaña "Progress" de la barra inferior)
    composable(Screen.ProgressDashboard.route) {
        ProgressDashboardScreen(
            onWeeklyReviewClick = { navController.navigate(Screen.WeeklyReview.route) },
            // Cada categoría abre su pantalla de detalle
            onCategoryClick = { category ->
                val destination = when (category.title) {
                    "Workout" -> Screen.WorkoutProgress
                    "Nutrition" -> Screen.NutritionProgress
                    else -> Screen.HealthTrends
                }
                navController.navigate(destination.route)
            },
            // El objetivo activo lleva a "Goals & Preferences" del Perfil (7.3)
            onActiveGoalClick = { navController.navigate(Screen.GoalsPreferences.route) }
        )
    }

    // Pendientes (tanda 2)
    placeholderDestination(Screen.WorkoutProgress, navController)
    placeholderDestination(Screen.NutritionProgress, navController)
    placeholderDestination(Screen.HealthTrends, navController)
    placeholderDestination(Screen.WeeklyReview, navController)
}