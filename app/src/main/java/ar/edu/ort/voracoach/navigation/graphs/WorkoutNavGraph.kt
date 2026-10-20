package ar.edu.ort.voracoach.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination

/**
 * FLUJO 4 - ENTRENAMIENTO (Integrante 2).
 *
 * PENDIENTE (Integrante 2): reemplazar cada placeholderDestination(...) por un
 * composable(...) con la pantalla real, conectando WorkoutViewModel (Retrofit + Room).
 * Los argumentos ya están definidos y tipados.
 */
fun NavGraphBuilder.workoutGraph(navController: NavHostController) {
    placeholderDestination(Screen.WorkoutDashboard, navController)
    placeholderDestination(Screen.WeeklyPlan, navController)
    // 4.3: {workoutId} obligatorio (String) -> GET /v1/workouts/{workoutId}
    placeholderDestination(
        Screen.WorkoutDetail, navController,
        arguments = listOf(navArgument(Screen.ARG_WORKOUT_ID) { type = NavType.StringType })
    )
    placeholderDestination(Screen.WorkoutPrep, navController)
    placeholderDestination(Screen.ActiveWorkout, navController)
    placeholderDestination(Screen.ExerciseGuide, navController)
    placeholderDestination(Screen.WorkoutCompleted, navController)
}
