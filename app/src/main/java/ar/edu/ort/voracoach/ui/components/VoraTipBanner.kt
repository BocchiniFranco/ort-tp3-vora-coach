package ar.edu.ort.voracoach.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintBorderTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraMintTranslucent
import androidx.compose.ui.unit.dp

/**
 * VoraTipBanner: "mensaje informativo" del Figma. Menta translúcida al 12 % con borde menta
 * al 25 %, radio 12, padding 12, ícono de destellos (sparkles) de 16 dp y texto 13 Medium
 * verde #123D35. Ejemplo:
 * "You're on track. A protein-rich dinner would help you reach your daily target."
 *
 * @param title título opcional en negrita (no aparece en el Figma; queda para otros usos).
 * @param onClick acción opcional si el banner lleva a otra pantalla.
 */
@Composable
fun VoraTipBanner(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(12.dp)
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraMintTranslucent)
            .border(1.dp, VoraMintBorderTranslucent, shape)
            .then(clickModifier)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image (sin tinte) para conservar el relleno blanco y el trazo verde del ícono del Figma.
        Image(
            painter = painterResource(R.drawable.ic_sparkles),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            if (title != null) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = VoraGreen)
            }
            Text(message, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = VoraGreen)
        }
    }
}
