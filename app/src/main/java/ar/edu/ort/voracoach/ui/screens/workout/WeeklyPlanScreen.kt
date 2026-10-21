package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.PlanDay
import ar.edu.ort.voracoach.model.PlannedSession
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 4.2 WeeklyPlanScreen (Demostración).
 *
 * Pantalla SIN ESTADO: recibe los datos y avisa los eventos con lambdas (onXClick).
 * No conoce al NavController: la navegación la decide WorkoutNavGraph.
 * LazyColumn: la pantalla scrollea en cualquier tamaño de celular (UI03).
 *
 * @param selectedDayIndex día marcado en el selector (el estado vive en el grafo).
 * @param onSessionClick se toca una tarjeta de la semana -> abre su detalle.
 * @param onSuggestionClick se toca una sugerencia -> habla con VORA.
 */
@Composable
fun WeeklyPlanScreen(
    days: List<PlanDay>,
    selectedDayIndex: Int,
    sessions: List<PlannedSession>,
    adjustSuggestions: List<String>,
    onBackClick: () -> Unit,
    onDaySelected: (Int) -> Unit,
    onSessionClick: (PlannedSession) -> Unit,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Barra superior con "Atrás" (NAV03: siempre se puede volver)
        item { VoraTopBar(title = "Weekly Plan", onBack = onBackClick) }

        // Título grande y subtítulo, como en Figma
        item {
            Column(modifier = Modifier.padding(horizontal = VoraScreenPadding)) {
                Text(
                    "Your Plan",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VoraTextPrimary
                )
                Text(
                    "Designed around your goals and recovery.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoraTextSecondary
                )
            }
        }

        // Selector de días de la semana
        item {
            WeekSelector(
                days = days,
                selectedIndex = selectedDayIndex,
                onDaySelected = onDaySelected,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Una tarjeta por cada sesión. key = id estable: Compose no confunde las filas.
        items(sessions, key = { it.id }) { session ->
            SessionCard(
                session = session,
                onClick = { onSessionClick(session) },
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }

        // Tarjeta oscura para ajustar la semana con VORA
        item {
            AdjustWeekCard(
                suggestions = adjustSuggestions,
                onSuggestionClick = onSuggestionClick,
                modifier = Modifier.padding(horizontal = VoraScreenPadding)
            )
        }
    }
}

/** Fila con los 7 días. El seleccionado queda en una "píldora" verde oscura. */
@Composable
private fun WeekSelector(
    days: List<PlanDay>,
    selectedIndex: Int,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            val isSelected = index == selectedIndex
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) VoraDarkGreen else Color.Transparent)
                    .clickable { onDaySelected(index) }
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Text(
                    day.dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) VoraAccentLime else VoraTextSecondary
                )
                Text(
                    day.dateNumber.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) Color.White else VoraTextPrimary
                )
            }
        }
    }
}

/** Tarjeta de una sesión. La de hoy tiene borde verde y la etiqueta READY. */
@Composable
private fun SessionCard(
    session: PlannedSession,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Borde solo para la sesión de hoy (si no, Modifier vacío = sin cambios)
    val borderModifier = if (session.isToday) {
        Modifier.border(1.5.dp, VoraDarkGreen, RoundedCornerShape(20.dp))
    } else {
        Modifier
    }
    VoraCard(modifier = modifier.then(borderModifier), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    session.dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraTextSecondary
                )
                Text(
                    session.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = VoraTextPrimary
                )
                Text(
                    session.details,
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
            }
            if (session.isToday) {
                Text(
                    "READY",
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraDarkGreen,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(VoraAccentLime)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            if (session.isRecovery) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(VoraMintProgress)
                )
            }
        }
    }
}

/**
 * Tarjeta oscura "Need to adjust your week?" con sugerencias rápidas.
 * FlowRow: si las sugerencias no entran en una línea, pasan a la siguiente.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdjustWeekCard(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    VoraCard(modifier = modifier, containerColor = VoraDarkGreen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = VoraAccentLime,
                modifier = Modifier.size(18.dp)
            )
            Text(
                "  NEED TO ADJUST YOUR WEEK?",
                style = MaterialTheme.typography.labelMedium,
                color = VoraAccentLime
            )
        }
        Text(
            WorkoutDemoData.ADJUST_WEEK_MESSAGE,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.8f)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { suggestion ->
                Text(
                    suggestion,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(VoraSecondaryGreen)
                        .clickable { onSuggestionClick(suggestion) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

/** Vista previa para ver la pantalla en Android Studio sin correr la app. */
@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun WeeklyPlanPreview() {
    VoraCoachTheme {
        WeeklyPlanScreen(
            days = WorkoutDemoData.weekDays,
            selectedDayIndex = WorkoutDemoData.TODAY_INDEX,
            sessions = WorkoutDemoData.plannedSessions,
            adjustSuggestions = WorkoutDemoData.adjustSuggestions,
            onBackClick = {},
            onDaySelected = {},
            onSessionClick = {},
            onSuggestionClick = {}
        )
    }
}