package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen
import ar.edu.ort.voracoach.viewmodel.ActiveWorkoutUiState

/**
 * 4.5 ActiveWorkoutScreen (Demostración SIMULADA).
 *
 * Muestra el ejercicio actual, el objetivo de series y repeticiones, el peso con +/−,
 * las series hechas, el descanso con cronómetro y un consejo de VORA.
 * Pantalla SIN ESTADO: todo lo calcula ActiveWorkoutViewModel.
 */
@Composable
fun ActiveWorkoutScreen(
    uiState: ActiveWorkoutUiState,
    onFinishWorkoutClick: () -> Unit,
    onDecreaseWeightClick: () -> Unit,
    onIncreaseWeightClick: () -> Unit,
    onAddRestClick: () -> Unit,
    onSkipRestClick: () -> Unit,
    onCompleteSetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraDarkGreen)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = VoraScreenPadding, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Encabezado: "EXERCISE 2 OF 3", nombre y botón "Finish Workout"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "EXERCISE ${uiState.exerciseNumber} OF ${uiState.totalExercises}",
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraMintProgress
                )
                Text(
                    uiState.exerciseName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
            SmallPill(text = "Finish Workout", onClick = onFinishWorkoutClick)
        }

        // Objetivo: repeticiones y matriz de series
        DarkCard {
            Text(
                "CURRENT VOLUME WORK",
                style = MaterialTheme.typography.labelSmall,
                color = VoraMintProgress,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BigStat(value = "${uiState.reps}", label = "REPS TARGET")
                BigStat(value = "${uiState.sets} × ${uiState.reps}", label = "SET MATRIX")
            }
        }

        // Última sesión y peso actual con − y +
        DarkCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "LAST SESSION",
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraMintProgress
                    )
                    Text(
                        uiState.lastSession,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
                CircleIconButton(Icons.Rounded.Remove, "Decrease weight", onDecreaseWeightClick)
                Text(
                    "${formatKg(uiState.weightKg)} kg",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                CircleIconButton(Icons.Rounded.Add, "Increase weight", onIncreaseWeightClick)
            }
        }

        // Series: hechas (con ✓), la actual (borde lima) y las que faltan
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (set in 1..uiState.sets) {
                SetChip(
                    number = set,
                    isDone = set < uiState.currentSet,
                    isCurrent = set == uiState.currentSet,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Descanso: solo aparece si está descansando
        if (uiState.isResting) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(VoraSecondaryGreen)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Resting • ${formatTime(uiState.restSecondsLeft)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = VoraMintProgress,
                    modifier = Modifier.weight(1f)
                )
                SmallPill(text = "+30s", onClick = onAddRestClick)
                SmallPill(text = "Skip", onClick = onSkipRestClick, highlighted = true)
            }
        }

        // Consejo del coach
        DarkCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "VORA COACH",
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraAccentLime,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Voice On",
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraMintProgress
                )
            }
            Text(
                WorkoutDemoData.ACTIVE_WORKOUT_COACH_TIP,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }

        VoraLimeButton(
            text = "Complete Set",
            onClick = onCompleteSetClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Tarjeta verde un poco más clara que el fondo, con los hijos en columna. */
@Composable
private fun DarkCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraSecondaryGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        content()
    }
}

/** Número grande con su etiqueta abajo ("10" / "REPS TARGET"). */
@Composable
private fun BigStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

/** Botón redondo chico con ícono (− y + del peso). */
@Composable
private fun CircleIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoraDarkGreen)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

/** Píldora chica clickeable ("Finish Workout", "+30s", "Skip"). */
@Composable
private fun SmallPill(text: String, onClick: () -> Unit, highlighted: Boolean = false) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = if (highlighted) VoraDarkGreen else Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (highlighted) VoraAccentLime else VoraSecondaryGreen)
            .border(1.dp, VoraMintProgress.copy(alpha = 0.4f), RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

/** Chip de una serie: ✓ si está hecha, borde lima si es la actual. */
@Composable
private fun SetChip(
    number: Int,
    isDone: Boolean,
    isCurrent: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(VoraSecondaryGreen)
            .border(1.5.dp, if (isCurrent) VoraAccentLime else Color.Transparent, shape)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDone) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = "Done",
                tint = VoraMintProgress,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            " Set $number",
            style = MaterialTheme.typography.labelMedium,
            color = if (isCurrent) VoraAccentLime else Color.White
        )
    }
}

/** 47.5 -> "47.5" y 45.0 -> "45" (sin ".0"). */
private fun formatKg(kg: Double): String =
    if (kg % 1.0 == 0.0) kg.toInt().toString() else kg.toString()

/** 78 segundos -> "01:18". */
private fun formatTime(totalSeconds: Int): String {
    val minutes = (totalSeconds / 60).toString().padStart(2, '0')
    val seconds = (totalSeconds % 60).toString().padStart(2, '0')
    return "$minutes:$seconds"
}

@Preview(showBackground = true)
@Composable
private fun ActiveWorkoutPreview() {
    VoraCoachTheme {
        ActiveWorkoutScreen(
            uiState = ActiveWorkoutUiState(
                exerciseNumber = 2,
                totalExercises = 3,
                exerciseName = "Seated Cable Row",
                sets = 3,
                reps = 12,
                currentSet = 2,
                weightKg = 45.0,
                lastSession = "45 kg × 12 reps",
                restSecondsLeft = 78
            ),
            onFinishWorkoutClick = {},
            onDecreaseWeightClick = {},
            onIncreaseWeightClick = {},
            onAddRestClick = {},
            onSkipRestClick = {},
            onCompleteSetClick = {}
        )
    }
}