package ar.edu.ort.voracoach.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ExerciseTrend
import ar.edu.ort.voracoach.model.ProgressDemoData
import ar.edu.ort.voracoach.ui.components.VoraBarChart
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraLightMintBorder
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTrack

/**
 * 6.3 WorkoutProgressScreen (Demostración).
 *
 * Sesiones, volumen y tiempo; gráfico de volumen por sesión; mejor marca personal
 * y la evolución de cada ejercicio. Pantalla SIN ESTADO.
 *
 * @param onExerciseClick tocar un ejercicio abre su guía de ejecución (4.6).
 */
@Composable
fun WorkoutProgressScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (ExerciseTrend) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoraTopBar(title = "Workout progress", onBack = onBackClick) }

        item {
            PeriodTabs(
                ProgressDemoData.periods,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            StatsStrip(
                ProgressDemoData.workoutStats,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Gráfico de barras con el volumen de cada sesión (componente compartido)
        item {
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Training Volume",
                            style = MaterialTheme.typography.titleSmall,
                            color = VoraTextPrimary
                        )
                        Text(
                            "KILOGRAMS LIFTED PER SESSION",
                            style = MaterialTheme.typography.labelSmall,
                            color = VoraTextSecondary
                        )
                    }
                    Text(
                        ProgressDemoData.VOLUME_CHANGE,
                        style = MaterialTheme.typography.labelMedium,
                        color = VoraMintProgress
                    )
                }
                VoraBarChart(
                    values = ProgressDemoData.trainingVolume,
                    labels = ProgressDemoData.trainingVolumeLabels,
                    highlightIndex = ProgressDemoData.trainingVolume.lastIndex
                )
            }
        }

        // Mejor marca personal (tarjeta blanca con borde menta)
        item {
            val shape = RoundedCornerShape(20.dp)
            Row(
                modifier = Modifier
                    .padding(horizontal = VoraScreenPadding)
                    .fillMaxWidth()
                    .clip(shape)
                    .background(VoraSurface)
                    .border(1.dp, VoraLightMintBorder, shape)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = VoraMintProgress)
                Column {
                    Text(
                        "PERSONAL BEST",
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraMintProgress
                    )
                    Text(
                        ProgressDemoData.PERSONAL_BEST_TITLE,
                        style = MaterialTheme.typography.titleSmall,
                        color = VoraTextPrimary
                    )
                    Text(
                        ProgressDemoData.PERSONAL_BEST_DETAIL,
                        style = MaterialTheme.typography.bodySmall,
                        color = VoraTextSecondary
                    )
                }
            }
        }

        item {
            VoraTipBanner(
                message = ProgressDemoData.WORKOUT_PROGRESS_TIP,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item { SectionLabel("Exercise progress", Modifier.padding(horizontal = VoraScreenPadding)) }

        // Un ejercicio por fila: la variación en verde si mejoró
        items(ProgressDemoData.exerciseTrends, key = { it.name }) { trend ->
            VoraCard(
                modifier = Modifier.padding(horizontal = VoraScreenPadding),
                onClick = { onExerciseClick(trend) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            trend.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = VoraTextPrimary
                        )
                        Text(
                            trend.detail,
                            style = MaterialTheme.typography.bodySmall,
                            color = VoraTextSecondary
                        )
                    }
                    Text(
                        trend.change,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (trend.isImproving) VoraDarkGreen else VoraTextSecondary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (trend.isImproving) VoraLightMint else VoraTrack)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun WorkoutProgressPreview() {
    VoraCoachTheme { WorkoutProgressScreen({}, {}) }
}