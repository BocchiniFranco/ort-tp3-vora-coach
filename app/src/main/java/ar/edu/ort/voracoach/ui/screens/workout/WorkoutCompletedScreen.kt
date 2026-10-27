package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraChip
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 4.7 WorkoutCompletedScreen (Demostración).
 *
 * Resumen al terminar: duración, ejercicios y volumen, un comentario de VORA y la
 * calificación de dificultad. Pantalla SIN ESTADO: la opción elegida vive en el grafo.
 *
 * @param selectedDifficulty opción marcada (null = ninguna todavía).
 * @param onDoneClick "Done": vuelve al dashboard de entrenamiento (4.1).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutCompletedScreen(
    selectedDifficulty: String?,
    difficultyOptions: List<String>,
    onDifficultySelected: (String) -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = VoraScreenPadding, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("COMPLETED", style = MaterialTheme.typography.labelMedium, color = VoraMintProgress)

        // Anillo menta con un check lima en el centro
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(VoraLightMint)
                .border(8.dp, VoraMintProgress, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(VoraDarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = VoraAccentLime,
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        Text(
            "You did well!",
            style = MaterialTheme.typography.headlineMedium,
            color = VoraTextPrimary
        )
        Text(
            "Strong session. Your consistency is improving.",
            style = MaterialTheme.typography.bodyMedium,
            color = VoraTextSecondary,
            textAlign = TextAlign.Center
        )

        // Resumen: duración, ejercicios y volumen
        VoraCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryStat(WorkoutDemoData.COMPLETED_DURATION, "DURATION")
                SummaryStat(WorkoutDemoData.COMPLETED_EXERCISES, "EXERCISES")
                SummaryStat(WorkoutDemoData.COMPLETED_VOLUME, "VOLUME")
            }
        }

        VoraTipBanner(title = "VORA INSIGHT", message = WorkoutDemoData.COMPLETED_INSIGHT)

        // Calificación de dificultad: un chip por opción, el elegido queda marcado
        Text(
            "Rate this workout difficulty",
            style = MaterialTheme.typography.bodySmall,
            color = VoraTextSecondary
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            difficultyOptions.forEach { option ->
                VoraChip(
                    text = option,
                    selected = option == selectedDifficulty,
                    onClick = { onDifficultySelected(option) }
                )
            }
        }

        VoraPrimaryButton(text = "Done", onClick = onDoneClick, modifier = Modifier.fillMaxWidth())
    }
}

/** Valor grande con su etiqueta abajo ("42 min" / "DURATION"). */
@Composable
private fun SummaryStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = VoraTextPrimary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = VoraTextSecondary)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun WorkoutCompletedPreview() {
    VoraCoachTheme {
        WorkoutCompletedScreen(
            selectedDifficulty = "Good",
            difficultyOptions = WorkoutDemoData.difficultyOptions,
            onDifficultySelected = {},
            onDoneClick = {}
        )
    }
}