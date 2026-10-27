package ar.edu.ort.voracoach.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.ui.screens.progress.HealthTrendsScreen
import ar.edu.ort.voracoach.ui.screens.progress.NutritionProgressScreen
import ar.edu.ort.voracoach.ui.screens.progress.ProgressDashboardScreen
import ar.edu.ort.voracoach.ui.screens.progress.TodayOverviewScreen
import ar.edu.ort.voracoach.ui.screens.progress.WeeklyReviewScreen
import ar.edu.ort.voracoach.ui.screens.progress.WorkoutProgressScreen

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

    // 6.3 Workout Progress: cada ejercicio abre la guía de ejecución (4.6)
    composable(Screen.WorkoutProgress.route) {
        WorkoutProgressScreen(
            onBackClick = { navController.popBackStack() },
            onExerciseClick = { navController.navigate(Screen.ExerciseGuide.route) }
        )
    }

    // 6.4 Nutrition Progress
    composable(Screen.NutritionProgress.route) {
        NutritionProgressScreen(onBackClick = { navController.popBackStack() })
    }

    // 6.5 Health Trends (también se abre desde el Home)
    composable(Screen.HealthTrends.route) {
        HealthTrendsScreen(onBackClick = { navController.popBackStack() })
    }

    // 6.6 Weekly Review
    composable(Screen.WeeklyReview.route) {
        WeeklyReviewScreen(
            onBackClick = { navController.popBackStack() },
            // "Get dinner ideas": abre el alta de comida con la cena ya elegida (5.3)
            onImproveClick = { navController.navigate(Screen.AddFood.createRoute(mealType = "DINNER")) },
            onNextWeekPlanClick = { navController.navigate(Screen.WeeklyPlan.route) },
            onAskVoraClick = { navController.navigate(Screen.CoachReady.route) },
            onDoneClick = { navController.popBackStack() }
        )
    }
}