package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.data.remote.MockScenario
import ar.edu.ort.voracoach.model.WeekActivityDay
import ar.edu.ort.voracoach.model.Workout
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraChip
import ar.edu.ort.voracoach.ui.components.VoraEmptyView
import ar.edu.ort.voracoach.ui.components.VoraErrorView
import ar.edu.ort.voracoach.ui.components.VoraHeroCard
import ar.edu.ort.voracoach.ui.components.VoraHeroLabel
import ar.edu.ort.voracoach.ui.components.VoraHeroPill
import ar.edu.ort.voracoach.ui.components.VoraHeroSecondaryButton
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraLoadingView
import ar.edu.ort.voracoach.ui.components.VoraScreenHeader
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraSectionHeader
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTrack
import ar.edu.ort.voracoach.viewmodel.WorkoutsUiState
import ar.edu.ort.voracoach.ui.components.VoraOfflineBanner

/**
 * 4.1 WorkoutDashboardScreen (FUNCIONAL: GET /v1/workouts, RF01 y RF05).
 *
 * Arriba replica el diseño de Figma (tarjeta del día, recomendación, actividad y progreso,
 * con datos de demostración). Abajo muestra el CATÁLOGO REAL que llega de la API,
 * con sus 4 estados: Loading, Success, Empty y Error con reintento.
 * Al final, un panel de pruebas para cambiar el escenario del mock en la defensa.
 *
 * Pantalla SIN ESTADO: el estado vive en WorkoutViewModel y la navegación en el grafo.
 *
 * @param onWorkoutClick recibe el id del entrenamiento tocado: cada uno abre SU detalle (NAV04).
 */
@Composable
fun WorkoutDashboardScreen(
    uiState: WorkoutsUiState,
    selectedScenario: MockScenario,
    slowMode: Boolean,
    onWeeklyPlanClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onTodayDetailsClick: () -> Unit,
    onWorkoutClick: (String) -> Unit,
    onViewProgressClick: () -> Unit,
    onRetryClick: () -> Unit,
    onScenarioSelected: (MockScenario) -> Unit,
    onSlowModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Encabezado: "Workout Goal" chiquito arriba, "Workouts" grande y calendario
        item {
            VoraScreenHeader(title = "Workouts", subtitle = "Workout Goal") {
                VoraIconCircleButton(
                    icon = Icons.Rounded.CalendarMonth,
                    contentDescription = "Weekly plan",
                    onClick = onWeeklyPlanClick
                )
            }
        }

        // Tarjeta oscura "READY FOR TODAY?"
        item {
            TodayWorkoutCard(
                onStartWorkoutClick = onStartWorkoutClick,
                onDetailsClick = onTodayDetailsClick,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Recomendación de VORA
        item {
            VoraTipBanner(
                title = "VORA RECOMMENDS",
                message = WorkoutDemoData.DASHBOARD_RECOMMENDATION,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Actividad de la semana
        item {
            WeekActivityCard(
                days = WorkoutDemoData.weekActivity,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Progreso
        item {
            ProgressCard(
                onViewProgressClick = onViewProgressClick,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // ------------------ CATÁLOGO REAL (Retrofit, RF01 y RF05) ------------------
        item {
            VoraSectionHeader(
                title = "Workout catalog",
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // "when" sobre la sealed interface: Kotlin obliga a contemplar los 4 estados
        when (uiState) {
            WorkoutsUiState.Loading -> item {
                VoraLoadingView(message = "Loading workouts...")
            }

            WorkoutsUiState.Empty -> item {
                VoraEmptyView(
                    title = "No workouts yet",
                    message = "Your coach hasn't published workouts. Pull them again in a moment.",
                    actionText = "Refresh",
                    onAction = onRetryClick
                )
            }

            is WorkoutsUiState.Error -> item {
                VoraErrorView(
                    title = "Couldn't load workouts",
                    message = uiState.message,
                    onRetry = onRetryClick
                )
            }

            is WorkoutsUiState.Success -> {
                // Si los datos vienen de Room porque falló la red, se avisa con reintento (RF02, RF05)
                if (uiState.isOffline) {
                    item {
                        VoraOfflineBanner(
                            message = "You're offline. Showing your saved workouts.",
                            onRetry = onRetryClick,
                            modifier = Modifier.padding(horizontal = VoraScreenPadding)
                        )
                    }
                }
                items(uiState.workouts, key = { it.id }) { workout ->
                    WorkoutCatalogCard(
                        workout = workout,
                        onClick = { onWorkoutClick(workout.id) },
                        modifier = Modifier.padding(horizontal = VoraScreenPadding)
                    )
                }
            }
        }
        // Panel de pruebas del mock (para mostrar RF05 en la defensa)
        item {
            MockScenarioPanel(
                selectedScenario = selectedScenario,
                slowMode = slowMode,
                onScenarioSelected = onScenarioSelected,
                onSlowModeChanged = onSlowModeChanged,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }
    }
}

/** Tarjeta oscura del entrenamiento de hoy (datos de demostración). */
@Composable
private fun TodayWorkoutCard(
    onStartWorkoutClick: () -> Unit,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoraHeroCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraHeroLabel(text = "Ready for today?")
            Spacer(Modifier.weight(1f))
            VoraHeroPill(text = WorkoutDemoData.TODAY_WORKOUT_INTENSITY)
        }
        Text(
            WorkoutDemoData.TODAY_WORKOUT_TITLE,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            WorkoutDemoData.TODAY_WORKOUT_FOCUS,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.75f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VoraLimeButton(
                text = "Start Workout",
                onClick = onStartWorkoutClick,
                modifier = Modifier.weight(1f)
            )
            VoraHeroSecondaryButton(text = "Details", onClick = onDetailsClick)
        }
    }
}

/** Tarjeta "This Week's Activity" con un círculo por día. */
@Composable
private fun WeekActivityCard(days: List<WeekActivityDay>, modifier: Modifier = Modifier) {
    VoraCard(modifier = modifier) {
        Text(
            "This Week's Activity",
            style = MaterialTheme.typography.titleSmall,
            color = VoraTextPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        day.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraTextSecondary
                    )
                    // Color del círculo según el día: hoy, hecho o pendiente
                    val circleColor = when {
                        day.isToday -> VoraDarkGreen
                        day.isDone -> VoraMintProgress
                        else -> VoraTrack
                    }
                    Box(
                        Modifier
                            .padding(top = 6.dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(circleColor)
                    )
                }
            }
        }
    }
}

/** Tarjeta "Your Progress" con racha, entrenamientos y volumen (demostración). */
@Composable
private fun ProgressCard(onViewProgressClick: () -> Unit, modifier: Modifier = Modifier) {
    VoraCard(modifier = modifier) {
        VoraSectionHeader(
            title = "Your Progress",
            actionText = "View progress",
            onAction = onViewProgressClick
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            ProgressStat("STREAK", WorkoutDemoData.PROGRESS_STREAK, Modifier.weight(1f))
            ProgressStat("WORKOUTS", WorkoutDemoData.PROGRESS_WORKOUTS, Modifier.weight(1f))
            ProgressStat("VOL. LIFTED", WorkoutDemoData.PROGRESS_VOLUME, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProgressStat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = VoraTextSecondary)
        Text(value, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary)
    }
}

/** Una fila del catálogo real: ícono, nombre, nivel, duración y cantidad de ejercicios. */
@Composable
private fun WorkoutCatalogCard(
    workout: Workout,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoraCard(modifier = modifier, onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(VoraLightMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.FitnessCenter, contentDescription = null, tint = VoraDarkGreen)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    workout.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = VoraTextPrimary
                )
                Text(
                    // "beginner" -> "Beginner"
                    "${workout.level.replaceFirstChar { it.uppercase() }} • " +
                            "${workout.durationMinutes} min • ${workout.exerciseCount} exercises",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = "Open workout",
                tint = VoraTextSecondary
            )
        }
    }
}

/**
 * Panel de pruebas: elige el escenario del mock (normal, empty, error, malformed)
 * y activa una demora de 3 s para ver el estado Loading (RF05).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MockScenarioPanel(
    selectedScenario: MockScenario,
    slowMode: Boolean,
    onScenarioSelected: (MockScenario) -> Unit,
    onSlowModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    VoraCard(modifier = modifier) {
        Text(
            "Mock server scenario (testing)",
            style = MaterialTheme.typography.titleSmall,
            color = VoraTextPrimary
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // MockScenario.entries = la lista de los 4 valores del enum
            MockScenario.entries.forEach { scenario ->
                VoraChip(
                    text = scenario.headerValue,
                    selected = scenario == selectedScenario,
                    onClick = { onScenarioSelected(scenario) }
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Slow response (3 s delay)",
                style = MaterialTheme.typography.bodyMedium,
                color = VoraTextPrimary,
                modifier = Modifier.weight(1f)
            )
            Switch(checked = slowMode, onCheckedChange = onSlowModeChanged)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun WorkoutDashboardPreview() {
    VoraCoachTheme {
        WorkoutDashboardScreen(
            uiState = WorkoutsUiState.Success(
                listOf(
                    Workout("w-1", "Upper Body Strength", "intermediate", 42, 3),
                    Workout("w-2", "Core Stability", "beginner", 20, 3)
                )
            ),
            selectedScenario = MockScenario.NORMAL,
            slowMode = false,
            onWeeklyPlanClick = {},
            onStartWorkoutClick = {},
            onTodayDetailsClick = {},
            onWorkoutClick = {},
            onViewProgressClick = {},
            onRetryClick = {},
            onScenarioSelected = {},
            onSlowModeChanged = {}
        )
    }
}