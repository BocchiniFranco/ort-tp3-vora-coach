package ar.edu.ort.voracoach.ui.components

// --- Foundation: dibujo, bordes, clicks y layouts ---
import androidx.compose.foundation.BorderStroke
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
// KeyboardOptions: tipo de teclado (numérico para calorías/macros, email, contraseña).
import androidx.compose.foundation.text.KeyboardOptions
// --- Íconos de Material (solo para la flecha ">" de las filas de lista) ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
// --- Material 3 ---
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
// painterResource: carga un VectorDrawable de res/drawable (íconos exportados del Figma).
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
// --- Paleta VORA (valores del sistema de diseño del Figma) ---
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMintTag
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary

/** Margen lateral estándar de las pantallas (20 dp: 20 + 350 + 20 = 390 del Figma). */
val VoraScreenPadding = 20.dp

/** Forma de píldora (radio 100 del Figma) para botones, chips y etiquetas. */
val VoraPillShape = RoundedCornerShape(100.dp)

/**
 * VoraTopBar: barra superior de pantallas secundarias con botón volver ("<"),
 * título y acciones opcionales a la derecha (NAV03: volver siempre visible).
 *
 * @param onBack si es null no se muestra la flecha (pantallas raíz).
 * @param backIcon ícono alternativo para el botón (por defecto, el chevron del Figma).
 * @param actions slot para íconos de la derecha (campana, ajustes, calendario...).
 */
@Composable
fun VoraTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backIcon: ImageVector? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            if (backIcon != null) {
                VoraIconCircleButton(icon = backIcon, contentDescription = "Back", onClick = onBack)
            } else {
                VoraIconCircleButton(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Back",
                    onClick = onBack,
                    iconSize = 18
                )
            }
        } else {
            Spacer(Modifier.width(40.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = VoraTextPrimary,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
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
private val IntrinsicActionsWidth = 88.dp

/**
 * Encabezado grande de las pantallas raíz: título de 24 SemiBold a la izquierda,
 * subtítulo opcional y acciones a la derecha (ej. "Notifications" + "Mark all as read").
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically, content = actions)
    }
}

/**
 * Botón circular blanco con borde sutil (#E4E9E6) e ícono de Material.
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
    CircleButtonFrame(onClick, modifier, containerColor) {
        Icon(icon, contentDescription = contentDescription, tint = iconTint, modifier = Modifier.size(20.dp))
    }
}

/**
 * Variante con ícono exportado del Figma (VectorDrawable cargado con painterResource).
 */
@Composable
fun VoraIconCircleButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = VoraSurface,
    iconTint: Color = VoraTextPrimary,
    iconSize: Int = 20
) {
    CircleButtonFrame(onClick, modifier, containerColor) {
        Icon(painter, contentDescription = contentDescription, tint = iconTint, modifier = Modifier.size(iconSize.dp))
    }
}

/** Marco común de los botones circulares: 40 dp, fondo blanco y borde de 1 dp. */
@Composable
private fun CircleButtonFrame(
    onClick: () -> Unit,
    modifier: Modifier,
    containerColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(containerColor)
            .border(1.dp, VoraBorder, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { content() }
}

/**
 * VoraCard: tarjeta blanca con borde #E4E9E6 y radio de 16 dp (patrón "tarjeta" del Figma).
 * Padding interior de 16 dp por defecto.
 */
@Composable
fun VoraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = VoraSurface,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor)
            .border(1.dp, VoraBorder, shape)
            .then(clickModifier)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

/**
 * Botón principal verde (#123D35) en forma de píldora, texto blanco 15 Bold
 * (muestra "Save changes" del Figma: padding 24/14, ancho completo).
 */
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
        modifier = modifier.fillMaxWidth(),
        shape = VoraPillShape,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoraGreen,
            contentColor = Color.White,
            disabledContainerColor = VoraGreen.copy(alpha = 0.35f),
            disabledContentColor = Color.White
        )
    ) {
        ButtonContent(text, icon, fontSize = 15, bold = true)
    }
}

/**
 * CTA lima (#C9E86A) con texto verde (#123D35) 14 SemiBold: va sobre paneles oscuros
 * ("Start Workout", "Start voice session").
 */
@Composable
fun VoraLimeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = VoraPillShape,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = VoraLime, contentColor = VoraGreen)
    ) {
        ButtonContent(text, icon, fontSize = 14, bold = false)
    }
}

/**
 * Acción secundaria sobre panel oscuro ("Details"): fondo transparente,
 * borde blanco al 25 % y texto blanco 14 SemiBold.
 */
@Composable
fun VoraHeroSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = VoraPillShape,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = Color.White)
    ) {
        ButtonContent(text, icon, fontSize = 14, bold = false)
    }
}

/**
 * Botón con borde #E4E9E6 en forma de píldora ("Not Now", "Cancel").
 */
@Composable
fun VoraOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = VoraTextSecondary,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = VoraPillShape,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        border = BorderStroke(1.dp, VoraBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = contentColor)
    ) {
        ButtonContent(text, icon, fontSize = 15, bold = false)
    }
}

/** Contenido compartido de los botones: ícono opcional + texto. */
@Composable
private fun ButtonContent(text: String, icon: ImageVector?, fontSize: Int, bold: Boolean) {
    if (icon != null) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontSize = fontSize.sp,
            fontWeight = if (bold) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.SemiBold
        )
    )
}

/**
 * Encabezado de sección: título 16 SemiBold y acción contextual 13 SemiBold verde
 * ("View History", "See all"). Si [actionText] es null no se muestra la acción.
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
        Text(title, style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp), color = VoraTextPrimary)
        if (actionText != null && onAction != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                color = VoraGreen,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Fila de lista: ícono en cuadro menta, título, subtítulo y flecha ">".
 * Se usa en Perfil, Ajustes y listados.
 */
@Composable
fun VoraListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = VoraGreen,
    iconBackground: Color = VoraMintTag,
    titleColor: Color = VoraTextPrimary,
    showChevron: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(clickModifier)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = titleColor)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
            }
        }
        if (trailing != null) {
            trailing()
        } else if (showChevron && onClick != null) {
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = VoraTextTertiary)
        }
    }
}

/** Línea divisoria de 1 dp (#E4E9E6). */
@Composable
fun VoraDividerLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(VoraBorder)
    )
}

/**
 * Chip tipo "prompt" del Figma: blanco con borde #E4E9E6, píldora, texto 13 Medium.
 * Seleccionado: fondo verde #123D35 y texto blanco.
 */
@Composable
fun VoraChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
        color = if (selected) Color.White else VoraTextPrimary,
        modifier = modifier
            .clip(VoraPillShape)
            .background(if (selected) VoraGreen else VoraSurface)
            .border(1.dp, if (selected) VoraGreen else VoraBorder, VoraPillShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

/**
 * Mosaico de estadística: etiqueta 11 en mayúsculas (texto tenue) y valor 16 Bold
 * (patrón "STEPS / 7,842" del Home).
 */
@Composable
fun VoraStatTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    VoraCard(modifier = modifier, onClick = onClick, contentPadding = PaddingValues(12.dp)) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = VoraGreen, modifier = Modifier.size(18.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Normal), color = VoraTextTertiary)
            Text(value, style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold), color = VoraTextPrimary)
        }
    }
}

/**
 * Campo de formulario del Figma (Edit profile): etiqueta en mayúsculas 11 SemiBold arriba,
 * separada 6 dp del campo blanco con borde #E4E9E6, radio 12 y valor 15 Regular.
 * Si [errorText] no es null el borde pasa a rojo y se muestra el mensaje debajo.
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
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = VoraTextSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder?.let { { Text(it, style = MaterialTheme.typography.bodyLarge, color = VoraTextTertiary) } },
            singleLine = singleLine,
            isError = errorText != null,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = VoraTextPrimary),
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = VoraTextSecondary) } },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VoraGreen,
                unfocusedBorderColor = VoraBorder,
                cursorColor = VoraGreen,
                focusedContainerColor = VoraSurface,
                unfocusedContainerColor = VoraSurface,
                errorBorderColor = VoraStatusError,
                errorContainerColor = VoraSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorText != null) {
            Text(text = errorText, color = VoraStatusError, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** Etiqueta compacta en píldora (patrón "ONLINE": menta al 15 % y texto verde 11 SemiBold). */
@Composable
fun VoraBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = VoraMintTag,
    contentColor: Color = VoraGreen
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = contentColor,
        modifier = modifier
            .clip(VoraPillShape)
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
