package ar.edu.ort.voracoach.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ProgressStat
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

// =====================================================================================
// Piezas COMPARTIDAS por las pantallas del flujo 6 (Progreso).
// Se escriben una sola vez y se reutilizan (UI02): si se corrige una, cambian todas.
// =====================================================================================

/**
 * Selector de período "Week / Month / 3 Months".
 * El elegido es estado de UI local: rememberSaveable lo conserva al rotar.
 * Los datos son de demostración, así que el período solo cambia la selección visible.
 */
@Composable
fun PeriodTabs(options: List<String>, modifier: Modifier = Modifier) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(VoraSurface)
            .border(1.dp, VoraBorder, RoundedCornerShape(50))
            .padding(4.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Text(
                option,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else VoraTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) VoraDarkGreen else Color.Transparent)
                    .clickable { selectedIndex = index }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

/** Tarjeta con varios datos en fila ("SESSIONS 4 · VOLUME 8,420 kg · AVG TIME 41 min"). */
@Composable
fun StatsStrip(stats: List<ProgressStat>, modifier: Modifier = Modifier) {
    VoraCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            stats.forEach { stat ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stat.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = VoraTextSecondary
                    )
                    Text(
                        stat.value,
                        style = MaterialTheme.typography.titleMedium,
                        color = VoraTextPrimary
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta oscura de VORA (insight / advice / analysis): mini orbe, título lima,
 * etiqueta opcional a la derecha, mensaje y un link de acción opcional.
 */
@Composable
fun DarkInsightCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraDarkGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Mini orbe con degradado, identidad visual del asistente
            Box(
                Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(VoraMintProgress, VoraAccentLime)))
            )
            Text(
                "  $title",
                style = MaterialTheme.typography.labelMedium,
                color = VoraAccentLime,
                modifier = Modifier.weight(1f)
            )
            if (badge != null) {
                Text(
                    badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = VoraDarkGreen,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(VoraAccentLime)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Text(message, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        if (actionText != null && onAction != null) {
            Text(
                "$actionText  →",
                style = MaterialTheme.typography.labelMedium,
                color = VoraAccentLime,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAction)
                    .padding(vertical = 4.dp)
            )
        }
    }
}

/** Título chico de sección en mayúsculas ("EXERCISE PROGRESS"). */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = VoraTextSecondary,
        modifier = modifier
    )
}

/** Mosaico chico con etiqueta arriba y valor abajo (grillas de 2 columnas). */
@Composable
fun SmallStatTile(stat: ProgressStat, modifier: Modifier = Modifier) {
    VoraCard(modifier = modifier) {
        Text(stat.label, style = MaterialTheme.typography.labelSmall, color = VoraTextSecondary)
        Text(stat.value, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary)
    }
}

/** Grilla de 2 columnas con mosaicos (4 datos -> 2 filas). */
@Composable
fun StatGrid(stats: List<ProgressStat>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // chunked(2): parte la lista en grupos de 2 -> cada grupo es una fila
        stats.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { stat -> SmallStatTile(stat, Modifier.weight(1f)) }
                // Si la fila quedó con 1 solo, se completa el lugar para mantener el ancho
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

/** Panel verde medio para pantallas oscuras (lo usa la revisión semanal). */
@Composable
fun DarkPanel(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraSecondaryGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}