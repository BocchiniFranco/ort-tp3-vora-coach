package ar.edu.ort.voracoach.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination

/**
 * FLUJO 6 - PROGRESO (Integrante 2).
 *
 * PENDIENTE (Integrante 2): reemplazar cada placeholderDestination(...) por la pantalla
 * real con datos de demostración.
 */
fun NavGraphBuilder.progressGraph(navController: NavHostController) {
    placeholderDestination(Screen.DailySummary, navController)
    placeholderDestination(Screen.ProgressDashboard, navController)
    placeholderDestination(Screen.WorkoutProgress, navController)
    placeholderDestination(Screen.NutritionProgress, navController)
    placeholderDestination(Screen.HealthTrends, navController)
    placeholderDestination(Screen.WeeklyReview, navController)
}
