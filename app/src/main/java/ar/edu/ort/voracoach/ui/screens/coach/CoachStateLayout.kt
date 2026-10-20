// Subpaquete del Flujo 3 (Asistente VORA): 6 estados simulados del asistente de voz.
package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * Plantilla COMÚN de los estados del asistente (3.1, 3.2, 3.3, 3.4 y 3.6).
 *
 * Todos comparten la misma estructura visual: barra superior con "X", píldora de estado,
 * orbe animado, título y una zona de acciones abajo. Centralizarla evita duplicar código
 * (DRY) y garantiza consistencia visual entre estados (UI02).
 *
 * @param orbState animación del orbe (IDLE, LISTENING, PROCESSING, SPEAKING, ERROR).
 * @param statusText texto de la píldora ("AI ONLINE", "LISTENING"...).
 * @param onCloseClick botón "X": cierra el asistente.
 * @param topActions íconos opcionales a la derecha de la barra superior.
 * @param content contenido central debajo del título (transcripción, pasos, respuesta...).
 * @param actions botones inferiores.
 */
@Composable
fun CoachStateLayout(
    orbState: OrbState,
    statusText: String,
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    orbSize: Dp = 220.dp,
    topActions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
    actions: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        VoraTopBar(title = "VORA Coach", onBack = onCloseClick, backIcon = Icons.Rounded.Close, actions = topActions)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = VoraScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CoachStatusPill(text = statusText, isError = orbState == OrbState.ERROR)
            VoraOrbGraphic(state = orbState, size = orbSize)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = VoraTextPrimary,
                textAlign = TextAlign.Center
            )
            content()
            Spacer(Modifier.height(8.dp))
            actions()
            Spacer(Modifier.height(16.dp))
        }
    }
}

/** Píldora de estado con punto de color (lima si está en línea, rojo si hay error). */
@Composable
fun CoachStatusPill(text: String, isError: Boolean = false) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isError) VoraStatusError.copy(alpha = 0.1f) else VoraDarkGreen)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isError) VoraStatusError else VoraAccentLime)
        )
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = if (isError) VoraStatusError else VoraAccentLime
        )
    }
}

/**
 * Botón circular grande de micrófono (verde oscuro con ícono lima).
 * Solo navega: NO pide permiso de RECORD_AUDIO (NAV05).
 */
@Composable
fun CoachMicButton(onClick: () -> Unit, contentDescription: String = "Start talking") {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = VoraDarkGreen, contentColor = VoraAccentLime)
    ) {
        Icon(Icons.Rounded.Mic, contentDescription = contentDescription, modifier = Modifier.size(36.dp))
    }
}

/** Aclaración visible de que la voz es simulada (transparencia en la defensa). */
@Composable
fun CoachDemoCaption(text: String = "Simulated voice demo · no microphone access") {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = VoraTextSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(VoraLightMint)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

/** Fila de un paso del procesamiento: check menta si terminó, punto gris si no. */
@Composable
fun CoachStepRow(text: String, done: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (done) VoraMintProgress else VoraLightMint),
            contentAlignment = Alignment.Center
        ) {
            if (done) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (done) VoraTextPrimary else VoraTextSecondary
        )
    }
}
