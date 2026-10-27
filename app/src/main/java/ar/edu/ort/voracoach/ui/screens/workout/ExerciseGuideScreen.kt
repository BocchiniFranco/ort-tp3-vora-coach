package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 4.6 ExerciseGuideScreen (Demostración).
 *
 * Guía de ejecución de un ejercicio: músculos trabajados, pasos numerados,
 * consejos de técnica y acceso a VORA. Pantalla SIN ESTADO.
 * verticalScroll: el contenido scrollea en cualquier tamaño de celular (UI03).
 */
@Composable
fun ExerciseGuideScreen(
    exerciseName: String,
    target: String,
    steps: List<String>,
    formCues: List<String>,
    askVoraText: String,
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAskVoraClick: () -> Unit,
    onBackToWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Barra superior: volver, título y ayuda "?"
        VoraTopBar(title = "Exercise Guide", onBack = onBackClick) {
            VoraIconCircleButton(
                icon = Icons.AutoMirrored.Rounded.HelpOutline,
                contentDescription = "Help",
                onClick = onHelpClick
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = VoraScreenPadding),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Nombre y músculos trabajados
            Column {
                Text(
                    exerciseName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = VoraTextPrimary
                )
                Text(target, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
            }

            // Ilustración (en Figma es una foto; acá un recuadro con ícono, sin recursos externos)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(VoraDarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.FitnessCenter,
                    contentDescription = null,
                    tint = VoraAccentLime,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Pasos numerados: forEachIndexed da la posición (empieza en 0)
            Text(
                "Performance Steps",
                style = MaterialTheme.typography.titleMedium,
                color = VoraTextPrimary
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                steps.forEachIndexed { index, step ->
                    Text(
                        "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VoraTextSecondary
                    )
                }
            }

            // Consejos de técnica
            VoraCard {
                Text(
                    "PRO FORM CUES",
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraTextSecondary
                )
                formCues.forEach { cue ->
                    Text(
                        "• $cue",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoraTextPrimary
                    )
                }
            }

            // Acceso al asistente (simulado, sin permisos de micrófono)
            VoraTipBanner(message = askVoraText, onClick = onAskVoraClick)

            VoraPrimaryButton(
                text = "Back to Workout",
                onClick = onBackToWorkoutClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun ExerciseGuidePreview() {
    VoraCoachTheme {
        ExerciseGuideScreen(
            exerciseName = WorkoutDemoData.GUIDE_EXERCISE_NAME,
            target = WorkoutDemoData.GUIDE_TARGET,
            steps = WorkoutDemoData.guideSteps,
            formCues = WorkoutDemoData.guideFormCues,
            askVoraText = WorkoutDemoData.GUIDE_ASK_VORA,
            onBackClick = {},
            onHelpClick = {},
            onAskVoraClick = {},
            onBackToWorkoutClick = {}
        )
    }
}