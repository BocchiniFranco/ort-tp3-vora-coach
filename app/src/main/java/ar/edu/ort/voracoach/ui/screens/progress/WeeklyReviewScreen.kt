package ar.edu.ort.voracoach.ui.screens.progress

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ProgressDemoData
import ar.edu.ort.voracoach.ui.components.VoraHeroSecondaryButton
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 6.6 WeeklyReviewScreen (Demostración, pantalla oscura).
 *
 * Resumen de la semana, lo que salió bien, qué mejorar y recomendaciones para
 * la próxima semana. Pantalla SIN ESTADO.
 */
@Composable
fun WeeklyReviewScreen(
    onBackClick: () -> Unit,
    onImproveClick: () -> Unit,
    onNextWeekPlanClick: () -> Unit,
    onAskVoraClick: () -> Unit,
    onDoneClick: () -> Unit,
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
        // Encabezado: volver, título y mini orbe
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(VoraSecondaryGreen)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    "Your weekly review",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    "Here's what changed this week.",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraMintProgress
                )
            }
            Box(
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(VoraMintProgress, VoraAccentLime)))
            )
        }

        // Frase principal + 3 datos de la semana
        DarkPanel {
            Text(
                ProgressDemoData.REVIEW_HEADLINE,
                style = MaterialTheme.typography.titleMedium,
                color = VoraAccentLime
            )
            Row(Modifier.fillMaxWidth()) {
                ProgressDemoData.reviewStats.forEach { stat ->
                    Column(Modifier.weight(1f)) {
                        Text(
                            stat.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            stat.value,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Lo que salió bien: un check por punto
        Text(
            "WHAT WENT WELL",
            style = MaterialTheme.typography.labelMedium,
            color = VoraMintProgress
        )
        ProgressDemoData.wentWell.forEach { point ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = VoraMintProgress,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        point.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                    Text(
                        point.detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Qué mejorar, con un acceso para registrar la cena
        Text(
            "WHAT TO IMPROVE",
            style = MaterialTheme.typography.labelMedium,
            color = VoraMintProgress
        )
        DarkPanel {
            Text(
                ProgressDemoData.IMPROVE_TITLE,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
            Text(
                ProgressDemoData.IMPROVE_DETAIL,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                "Get dinner ideas  →",
                style = MaterialTheme.typography.labelMedium,
                color = VoraAccentLime,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onImproveClick)
                    .padding(vertical = 4.dp)
            )
        }

        // Recomendaciones para la próxima semana (tarjeta blanca)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VoraSurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "NEXT WEEK'S RECOMMENDATIONS",
                style = MaterialTheme.typography.labelSmall,
                color = VoraTextSecondary
            )
            ProgressDemoData.nextWeekTips.forEach { tip ->
                Text("• $tip", style = MaterialTheme.typography.bodySmall, color = VoraTextPrimary)
            }
            Text(
                "View next week's plan  →",
                style = MaterialTheme.typography.labelMedium,
                color = VoraDarkGreen,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onNextWeekPlanClick)
                    .padding(vertical = 4.dp)
            )
        }

        VoraLimeButton(
            text = "Ask VORA",
            onClick = onAskVoraClick,
            modifier = Modifier.fillMaxWidth()
        )
        VoraHeroSecondaryButton(
            text = "Done",
            onClick = onDoneClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyReviewPreview() {
    VoraCoachTheme { WeeklyReviewScreen({}, {}, {}, {}, {}) }
}