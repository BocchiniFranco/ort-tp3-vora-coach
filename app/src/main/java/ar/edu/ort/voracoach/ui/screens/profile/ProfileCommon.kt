package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraListRow
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraMintTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import androidx.compose.ui.res.painterResource
import ar.edu.ort.voracoach.R

/** Estructura común de 7.2 a 7.6: encabezado con "volver" y contenido desplazable. */
@Composable
internal fun ProfileSubScreen(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraIconCircleButton(
                painter = painterResource(R.drawable.ic_chevron_left),
                contentDescription = "Back",
                onClick = onBackClick,
                iconSize = 16,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextPrimary
            )
        }
        content()
    }
}

@Composable
internal fun ProfileSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = VoraTextTertiary,
        modifier = modifier.padding(top = 4.dp)
    )
}

@Composable
internal fun ProfileDescription(text: String) {
    Text(text = text, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary)
}

/** Tarjeta de opción: menta con borde y check cuando está elegida; blanca con círculo vacío si no. */
@Composable
internal fun SelectableOptionCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    subtitle: String? = null
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) VoraMintTranslucent else VoraSurface)
            .border(if (selected) 2.dp else 1.dp, if (selected) VoraMint else VoraBorder, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextPrimary
            )
            if (subtitle != null) {
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
            }
        }
        Spacer(Modifier.width(12.dp))
        SelectionMark(selected)
    }
}

@Composable
internal fun SelectionMark(selected: Boolean) {
    if (selected) {
        Box(
            modifier = Modifier.size(20.dp).clip(CircleShape).background(VoraGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(14.dp))
        }
    } else {
        Box(Modifier.size(20.dp).clip(CircleShape).border(1.5.dp, VoraBorder, CircleShape))
    }
}

/** Opciones de igual ancho; la elegida en verde oscuro ("4 days", "Balanced"). */
@Composable
internal fun SegmentedOptions(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option == selected
            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isSelected) Color.White else VoraTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) VoraGreen else VoraSurface)
                    .border(1.dp, if (isSelected) VoraGreen else VoraBorder, RoundedCornerShape(12.dp))
                    .clickable { onSelect(option) }
                    .padding(vertical = 10.dp)
            )
        }
    }
}

/** Chip de preferencia: menta cuando está elegido ("Strength", "Everything"). */
@Composable
internal fun PreferenceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .clip(VoraPillShape)
            .background(if (selected) VoraMint else VoraSurface)
            .border(1.dp, if (selected) VoraMint else VoraBorder, VoraPillShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = if (selected) VoraGreen else VoraTextPrimary
        )
        trailing?.invoke()
    }
}

/** Tarjeta blanca con borde, para agrupar filas (snapshot, switches, preferencias). */
@Composable
internal fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
internal fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = VoraTextPrimary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = VoraMint,
                checkedBorderColor = VoraMint,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = VoraBorder,
                uncheckedBorderColor = VoraBorder
            )
        )
    }
}

/** Fila con ícono del Figma (Perfil y Settings), dentro de una tarjeta blanca. */
@Composable
internal fun ProfileNavigationRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        VoraListRow(
            title = title,
            subtitle = subtitle,
            icon = icon,
            iconBackground = VoraBackground,
            onClick = onClick,
            trailing = trailing
        )
    }
}

/** Fila "etiqueta ........ valor" de las tarjetas de resumen. */
@Composable
internal fun LabelValueRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = VoraTextSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
            color = VoraTextPrimary
        )
    }
}
