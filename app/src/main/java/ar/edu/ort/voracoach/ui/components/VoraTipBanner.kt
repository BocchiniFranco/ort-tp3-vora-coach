package ar.edu.ort.voracoach.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// Ícono de "destello" (AutoAwesome) que representa a la IA de VORA.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
// Brush.linearGradient: degradado para el pequeño orbe del banner.
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraLightMintBorder
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress

/**
 * VoraTipBanner: tarjeta menta claro (#E3F5EE) con borde #CBE8DC y texto #103B31.
 * Muestra recomendaciones de la IA, por ejemplo:
 * "You're on track. A protein-rich dinner would help you reach your daily target."
 *
 * @param title título opcional en negrita (ej. "Vora's tip").
 * @param message texto de la recomendación.
 * @param onClick acción opcional si el banner lleva a otra pantalla.
 */
@Composable
fun VoraTipBanner(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(20.dp)
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraLightMint)
            .border(1.dp, VoraLightMintBorder, shape)
            .then(clickModifier)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Mini orbe con degradado + destello: identidad visual del asistente.
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VoraMintProgress, VoraAccentLime))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = VoraDarkGreen,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (title != null) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = VoraDarkGreen)
            }
            Text(message, style = MaterialTheme.typography.bodyMedium, color = VoraDarkGreen)
        }
    }
}
