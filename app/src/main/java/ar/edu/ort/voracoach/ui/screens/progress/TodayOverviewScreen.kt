package ar.edu.ort.voracoach.ui.screens.progress

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.CompletionItem
import ar.edu.ort.voracoach.model.ProgressDemoData
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraCircularProgress
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 6.1 TodayOverviewScreen ("Daily Summary", Demostración).
 *
 * Avance del día (anillo con el porcentaje), detalle por categoría, consejo de VORA
 * y el entrenamiento del día. Pantalla SIN ESTADO.
 */
@Composable
fun TodayOverviewScreen(
    onBackClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onWeeklyTargetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoraTopBar(title = "Daily Summary", onBack = onBackClick) }

        item {
            Column(Modifier.padding(horizontal = VoraScreenPadding)) {
                Text(
                    ProgressDemoData.OVERVIEW_DATE,
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraTextSecondary
                )
                Text(
                    "Today's Overview",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VoraTextPrimary
                )
            }
        }

        // Avance del día: porcentaje grande + anillo + una fila por categoría
        item {
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "DAILY COMPLETION",
                            style = MaterialTheme.typography.labelSmall,
                            color = VoraTextSecondary
                        )
                        Text(
                            ProgressDemoData.DAILY_COMPLETION_TEXT,
                            style = MaterialTheme.typography.headlineMedium,
                            color = VoraTextPrimary
                        )
                    }
                    VoraCircularProgress(
                        progress = ProgressDemoData.DAILY_COMPLETION,
                        centerText = "",
                        size = 56.dp
                    )
                }
                ProgressDemoData.completionItems.forEach { item -> CompletionRow(item) }
            }
        }

        item {
            DarkInsightCard(
                title = "VORA ADVICE",
                message = ProgressDemoData.OVERVIEW_ADVICE,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            Text(
                "Category Breakdowns",
                style = MaterialTheme.typography.titleMedium,
                color = VoraTextPrimary,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Entrenamiento del día, todavía sin empezar
        item {
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Row {
                    Text(
                        "WORKOUT",
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraTextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "NOT STARTED",
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraStatusError
                    )
                }
                Text(
                    WorkoutDemoData.TODAY_WORKOUT_TITLE,
                    style = MaterialTheme.typography.titleMedium,
                    color = VoraTextPrimary
                )
                Text(
                    WorkoutDemoData.TODAY_WORKOUT_SUMMARY,
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
                VoraPrimaryButton(
                    text = "Start Workout",
                    onClick = onStartWorkoutClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Objetivo actual y meta semanal (abre el plan de la semana)
        item {
            VoraCard(
                modifier = Modifier.padding(horizontal = VoraScreenPadding),
                onClick = onWeeklyTargetClick
            ) {
                Row {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "CURRENT GOAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = VoraTextSecondary
                        )
                        Text(
                            ProgressDemoData.OVERVIEW_GOAL,
                            style = MaterialTheme.typography.titleSmall,
                            color = VoraTextPrimary
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            "WEEKLY TARGET",
                            style = MaterialTheme.typography.labelSmall,
                            color = VoraTextSecondary
                        )
                        Text(
                            ProgressDemoData.OVERVIEW_WEEKLY_TARGET,
                            style = MaterialTheme.typography.titleSmall,
                            color = VoraTextPrimary
                        )
                    }
                }
            }
        }
    }
}

/** Fila "● Nutrition Target   1,420 / 2,100 kcal   78%". Punto rojo si no empezó. */
@Composable
private fun CompletionRow(item: CompletionItem) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (item.isPending) VoraStatusError else VoraMintProgress)
        )
        Text(
            "  ${item.label}",
            style = MaterialTheme.typography.bodySmall,
            color = VoraTextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            "${item.detail}  ",
            style = MaterialTheme.typography.labelSmall,
            color = VoraTextSecondary
        )
        Text(item.percent, style = MaterialTheme.typography.labelMedium, color = VoraTextPrimary)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun TodayOverviewPreview() {
    VoraCoachTheme { TodayOverviewScreen({}, {}, {}) }
}