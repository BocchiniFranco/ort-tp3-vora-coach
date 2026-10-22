// Subpaquete de la pantalla 1.2 Register (Figma "Login - 02 — SignUp").
package ar.edu.ort.voracoach.ui.screens.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.screens.login.AuthAiBadge
import ar.edu.ort.voracoach.ui.screens.login.AuthBackgroundBox
import ar.edu.ort.voracoach.ui.screens.login.AuthBrandColumn
import ar.edu.ort.voracoach.ui.screens.login.AuthBrandTagline
import ar.edu.ort.voracoach.ui.screens.login.AuthBrandTitle
import ar.edu.ort.voracoach.ui.screens.login.AuthFieldError
import ar.edu.ort.voracoach.ui.screens.login.AuthFieldStyle
import ar.edu.ort.voracoach.ui.screens.login.AuthInfoDialog
import ar.edu.ort.voracoach.ui.screens.login.AuthLinkText
import ar.edu.ort.voracoach.ui.screens.login.AuthOrDivider
import ar.edu.ort.voracoach.ui.screens.login.AuthPrimaryButton
import ar.edu.ort.voracoach.ui.screens.login.AuthSocialRow
import ar.edu.ort.voracoach.ui.screens.login.AuthTextField
import ar.edu.ort.voracoach.ui.screens.login.AuthTopBar
import ar.edu.ort.voracoach.ui.screens.login.authText
import ar.edu.ort.voracoach.ui.theme.AuthBorder
import ar.edu.ort.voracoach.ui.theme.AuthFieldBorder
import ar.edu.ort.voracoach.ui.theme.AuthGreen
import ar.edu.ort.voracoach.ui.theme.AuthLime
import ar.edu.ort.voracoach.ui.theme.AuthLink
import ar.edu.ort.voracoach.ui.theme.AuthMuted
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.viewmodel.AuthUiState

/**
 * 1.2 RegisterScreen (Demostración) — réplica del frame "Login - 02 — SignUp" del Figma.
 *
 * Formulario validado localmente por AuthViewModel (nombre, email y contraseña de 8+
 * caracteres). No crea cuentas reales: al validar, ingresa como el usuario de demostración.
 *
 * @param onCreateAccountClick envía los datos para validar ("Sign Up →").
 * @param onSocialSignUpClick Apple / Google / Face ID (demo).
 * @param onBackToLoginClick vuelve a 1.1 (flecha "<", link "Sign In" y botón Atrás del sistema).
 */
@Composable
fun RegisterScreen(
    uiState: AuthUiState,
    onCreateAccountClick: (name: String, email: String, password: String) -> Unit,
    onSocialSignUpClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    // Diálogos informativos de los links sin backend ("Password help?" y "Recover").
    var infoDialog by rememberSaveable { mutableStateOf<String?>(null) }

    val isLoading = uiState is AuthUiState.Loading
    val errors = uiState as? AuthUiState.Error

    AuthBackgroundBox(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp)
        ) {
            AuthTopBar(onBackClick = onBackToLoginClick)

            // ---------------- Orbe y marca ----------------
            AuthBrandColumn {
                Spacer(Modifier.height(12.dp))
                RegisterOrb()
                Spacer(Modifier.height(10.dp))
                AuthBrandTitle(sizeSp = 28, letterSpacing = -0.7f, lineHeight = 28)
                Spacer(Modifier.height(5.dp))
                AuthBrandTagline(sizeSp = 11, letterSpacing = 1.98f, color = AuthLink)
                Spacer(Modifier.height(20.dp))
            }

            // ---------------- Tarjeta del formulario ----------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), ambientColor = AuthGreen.copy(alpha = 0.04f), spotColor = AuthGreen.copy(alpha = 0.06f))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(1.dp, AuthBorder, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                RegisterField("Full Name") {
                    AuthTextField(
                        value = name, onValueChange = { name = it }, placeholder = "Alex Morgan",
                        leadingIcon = R.drawable.ic_reg_user, fieldStyle = AuthFieldStyle.REGISTER,
                        isError = errors?.nameError != null,
                        textColor = AuthGreen, placeholderColor = AuthFieldBorder, iconTint = AuthGreen
                    )
                    AuthFieldError(errors?.nameError)
                }
                RegisterField("Email Address") {
                    AuthTextField(
                        value = email, onValueChange = { email = it }, placeholder = "alex.morgan@email.com",
                        leadingIcon = R.drawable.ic_reg_mail, fieldStyle = AuthFieldStyle.REGISTER,
                        keyboardType = KeyboardType.Email, isError = errors?.emailError != null,
                        textColor = AuthGreen, placeholderColor = AuthFieldBorder, iconTint = AuthGreen
                    )
                    AuthFieldError(errors?.emailError)
                }
                RegisterField("Password") {
                    AuthTextField(
                        value = password, onValueChange = { password = it }, placeholder = "At least 8 characters",
                        leadingIcon = R.drawable.ic_reg_lock, fieldStyle = AuthFieldStyle.REGISTER,
                        keyboardType = KeyboardType.Password, isPassword = true,
                        passwordToggleIcon = R.drawable.ic_reg_eye, isError = errors?.passwordError != null,
                        textColor = AuthGreen, placeholderColor = AuthFieldBorder, iconTint = AuthGreen
                    )
                    AuthFieldError(errors?.passwordError)
                }
                // Nota de seguridad: "✓ 8+ secure characters" y link "Password help?".
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(painterResource(R.drawable.ic_reg_check_circle), contentDescription = null, tint = AuthGreen, modifier = Modifier.size(12.5.dp))
                        Text("8+ secure characters", style = authText(11, FontWeight.Normal, lineHeight = 16), color = AuthGreen)
                    }
                    Text(
                        "Password help?",
                        style = authText(11, FontWeight.Normal, lineHeight = 16).copy(textDecoration = TextDecoration.Underline),
                        color = AuthGreen,
                        modifier = Modifier.clickable { infoDialog = "Use at least 8 characters. Mixing letters, numbers and symbols makes it stronger." }
                    )
                }
                AuthPrimaryButton(
                    text = "Sign Up",
                    arrowIcon = R.drawable.ic_reg_arrow_right,
                    onClick = { onCreateAccountClick(name, email, password) },
                    isLoading = isLoading,
                    height = 48.dp
                )
                AuthOrDivider(text = "Or sign up with", showLines = false)
                AuthSocialRow(
                    appleIcon = R.drawable.ic_reg_logo_apple,
                    googleIcon = R.drawable.ic_reg_logo_google,
                    onSocialClick = onSocialSignUpClick,
                    gap = 10.dp
                )
            }

            // ---------------- Pie de confianza y recuperación ----------------
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Icon(painterResource(R.drawable.ic_reg_shield), contentDescription = null, tint = AuthGreen, modifier = Modifier.width(10.dp).height(12.5.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Biometric sign-in", style = authText(16, FontWeight.Normal, lineHeight = 24), color = AuthGreen)
                }
                AuthLinkText(
                    prefix = "Having account issues? ", link = "Recover", sizeSp = 16, color = AuthGreen,
                    onClick = { infoDialog = "Account recovery isn't available in this demo. Create the account again or sign in with Apple, Google or Face ID." }
                )
            }
            Box(modifier = Modifier.fillMaxWidth().padding(top = 28.dp, bottom = 4.dp), contentAlignment = Alignment.Center) {
                AuthLinkText(prefix = "Already have an account? ", link = "Sign In", sizeSp = 14, color = AuthMuted, onClick = onBackToLoginClick)
            }
        }
    }

    infoDialog?.let { message ->
        AuthInfoDialog(title = "VORA Coach", message = message, onDismiss = { infoDialog = null })
    }
}

/** Campo del registro: etiqueta 16 Regular arriba y el campo 4 dp debajo. */
@Composable
private fun RegisterField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = authText(16, FontWeight.Normal, lineHeight = 24), color = AuthGreen)
        content()
    }
}

/**
 * Orbe de marca del registro: 56 dp con degradado a 45° (#0E382F → #34D399 → #CCE65E),
 * resplandor menta, punto blanco central y etiqueta "AI" lima.
 */
@Composable
private fun RegisterOrb() {
    Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
        // Resplandor: círculo menta al 30 % desenfocado (aproximado con degradado radial).
        Canvas(modifier = Modifier.size(72.dp)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x4D34D399), Color(0x0034D399)),
                    center = center,
                    radius = size.minDimension / 2f
                )
            )
        }
        Box(modifier = Modifier.size(56.dp).shadow(8.dp, CircleShape), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(AuthGreen, Color(0xFF34D399), AuthLime),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, 0f)
                    )
                )
                drawCircle(color = Color.White.copy(alpha = 0.3f), radius = 10.dp.toPx())
                drawCircle(color = Color.White, radius = 5.dp.toPx())
            }
        }
        AuthAiBadge(textColor = AuthLime, borderColor = Color(0x4D064E3B), offsetX = 6.dp)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 903)
@Composable
private fun RegisterScreenPreview() {
    VoraCoachTheme {
        RegisterScreen(AuthUiState.Idle, { _, _, _ -> }, {}, {})
    }
}
