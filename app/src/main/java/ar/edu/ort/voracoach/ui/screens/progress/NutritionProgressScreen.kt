package ar.edu.ort.voracoach.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DayTarget
import ar.edu.ort.voracoach.model.ProgressDemoData
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTrack

/**
 * 6.4 NutritionProgressScreen (Demostración).
 *
 * Promedios de la semana, días en que se cumplió el objetivo de proteína,
 * análisis de VORA y hábitos de comida. Pantalla SIN ESTADO.
 */
@Composable
fun NutritionProgressScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoraTopBar(title = "Nutrition progress", onBack = onBackClick) }

        item {
            PeriodTabs(
                ProgressDemoData.periods,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            StatsStrip(
                ProgressDemoData.nutritionStats,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Selector de macro + cumplimiento diario del objetivo
        item {
            VoraCard(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                PeriodTabs(ProgressDemoData.macroTabs)
                SectionLabel(ProgressDemoData.PROTEIN_TARGET_LABEL)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProgressDemoData.proteinDays.forEach { day -> DayTargetCircle(day) }
                }
            }
        }

        item {
            DarkInsightCard(
                title = "Nutrition Analysis",
                message = ProgressDemoData.NUTRITION_ANALYSIS,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            SectionLabel(
                "Food habits & patterns",
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            StatGrid(
                ProgressDemoData.foodHabits,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }
    }
}

/** Círculo con la inicial del día y debajo "HIT" (cumplió) o "MISS" (no cumplió). */
@Composable
private fun DayTargetCircle(day: DayTarget) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (day.isHit) VoraLightMint else VoraTrack)
                .border(
                    1.5.dp,
                    if (day.isHit) VoraMintProgress else Color.Transparent,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                day.label,
                style = MaterialTheme.typography.labelMedium,
                color = if (day.isHit) VoraDarkGreen else VoraTextSecondary
            )
        }
        Text(
            if (day.isHit) "HIT" else "MISS",
            style = MaterialTheme.typography.labelSmall,
            color = if (day.isHit) VoraMintProgress else VoraTextSecondary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun NutritionProgressPreview() {
    VoraCoachTheme { NutritionProgressScreen({}) }
}