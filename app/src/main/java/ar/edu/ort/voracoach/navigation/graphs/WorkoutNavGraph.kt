package ar.edu.ort.voracoach.navigation.graphs

import ar.edu.ort.voracoach.ui.screens.workout.WorkoutDetailScreen
import ar.edu.ort.voracoach.viewmodel.WorkoutDetailViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.edu.ort.voracoach.ui.screens.workout.WorkoutDashboardScreen
import ar.edu.ort.voracoach.viewmodel.WorkoutViewModel
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
import androidx.compose.runtime.LaunchedEffect
import ar.edu.ort.voracoach.ui.screens.workout.ActiveWorkoutScreen
import ar.edu.ort.voracoach.viewmodel.ActiveWorkoutViewModel

/**
 * FLUJO 4 - ENTRENAMIENTO (Integrante 2).
 *
 * Cada composable(...) asocia una ruta con su pantalla real y decide a dónde
 * lleva cada botón. Las pantallas no conocen al NavController: solo avisan eventos.
 * Las que siguen con placeholderDestination(...) están pendientes.
 */
fun NavGraphBuilder.workoutGraph(navController: NavHostController) {
    // 4.1 Workout Dashboard (FUNCIONAL): catálogo real con Retrofit (RF01, RF05).
    composable(Screen.WorkoutDashboard.route) {
        // hiltViewModel(): Hilt crea el ViewModel (con su repositorio) y lo mantiene
        // vivo mientras esta pantalla esté en la pila: al rotar NO se vuelve a crear (RF07).
        val viewModel: WorkoutViewModel = hiltViewModel()
        // collectAsStateWithLifecycle(): observa el StateFlow y redibuja cuando cambia
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val scenario by viewModel.scenario.collectAsStateWithLifecycle()
        val slowMode by viewModel.slowMode.collectAsStateWithLifecycle()

        WorkoutDashboardScreen(
            uiState = uiState,
            selectedScenario = scenario,
            slowMode = slowMode,
            onWeeklyPlanClick = { navController.navigate(Screen.WeeklyPlan.route) },
            onStartWorkoutClick = { navController.navigate(Screen.WorkoutPrep.route) },
            onTodayDetailsClick = { navController.navigate(Screen.WeeklyPlan.route) },
            // NAV04: cada entrenamiento abre SU detalle, con su propio id
            onWorkoutClick = { workoutId ->
                navController.navigate(Screen.WorkoutDetail.createRoute(workoutId))
            },
            onViewProgressClick = { navController.navigate(Screen.WorkoutProgress.route) },
            onRetryClick = viewModel::retry,
            onScenarioSelected = viewModel::onScenarioSelected,
            onSlowModeChanged = viewModel::onSlowModeChanged
        )
    }

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

    // 4.3 Workout Detail (FUNCIONAL): GET /v1/workouts/{workoutId} (RF01, NAV04).
    composable(
        route = Screen.WorkoutDetail.route,
        // El argumento {workoutId} es obligatorio y es texto. Llega al ViewModel por SavedStateHandle.
        arguments = listOf(navArgument(Screen.ARG_WORKOUT_ID) { type = NavType.StringType })
    ) {
        val viewModel: WorkoutDetailViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        WorkoutDetailScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onRetryClick = viewModel::retry,
            onStartWorkoutClick = { navController.navigate(Screen.WorkoutPrep.route) },
            // "Edit workout": se ajusta hablando con VORA (simulado)
            onEditWorkoutClick = { navController.navigate(Screen.CoachConversation.route) },
            // Cada ejercicio abre la guía de ejecución (4.6)
            onExerciseClick = { navController.navigate(Screen.ExerciseGuide.route) }
        )
    }
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
    // 4.5 Active Workout (demo SIMULADA): series, peso y cronómetro de descanso.
    composable(Screen.ActiveWorkout.route) {
        val viewModel: ActiveWorkoutViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // Cuando el ViewModel marca isFinished, navega a 4.7 UNA sola vez.
        // popUpTo inclusive: "Atrás" desde 4.7 no vuelve al entrenamiento terminado.
        LaunchedEffect(uiState.isFinished) {
            if (uiState.isFinished) {
                viewModel.onFinishHandled()
                navController.navigate(Screen.WorkoutCompleted.route) {
                    popUpTo(Screen.ActiveWorkout.route) { inclusive = true }
                }
            }
        }

        ActiveWorkoutScreen(
            uiState = uiState,
            onFinishWorkoutClick = viewModel::finishWorkout,
            onDecreaseWeightClick = viewModel::decreaseWeight,
            onIncreaseWeightClick = viewModel::increaseWeight,
            onAddRestClick = viewModel::addRestTime,
            onSkipRestClick = viewModel::skipRest,
            onCompleteSetClick = viewModel::completeSet
        )
    }
    placeholderDestination(Screen.ExerciseGuide, navController)
    placeholderDestination(Screen.WorkoutCompleted, navController)
}