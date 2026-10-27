package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.Exercise
import ar.edu.ort.voracoach.model.WorkoutDetail
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraErrorView
import ar.edu.ort.voracoach.ui.components.VoraLoadingView
import ar.edu.ort.voracoach.ui.components.VoraOutlinedButton
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.viewmodel.WorkoutDetailUiState
import ar.edu.ort.voracoach.ui.components.VoraOfflineBanner

/**
 * 4.3 WorkoutDetailScreen (FUNCIONAL: GET /v1/workouts/{workoutId}, RF01 y RF05).
 *
 * Muestra nombre, nivel, duración y ejercicios (series, repeticiones y descanso).
 * Pantalla SIN ESTADO: el estado vive en WorkoutDetailViewModel.
 *
 * @param onExerciseClick tocar un ejercicio abre la guía de ejecución (4.6).
 */
@Composable
fun WorkoutDetailScreen(
    uiState: WorkoutDetailUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onEditWorkoutClick: () -> Unit,
    onExerciseClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { VoraTopBar(title = "Workout Overview", onBack = onBackClick) }

        when (uiState) {
            WorkoutDetailUiState.Loading -> item {
                VoraLoadingView(message = "Loading workout...")
            }

            is WorkoutDetailUiState.Error -> item {
                VoraErrorView(
                    title = "Couldn't load this workout",
                    message = uiState.message,
                    onRetry = onRetryClick
                )
            }

            is WorkoutDetailUiState.Success -> {
                val workout = uiState.workout

                // Aviso si el detalle viene de la caché de Room (RF02)
                if (uiState.isOffline) {
                    item {
                        VoraOfflineBanner(
                            message = "You're offline. Showing the saved version of this workout.",
                            onRetry = onRetryClick,
                            modifier = Modifier.padding(horizontal = VoraScreenPadding)
                        )
                    }
                }

                // Nombre, resumen y músculos trabajados
                item { WorkoutHeader(workout, Modifier.padding(horizontal = VoraScreenPadding)) }

                // Nota del coach con la descripción que manda la API
                if (workout.description.isNotBlank()) {
                    item {
                        VoraTipBanner(
                            title = "VORA COACH NOTE",
                            message = workout.description,
                            modifier = Modifier.padding(horizontal = VoraScreenPadding)
                        )
                    }
                }

                // Lista de ejercicios numerada: itemsIndexed da también la posición
                itemsIndexed(
                    workout.exercises,
                    key = { _, exercise -> exercise.id }) { index, exercise ->
                    ExerciseRow(
                        number = index + 1,
                        exercise = exercise,
                        onClick = { onExerciseClick(exercise) },
                        modifier = Modifier.padding(horizontal = VoraScreenPadding)
                    )
                }

                // Botones de acción
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = VoraScreenPadding),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VoraPrimaryButton(
                            text = "Start Workout",
                            onClick = onStartWorkoutClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                        VoraOutlinedButton(
                            text = "Edit workout",
                            onClick = onEditWorkoutClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/** Título, "25 min • Beginner • 3 exercises" y "Focus: ..." con los músculos. */
@Composable
private fun WorkoutHeader(workout: WorkoutDetail, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(workout.name, style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
        Text(
            "${workout.durationMinutes} min • ${workout.level.replaceFirstChar { it.uppercase() }} • " +
                    "${workout.exercises.size} exercises",
            style = MaterialTheme.typography.bodyMedium,
            color = VoraTextSecondary
        )
        // distinct(): saca los músculos repetidos
        val muscles = workout.exercises.map { it.muscleGroup }.filter { it.isNotBlank() }.distinct()
        if (muscles.isNotEmpty()) {
            Text(
                "Focus: ${muscles.joinToString(" • ")}",
                style = MaterialTheme.typography.bodySmall,
                color = VoraTextSecondary
            )
        }
    }
}

/** Una fila de ejercicio: número, nombre, "3 × 10 • Rest 60s" y flecha. */
@Composable
private fun ExerciseRow(
    number: Int,
    exercise: Exercise,
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
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VoraLightMint),
                contentAlignment = Alignment.Center
            ) {
                // padStart: 1 -> "01"
                Text(
                    number.toString().padStart(2, '0'),
                    style = MaterialTheme.typography.titleSmall,
                    color = VoraDarkGreen
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = VoraTextPrimary
                )
                Text(
                    "${exercise.sets} × ${exercise.reps} • Rest ${exercise.restSeconds}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = "Open exercise guide",
                tint = VoraTextSecondary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun WorkoutDetailPreview() {
    VoraCoachTheme {
        WorkoutDetailScreen(
            uiState = WorkoutDetailUiState.Success(
                WorkoutDetail(
                    id = "w01",
                    name = "Fuerza inicial",
                    level = "beginner",
                    durationMinutes = 25,
                    description = "Rutina de ejemplo para el trabajo práctico.",
                    exercises = listOf(
                        Exercise("e01", "Sentadilla", "Piernas", 3, 10, 60, "", ""),
                        Exercise("e02", "Remo con mancuerna", "Espalda", 3, 10, 60, "", "")
                    )
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onStartWorkoutClick = {},
            onEditWorkoutClick = {},
            onExerciseClick = {}
        )
    }
}