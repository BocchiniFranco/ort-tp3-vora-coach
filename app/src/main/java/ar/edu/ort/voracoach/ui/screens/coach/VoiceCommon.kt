// Subpaquete del Flujo 3 (Asistente VORA): 6 estados del Figma "AI Voice Flow".
package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary

/**
 * Encabezado de los estados del asistente (Figma "header-row"): botón volver de 32 dp con
 * chevron de 14 dp y título 15 SemiBold a su derecha; a la derecha, un indicador opcional
 * ("ONLINE", "● LIVE AUDIO", "SPEAKING", orbe...).
 *
 * @param dark true en las pantallas de fondo verde (Listening, Processing, Speaking):
 *             botón blanco al 8 % con borde blanco al 12 % y texto blanco.
 */
@Composable
fun VoiceHeader(
    title: String,
    onBackClick: () -> Unit,
    dark: Boolean,
    modifier: Modifier = Modifier,
    showOnlineDot: Boolean = false,
    trailing: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val shape = RoundedCornerShape(16.dp)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(shape)
                    .background(if (dark) Color.White.copy(alpha = 0.08f) else Color.White)
                    .border(1.dp, if (dark) Color.White.copy(alpha = 0.12f) else VoraBorder, shape)
                    .clickable(onClickLabel = "Back", onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Back",
                    tint = if (dark) Color.White else VoraTextPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                title,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                color = if (dark) Color.White else VoraTextPrimary
            )
            // Punto menta de 6 dp "en línea" (encabezado del chat en el Figma).
            if (showOnlineDot) VoiceDot(color = ar.edu.ort.voracoach.ui.theme.VoraMint, size = 6)
        }
        Row(verticalAlignment = Alignment.CenterVertically, content = trailing)
    }
}

/** Etiqueta de estado en píldora (ej. "ONLINE", "SPEAKING"): fondo menta al 15 %, 11 SemiBold. */
@Composable
fun VoiceStatusTag(text: String, textColor: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(ar.edu.ort.voracoach.ui.theme.VoraMintTag)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

/** Punto de color de [size] dp (indicador "en vivo" / "en línea"). */
@Composable
fun VoiceDot(color: Color, size: Int) {
    Box(Modifier.size(size.dp).clip(CircleShape).background(color))
}
