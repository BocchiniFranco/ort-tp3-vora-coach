package ar.edu.ort.voracoach.navigation.graphs

import ar.edu.ort.voracoach.ui.screens.workout.PreWorkoutScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination
import ar.edu.ort.voracoach.ui.screens.workout.WeeklyPlanScreen

/**
 * FLUJO 4 - ENTRENAMIENTO (Integrante 2).
 *
 * Cada composable(...) asocia una ruta con su pantalla real y decide a dónde
 * lleva cada botón. Las pantallas no conocen al NavController: solo avisan eventos.
 * Las que siguen con placeholderDestination(...) están pendientes.
 */
fun NavGraphBuilder.workoutGraph(navController: NavHostController) {
    placeholderDestination(Screen.WorkoutDashboard, navController)

    // 4.2 Weekly Plan (demo): plan de la semana con datos de WorkoutDemoData.
    composable(Screen.WeeklyPlan.route) {
        // Día elegido en el selector. rememberSaveable: se conserva si se rota la pantalla.
        var selectedDay by rememberSaveable { mutableIntStateOf(WorkoutDemoData.TODAY_INDEX) }

        WeeklyPlanScreen(
            days = WorkoutDemoData.weekDays,
            selectedDayIndex = selectedDay,
            sessions = WorkoutDemoData.plannedSessions,
            adjustSuggestions = WorkoutDemoData.adjustSuggestions,
            onBackClick = { navController.popBackStack() },
            onDaySelected = { index -> selectedDay = index },
            // Tocar una sesión abre su detalle (4.3) con el id de ese entrenamiento
            onSessionClick = { session ->
                navController.navigate(Screen.WorkoutDetail.createRoute(session.id))
            },
            // Las sugerencias abren el chat con VORA (3.5)
            onSuggestionClick = { navController.navigate(Screen.CoachConversation.route) }
        )
    }

    // 4.3: {workoutId} obligatorio (String) -> GET /v1/workouts/{workoutId}
    placeholderDestination(
        Screen.WorkoutDetail, navController,
        arguments = listOf(navArgument(Screen.ARG_WORKOUT_ID) { type = NavType.StringType })
    )
    // 4.4 Pre Workout (demo): "GET READY" antes de empezar a entrenar.
    composable(Screen.WorkoutPrep.route) {
        PreWorkoutScreen(
            title = WorkoutDemoData.TODAY_WORKOUT_TITLE,
            summary = WorkoutDemoData.TODAY_WORKOUT_SUMMARY,
            notes = WorkoutDemoData.preWorkoutNotes,
            suggestion = WorkoutDemoData.PRE_WORKOUT_SUGGESTION,
            onCloseClick = { navController.popBackStack() },
            // El micrófono abre el asistente de voz (simulado, sin permisos)
            onAskVoraClick = { navController.navigate(Screen.CoachReady.route) },
            // Empieza el entrenamiento activo (4.5)
            onStartWorkoutClick = { navController.navigate(Screen.ActiveWorkout.route) }
        )
    }
    placeholderDestination(Screen.ActiveWorkout, navController)
    placeholderDestination(Screen.ExerciseGuide, navController)
    placeholderDestination(Screen.WorkoutCompleted, navController)
}