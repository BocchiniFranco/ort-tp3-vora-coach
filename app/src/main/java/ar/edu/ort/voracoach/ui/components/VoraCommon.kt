package ar.edu.ort.voracoach.ui.components

// --- Foundation: dibujo, clicks y layouts ---
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// --- Íconos ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
// --- Material 3 ---
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
// --- Runtime y UI ---
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
// KeyboardOptions: tipo de teclado (numérico para calorías/macros).
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
// --- Paleta VORA ---
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraDivider
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraLightMintBorder
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/** Padding horizontal estándar de las pantallas (márgenes laterales de Figma). */
val VoraScreenPadding = 20.dp

/**
 * VoraTopBar: barra superior de pantallas secundarias con botón volver ("<"),
 * título centrado y acciones opcionales a la derecha (NAV03: volver siempre visible).
 *
 * @param onBack si es null no se muestra la flecha (pantallas raíz).
 * @param actions slot para íconos de la derecha (campana, ajustes, calendario...).
 */
@Composable
fun VoraTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backIcon: ImageVector = Icons.AutoMirrored.Rounded.ArrowBack,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            VoraIconCircleButton(icon = backIcon, contentDescription = "Back", onClick = onBack)
        } else {
            Spacer(Modifier.width(48.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = VoraTextPrimary,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Row(
            modifier = Modifier.width(IntrinsicActionsWidth),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}

/** Ancho reservado a la derecha de la TopBar para que el título quede centrado. */
private val IntrinsicActionsWidth = 96.dp

/**
 * Encabezado grande de las pantallas raíz (Home, Workout, Nutrition, Progress, Profile).
 * Título grande a la izquierda, subtítulo opcional y acciones (íconos) a la derecha.
 */
@Composable
fun VoraScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = VoraScreenPadding, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary)
            }
            Text(title, style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), content = actions)
    }
}

/**
 * Botón circular blanco con ícono (campana, calendario, ajustes, volver).
 * Siempre requiere onClick: así evitamos botones "muertos" (NAV01).
 */
@Composable
fun VoraIconCircleButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = VoraSurface,
    iconTint: Color = VoraTextPrimary
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(containerColor)
    ) {
        Icon(icon, contentDescription = contentDescription, tint = iconTint)
    }
}

/**
 * VoraCard: tarjeta blanca de esquinas 20 dp, base de casi todos los bloques de contenido.
 */
@Composable
fun VoraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = VoraSurface,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(containerColor)
            .then(clickModifier)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

/** Botón primario verde oscuro con texto blanco (acciones principales sobre fondo claro). */
@Composable
fun VoraPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoraDarkGreen,
            contentColor = Color.White,
            disabledContainerColor = VoraDarkGreen.copy(alpha = 0.35f),
            disabledContentColor = Color.White
        )
    ) {
        ButtonContent(text, icon)
    }
}

/** Botón CTA lima (#C6E854) con texto #103B31: se usa sobre las Hero Cards oscuras. */
@Composable
fun VoraLimeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoraAccentLime,
            contentColor = VoraDarkGreen
        )
    ) {
        ButtonContent(text, icon)
    }
}

/** Sub-botón verde secundario (#225046) dentro de Hero Cards. */
@Composable
fun VoraHeroSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen,
            contentColor = Color.White
        )
    ) {
        ButtonContent(text, icon)
    }
}

/** Botón secundario con borde (Cancel, Edit, Skip...). */
@Composable
fun VoraOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = VoraDarkGreen,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VoraLightMintBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = VoraSurface,
            contentColor = contentColor
        )
    ) {
        ButtonContent(text, icon)
    }
}

/** Contenido compartido de los botones: ícono opcional + texto. */
@Composable
private fun ButtonContent(text: String, icon: ImageVector?) {
    if (icon != null) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
    }
    Text(text, style = MaterialTheme.typography.labelLarge)
}

/**
 * Encabezado de sección: título a la izquierda y acción tipo link a la derecha
 * ("See all", "Edit"). Si [actionText] es null no se muestra la acción.
 */
@Composable
fun VoraSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, color = VoraTextPrimary)
        if (actionText != null && onAction != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.titleSmall,
                color = ar.edu.ort.voracoach.ui.theme.VoraMintProgress,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Fila de lista con ícono en círculo menta, título, subtítulo y flecha ">".
 * Se usa en Perfil, Notificaciones, Ajustes y listados de comidas/ejercicios.
 */
@Composable
fun VoraListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = VoraDarkGreen,
    iconBackground: Color = VoraLightMint,
    titleColor: Color = VoraTextPrimary,
    showChevron: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .then(clickModifier)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = titleColor)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
            }
        }
        if (trailing != null) {
            trailing()
        } else if (showChevron && onClick != null) {
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = VoraTextSecondary
            )
        }
    }
}

/** Línea divisoria sutil entre filas. */
@Composable
fun VoraDividerLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(VoraDivider)
    )
}

/**
 * Chip seleccionable (filtros de tipo de comida, días de la semana, objetivos).
 * Seleccionado: fondo verde oscuro + texto blanco. No seleccionado: blanco con borde menta.
 */
@Composable
fun VoraChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(50)
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = if (selected) Color.White else VoraTextPrimary,
        modifier = modifier
            .clip(shape)
            .background(if (selected) VoraDarkGreen else VoraSurface)
            .border(1.dp, if (selected) VoraDarkGreen else VoraLightMintBorder, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

/**
 * Mosaico de estadística: ícono, valor grande y etiqueta ("8,432 Steps", "45 min").
 */
@Composable
fun VoraStatTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    VoraCard(modifier = modifier, onClick = onClick) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(VoraLightMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = VoraDarkGreen, modifier = Modifier.size(20.dp))
            }
        }
        Text(value, style = MaterialTheme.typography.titleLarge, color = VoraTextPrimary)
        Text(label, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
    }
}

/**
 * Campo de texto con el estilo VORA (bordes redondeados, foco verde oscuro y
 * mensaje de error en rojo debajo cuando [errorText] no es null).
 */
@Composable
fun VoraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    leadingIcon: ImageVector? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            singleLine = singleLine,
            isError = errorText != null,
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VoraDarkGreen,
                unfocusedBorderColor = VoraLightMintBorder,
                focusedLabelColor = VoraDarkGreen,
                cursorColor = VoraDarkGreen,
                focusedContainerColor = VoraSurface,
                unfocusedContainerColor = VoraSurface,
                errorBorderColor = VoraStatusError,
                errorLabelColor = VoraStatusError
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorText != null) {
            Text(
                text = errorText,
                color = VoraStatusError,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}

/** Etiqueta pequeña tipo "badge" (ej. "Demo", "Offline", "Intermediate"). */
@Composable
fun VoraBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = VoraLightMint,
    contentColor: Color = VoraDarkGreen
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = contentColor,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
