package ar.edu.ort.voracoach.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ProgressCategory
import ar.edu.ort.voracoach.model.ProgressDemoData
import ar.edu.ort.voracoach.ui.components.VoraListRow
import ar.edu.ort.voracoach.ui.components.VoraScreenHeader
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraLightMintBorder
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 6.2 ProgressDashboardScreen (Demostración). Destino "Progress" de la barra inferior.
 *
 * Resumen de la semana, insight de VORA, accesos a cada categoría y el objetivo activo.
 * Pantalla SIN ESTADO: cada acceso avisa con una lambda y el grafo navega.
 *
 * @param onCategoryClick recibe la categoría tocada (Workout, Nutrition o Activity).
 */
@Composable
fun ProgressDashboardScreen(
    onWeeklyReviewClick: () -> Unit,
    onCategoryClick: (ProgressCategory) -> Unit,
    onActiveGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoraScreenHeader(title = "Your progress") }

        item {
            Text(
                "Small improvements add up.",
                style = MaterialTheme.typography.bodyMedium,
                color = VoraTextSecondary,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            PeriodTabs(
                ProgressDemoData.periods,
                Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        item {
            Column(
                Modifier.padding(horizontal = VoraScreenPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SectionLabel("This week overview")
                StatGrid(ProgressDemoData.weekOverview)
            }
        }

        item {
            DarkInsightCard(
                title = "VORA Insight",
                message = ProgressDemoData.DASHBOARD_INSIGHT,
                badge = "CONSISTENT",
                actionText = "See weekly review",
                onAction = onWeeklyReviewClick,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Una fila por categoría: abre su pantalla de detalle
        items(ProgressDemoData.categories, key = { it.title }) { category ->
            VoraListRow(
                title = category.title,
                subtitle = category.subtitle,
                icon = iconFor(category.title),
                onClick = { onCategoryClick(category) },
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Objetivo activo (tarjeta menta)
        item {
            val shape = RoundedCornerShape(20.dp)
            Column(
                modifier = Modifier
                    .padding(horizontal = VoraScreenPadding)
                    .fillMaxWidth()
                    .clip(shape)
                    .background(VoraLightMint)
                    .border(1.dp, VoraLightMintBorder, shape)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row {
                    Text(
                        "ACTIVE GOAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraTextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        ProgressDemoData.ACTIVE_GOAL_PROGRESS,
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraMintProgress
                    )
                }
                Text(
                    ProgressDemoData.ACTIVE_GOAL,
                    style = MaterialTheme.typography.titleMedium,
                    color = VoraTextPrimary
                )
                Text(
                    ProgressDemoData.ACTIVE_GOAL_DETAIL,
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
                Text(
                    "View goals  →",
                    style = MaterialTheme.typography.labelMedium,
                    color = VoraMintProgress,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onActiveGoalClick)
                )
            }
        }
    }
}

/** Ícono de cada categoría. */
private fun iconFor(title: String): ImageVector = when (title) {
    "Workout" -> Icons.Rounded.FitnessCenter
    "Nutrition" -> Icons.Rounded.Restaurant
    else -> Icons.AutoMirrored.Rounded.DirectionsWalk
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun ProgressDashboardPreview() {
    VoraCoachTheme { ProgressDashboardScreen({}, {}, {}) }
}