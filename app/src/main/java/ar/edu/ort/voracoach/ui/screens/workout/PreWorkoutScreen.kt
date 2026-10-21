package ar.edu.ort.voracoach.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.CoachNote
import ar.edu.ort.voracoach.model.WorkoutDemoData
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen

/**
 * 4.4 PreWorkoutScreen (Demostración).
 *
 * Pantalla de fondo verde oscuro, antes de empezar a entrenar: muestra el foco del día,
 * la última sesión y un consejo de VORA. Es SIN ESTADO: recibe datos y avisa eventos.
 * verticalScroll: si el celular es chico, el contenido scrollea (UI03).
 *
 * @param onCloseClick botón "X": vuelve a la pantalla anterior.
 * @param onAskVoraClick "Need anything?": abre el asistente de voz (simulado).
 * @param onStartWorkoutClick "Start Workout": pasa al entrenamiento activo (4.5).
 */
@Composable
fun PreWorkoutScreen(
    title: String,
    summary: String,
    notes: List<CoachNote>,
    suggestion: String,
    onCloseClick: () -> Unit,
    onAskVoraClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraDarkGreen)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = VoraScreenPadding, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Fila superior: etiqueta "GET READY" y botón "X" para cerrar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "GET READY",
                style = MaterialTheme.typography.labelMedium,
                color = VoraAccentLime,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        // Nombre del entrenamiento y resumen
        Column {
            Text(title, style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Text(summary, style = MaterialTheme.typography.bodyMedium, color = VoraMintProgress)
        }

        // Una tarjeta por cada nota (foco, última sesión, consejo)
        notes.forEach { note -> CoachNoteCard(note) }

        // Botón "Need anything?" con micrófono (no pide permisos: solo navega)
        AskVoraRow(suggestion = suggestion, onClick = onAskVoraClick)

        Spacer(Modifier.height(8.dp))

        // Botón principal
        Button(
            onClick = onStartWorkoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = VoraMintProgress,
                contentColor = VoraDarkGreen
            )
        ) {
            Text("Start Workout", style = MaterialTheme.typography.titleMedium)
        }
    }
}

/** Tarjeta verde (un poco más clara que el fondo) con etiqueta, título opcional y texto. */
@Composable
private fun CoachNoteCard(note: CoachNote) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraSecondaryGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(note.label, style = MaterialTheme.typography.labelSmall, color = VoraAccentLime)
        // "?.let": solo dibuja el título si NO es null
        note.title?.let { title ->
            Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Text(
            note.body,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.75f)
        )
    }
}

/** Píldora "Need anything?" con la sugerencia y un círculo lima con micrófono. */
@Composable
private fun AskVoraRow(suggestion: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(VoraSecondaryGreen)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(VoraMintProgress)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Need anything?",
                style = MaterialTheme.typography.labelMedium,
                color = VoraAccentLime
            )
            Text(
                suggestion,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(VoraAccentLime),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Mic,
                contentDescription = "Ask VORA",
                tint = VoraDarkGreen,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreWorkoutPreview() {
    VoraCoachTheme {
        PreWorkoutScreen(
            title = WorkoutDemoData.TODAY_WORKOUT_TITLE,
            summary = WorkoutDemoData.TODAY_WORKOUT_SUMMARY,
            notes = WorkoutDemoData.preWorkoutNotes,
            suggestion = WorkoutDemoData.PRE_WORKOUT_SUGGESTION,
            onCloseClick = {},
            onAskVoraClick = {},
            onStartWorkoutClick = {}
        )
    }
}