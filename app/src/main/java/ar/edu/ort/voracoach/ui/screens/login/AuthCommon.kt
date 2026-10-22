// Piezas visuales compartidas por 1.1 Login y 1.2 Register (Figma "Login Flow").
package ar.edu.ort.voracoach.ui.screens.login

// @DrawableRes: id de un recurso drawable (íconos exportados del Figma).
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// BasicTextField: campo de texto sin estilo propio; lo decoramos igual al Figma.
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.theme.AuthBackground
import ar.edu.ort.voracoach.ui.theme.AuthBorder
import ar.edu.ort.voracoach.ui.theme.AuthGlow
import ar.edu.ort.voracoach.ui.theme.AuthGreen
import ar.edu.ort.voracoach.ui.theme.AuthLime
import ar.edu.ort.voracoach.ui.theme.AuthMuted
import ar.edu.ort.voracoach.ui.theme.Manrope
import ar.edu.ort.voracoach.ui.theme.VoraStatusError

/**
 * Fondo de las pantallas de acceso: color #F3F9F5 con el brillo circular superior del Figma
 * ("Top Decorative Ambient Glow": círculo #DCF5E9 de 320 dp, desenfocado, al 70 %).
 * El desenfoque se aproxima con un degradado radial (Modifier.blur exige Android 12+).
 */
@Composable
fun AuthBackgroundBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(AuthBackground)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(260.dp)) {
            val radius = 200.dp.toPx()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuthGlow.copy(alpha = 0.7f), AuthGlow.copy(alpha = 0f)),
                    center = Offset(size.width / 2f, 64.dp.toPx()),
                    radius = radius
                ),
                radius = radius,
                center = Offset(size.width / 2f, 64.dp.toPx())
            )
        }
        content()
    }
}

/**
 * Barra superior del acceso: botón "volver" circular de 40 dp y selector de idioma "EN".
 * El selector abre un menú con los idiomas disponibles (solo inglés en la demo).
 */
@Composable
fun AuthTopBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    var languageMenuOpen by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(1.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, AuthBorder, CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(R.drawable.ic_chevron_left), contentDescription = "Back", tint = AuthGreen, modifier = Modifier.size(20.dp))
        }
        Box {
            Row(
                modifier = Modifier
                    .shadow(1.dp, RoundedCornerShape(100.dp))
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White)
                    .border(1.dp, AuthBorder, RoundedCornerShape(100.dp))
                    .clickable { languageMenuOpen = true }
                    .padding(horizontal = 15.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("EN", style = authText(12, FontWeight.Bold, letterSpacing = 0.3f), color = AuthMuted)
                Spacer(Modifier.width(6.dp))
                Icon(
                    painterResource(R.drawable.ic_chevron_down), contentDescription = "Select language",
                    tint = AuthMuted, modifier = Modifier.size(14.dp).alpha(0.7f)
                )
            }
            DropdownMenu(expanded = languageMenuOpen, onDismissRequest = { languageMenuOpen = false }) {
                DropdownMenuItem(text = { Text("English") }, onClick = { languageMenuOpen = false })
                DropdownMenuItem(text = { Text("Español (coming soon)") }, onClick = {}, enabled = false)
            }
        }
    }
}

/** Estilo de texto de las pantallas de acceso (Manrope con tamaño, peso y espaciado del Figma). */
fun authText(sizeSp: Int, weight: FontWeight, letterSpacing: Float = 0f, lineHeight: Int? = null) = TextStyle(
    fontFamily = Manrope,
    fontWeight = weight,
    fontSize = sizeSp.sp,
    letterSpacing = letterSpacing.sp,
    lineHeight = lineHeight?.sp ?: androidx.compose.ui.unit.TextUnit.Unspecified
)

/** Variante visual de los campos: Login (fondo tintado, radio 16) o Register (blanco, radio 12). */
enum class AuthFieldStyle { LOGIN, REGISTER }

/**
 * Campo de texto del acceso, idéntico al Figma: ícono a la izquierda, borde suave,
 * placeholder gris y, en contraseñas, el ícono de "ojo" para mostrar u ocultar.
 * Con [isError] el borde pasa a rojo (#D95D5D).
 */
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    @DrawableRes leadingIcon: Int,
    fieldStyle: AuthFieldStyle,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    @DrawableRes passwordToggleIcon: Int = R.drawable.ic_eye,
    textColor: Color = AuthGreen,
    placeholderColor: Color = ar.edu.ort.voracoach.ui.theme.AuthPlaceholder,
    iconTint: Color = AuthMuted
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val isLogin = fieldStyle == AuthFieldStyle.LOGIN
    val shape = RoundedCornerShape(if (isLogin) 16.dp else 12.dp)
    val borderColor = when {
        isError -> VoraStatusError
        isLogin -> AuthBorder
        else -> ar.edu.ort.voracoach.ui.theme.AuthFieldBorder
    }
    val fontSize = if (isLogin) 14 else 16
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = authText(fontSize, FontWeight.Normal).copy(color = textColor),
        cursorBrush = SolidColor(AuthGreen),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(if (isLogin) AuthBackground.copy(alpha = 0.6f) else Color.White)
                    .border(1.dp, borderColor, shape)
                    .padding(start = 14.dp, end = 14.dp, top = if (isLogin) 16.dp else 13.dp, bottom = if (isLogin) 16.dp else 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (isLogin) 11.dp else 12.dp)
            ) {
                Icon(painterResource(leadingIcon), contentDescription = null, tint = iconTint, modifier = Modifier.size(if (isLogin) 20.dp else 17.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(placeholder, style = authText(fontSize, FontWeight.Normal), color = placeholderColor, maxLines = 1)
                    }
                    innerTextField()
                }
                if (isPassword) {
                    Icon(
                        painterResource(passwordToggleIcon),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = iconTint,
                        modifier = Modifier.size(20.dp).clickable { passwordVisible = !passwordVisible }
                    )
                }
            }
        }
    )
}

/** Mensaje de validación debajo de un campo (rojo #D95D5D, 12 sp). */
@Composable
fun AuthFieldError(text: String?) {
    if (text != null) {
        Text(text, style = authText(12, FontWeight.Medium), color = VoraStatusError, modifier = Modifier.padding(start = 4.dp))
    }
}

/**
 * Botón principal lima del acceso ("Sign In →" / "Sign Up →"): #CCE65E, radio 16,
 * texto 16 Bold #0E382F con flecha, y sombra lima al 35 %. Muestra un indicador mientras carga.
 */
@Composable
fun AuthPrimaryButton(
    text: String,
    @DrawableRes arrowIcon: Int,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shadow(10.dp, shape, ambientColor = AuthLime.copy(alpha = 0.35f), spotColor = AuthLime.copy(alpha = 0.35f))
            .clip(shape)
            .background(AuthLime)
            .clickable(enabled = !isLoading, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = AuthGreen, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
        } else {
            Text(text, style = authText(16, FontWeight.Bold, lineHeight = 24), color = AuthGreen)
            Spacer(Modifier.width(8.dp))
            Icon(painterResource(arrowIcon), contentDescription = null, tint = AuthGreen, modifier = Modifier.size(16.dp))
        }
    }
}

/**
 * Fila de accesos alternativos: Apple, Google y Face ID (este último con fondo menta).
 * En la demo cualquiera de los tres ingresa como Alex Morgan (no hay OAuth real).
 */
@Composable
fun AuthSocialRow(
    @DrawableRes appleIcon: Int,
    @DrawableRes googleIcon: Int,
    onSocialClick: () -> Unit,
    gap: Dp,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(gap)) {
        SocialButton("Sign in with Apple", onSocialClick, Color.White, AuthBorder) {
            Image(painterResource(appleIcon), contentDescription = null, modifier = Modifier.size(20.dp))
        }
        SocialButton("Sign in with Google", onSocialClick, Color.White, AuthBorder) {
            // Image (y no Icon) para conservar los 4 colores originales del logo de Google.
            Image(painterResource(googleIcon), contentDescription = null, modifier = Modifier.size(20.dp))
        }
        SocialButton("Sign in with Face ID", onSocialClick, Color(0xFFECFDF5), Color(0xCCA7F3D0)) {
            Icon(painterResource(R.drawable.ic_face_id), contentDescription = null, tint = ar.edu.ort.voracoach.ui.theme.AuthLink, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun RowScope.SocialButton(
    description: String,
    onClick: () -> Unit,
    background: Color,
    border: Color,
    icon: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clip(shape)
            .background(background)
            .border(1.dp, border, shape)
            .clickable(onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center
    ) { icon() }
}

/** Separador "Or continue with": líneas finas a los lados (opcionales) y texto 12 sp. */
@Composable
fun AuthOrDivider(text: String, showLines: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.weight(1f).height(1.dp).background(if (showLines) AuthBorder else Color.Transparent))
        Text(text, style = authText(12, FontWeight.Normal, lineHeight = 16), color = AuthMuted, modifier = Modifier.padding(horizontal = 12.dp))
        Box(Modifier.weight(1f).height(1.dp).background(if (showLines) AuthBorder else Color.Transparent))
    }
}

/**
 * Texto con una parte final en negrita subrayada y clickeable
 * (ej. "Don't have an account? **Sign up here**").
 */
@Composable
fun AuthLinkText(prefix: String, link: String, sizeSp: Int, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            append(prefix)
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) { append(link) }
        },
        style = authText(sizeSp, FontWeight.Normal, lineHeight = if (sizeSp >= 16) 24 else 20),
        color = color,
        modifier = modifier.clickable(onClickLabel = link, onClick = onClick)
    )
}

/** Etiqueta "AI" del orbe de marca: píldora oscura con texto de 10 sp. */
@Composable
fun BoxScope.AuthAiBadge(textColor: Color, borderColor: Color, offsetX: Dp) {
    Text(
        text = "AI",
        style = authText(10, FontWeight.Bold, letterSpacing = 0.5f, lineHeight = 15),
        color = textColor,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = offsetX, y = 4.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(AuthGreen)
            .border(1.dp, borderColor, RoundedCornerShape(100.dp))
            .padding(horizontal = 9.dp, vertical = 0.dp)
    )
}

/** Columna de marca centrada (orbe + "VORA Coach" + "ADAPTIVE INTELLIGENCE"). */
@Composable
fun AuthBrandColumn(content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) { content() }
}

/** Texto de título de marca del acceso. */
@Composable
fun AuthBrandTitle(sizeSp: Int, letterSpacing: Float, lineHeight: Int) {
    Text("VORA Coach", style = authText(sizeSp, FontWeight.Bold, letterSpacing, lineHeight), color = AuthGreen)
}

/** Etiqueta de marca "ADAPTIVE INTELLIGENCE". */
@Composable
fun AuthBrandTagline(sizeSp: Int, letterSpacing: Float, color: Color) {
    Text("ADAPTIVE INTELLIGENCE", style = authText(sizeSp, FontWeight.Bold, letterSpacing, lineHeight = if (sizeSp == 12) 16 else 17), color = color)
}

/** Diálogo informativo para enlaces sin backend real ("Forgot your password?", "Recover"). */
@Composable
fun AuthInfoDialog(title: String, message: String, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, style = MaterialTheme.typography.titleSmall) },
        text = { Text(message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) { Text("OK", color = AuthGreen) }
        }
    )
}
