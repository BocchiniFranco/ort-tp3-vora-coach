package ar.edu.ort.voracoach.ui.screens.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.HabitSignal
import ar.edu.ort.voracoach.model.ProgressDemoData
import ar.edu.ort.voracoach.ui.components.VoraBarChart
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 6.5 HealthTrendsScreen (Demostración).
 *
 * Pasos, distancia y calorías activas; gráfico de pasos de la semana y señales
 * de hábitos. Pantalla SIN ESTADO.
 */
@Composable
fun HealthTrendsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoraTopBar(title = "Health trends", onBack = onBackClick) }

        item {
            Text(
                "See how your daily habits are changing.",
                style = MaterialTheme.typography.bodyMedium,
                color = VoraTextSecondary,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            StatsStrip(
                ProgressDemoData.healthStats,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Gráfico de pasos de la semana
        item {
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Row {
                    Text(
                        "Weekly Steps Trend",
                        style = MaterialTheme.typography.titleSmall,
                        color = VoraTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        ProgressDemoData.STEPS_GOAL,
                        style = MaterialTheme.typography.labelMedium,
                        color = VoraMintProgress
                    )
                }
                VoraBarChart(
                    values = ProgressDemoData.weeklySteps,
                    labels = ProgressDemoData.weekLabels,
                    highlightIndex = 4
                )
            }
        }

        item {
            VoraTipBanner(
                message = ProgressDemoData.HEALTH_TIP,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item { SectionLabel("Habit signals", Modifier.padding(horizontal = VoraScreenPadding)) }

        // Una señal por fila: "Improving" en verde, el resto en gris
        items(ProgressDemoData.habitSignals, key = { it.name }) { signal ->
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(iconFor(signal), contentDescription = null, tint = VoraDarkGreen)
                    Text(
                        signal.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = VoraTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        signal.status,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (signal.isImproving) VoraMintProgress else VoraTextSecondary
                    )
                }
            }
        }
    }
}

/** Ícono de cada señal según su nombre. */
private fun iconFor(signal: HabitSignal): ImageVector = when (signal.name) {
    "Sleep Quality" -> Icons.Rounded.Bedtime
    "Daily Movement" -> Icons.AutoMirrored.Rounded.DirectionsWalk
    "Workout Frequency" -> Icons.Rounded.FitnessCenter
    else -> Icons.Rounded.Restaurant
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun HealthTrendsPreview() {
    VoraCoachTheme { HealthTrendsScreen({}) }
}