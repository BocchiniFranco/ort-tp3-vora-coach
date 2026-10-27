// Subpaquete de la pantalla 1.1 Login (Figma "Login - 01 — SignIn").
package ar.edu.ort.voracoach.ui.screens.login

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
// --- Estado de Compose ---
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
// rememberSaveable: conserva lo escrito aunque se rote la pantalla.
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.theme.AuthBorder
import ar.edu.ort.voracoach.ui.theme.AuthGreen
import ar.edu.ort.voracoach.ui.theme.AuthLink
import ar.edu.ort.voracoach.ui.theme.AuthMuted
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
// --- Estado de la pantalla ---
import ar.edu.ort.voracoach.viewmodel.AuthUiState

/**
 * 1.1 LoginScreen (Demostración) — réplica del frame "Login - 01 — SignIn" del Figma.
 *
 * Pantalla SIN estado de negocio ("stateless"): recibe [uiState] y expone eventos como
 * lambdas. Solo guarda localmente lo que el usuario escribe (rememberSaveable), igual que
 * LoginScreen del proyecto de ejemplo. VoraNavHost conecta los eventos con AuthViewModel.
 *
 * Sin backend real (consigna): cualquier email válido con 8+ caracteres de contraseña, o
 * los botones Apple / Google / Face ID, ingresan como el usuario de demo Alex Morgan.
 *
 * @param onBackClick botón "<": sale de la app (Login es la primera pantalla).
 * @param onLoginClick valida y "loguea" (sin backend real).
 * @param onSocialLoginClick acceso alternativo de demostración.
 * @param onRegisterClick navega a 1.2 Register ("Sign up here").
 */
@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onBackClick: () -> Unit,
    onLoginClick: (email: String, password: String) -> Unit,
    onSocialLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    // Diálogo informativo de "Forgot your password?" (no hay recuperación real en la demo).
    var showForgotDialog by rememberSaveable { mutableStateOf(false) }

    val isLoading = uiState is AuthUiState.Loading
    // "as?" = cast seguro: null si el estado no es Error.
    val errors = uiState as? AuthUiState.Error

    AuthBackgroundBox(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // UI03: nada se corta en pantallas chicas
                .imePadding() // el teclado no tapa los campos
                .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp)
        ) {
            AuthTopBar(onBackClick = onBackClick)

            // ---------------- Marca y bienvenida ----------------
            Column(modifier = Modifier.padding(top = 38.dp, bottom = 28.dp)) {
                AuthBrandColumn {
                    LoginOrb()
                    Spacer(Modifier.height(16.dp))
                    AuthBrandTitle(sizeSp = 24, letterSpacing = -0.6f, lineHeight = 32)
                    Spacer(Modifier.height(4.dp))
                    AuthBrandTagline(sizeSp = 12, letterSpacing = 1.2f, color = Color(0xCC065F46))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Your strength, nutrition, and progress plan\nin one place.",
                        style = authText(14, FontWeight.Normal, lineHeight = 20),
                        color = AuthMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ---------------- Tarjeta del formulario ----------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), ambientColor = AuthGreen.copy(alpha = 0.04f), spotColor = AuthGreen.copy(alpha = 0.06f))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(1.dp, AuthBorder, RoundedCornerShape(24.dp))
                    .padding(25.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Email
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "Email Address",
                            style = authText(12, FontWeight.Bold, letterSpacing = 0.3f, lineHeight = 16),
                            color = AuthGreen,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        AuthTextField(
                            value = email, onValueChange = { email = it },
                            placeholder = "alex.morgan@email.com",
                            leadingIcon = R.drawable.ic_mail,
                            fieldStyle = AuthFieldStyle.LOGIN,
                            keyboardType = KeyboardType.Email,
                            isError = errors?.emailError != null
                        )
                        AuthFieldError(errors?.emailError)
                    }
                    // Contraseña + "Forgot your password?"
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Password", style = authText(12, FontWeight.Bold, letterSpacing = 0.3f, lineHeight = 16), color = AuthGreen)
                            Text(
                                "Forgot your password?",
                                style = authText(12, FontWeight.Normal, lineHeight = 16),
                                color = AuthLink,
                                modifier = Modifier.clickable(enabled = !isLoading) { showForgotDialog = true }
                            )
                        }
                        AuthTextField(
                            value = password, onValueChange = { password = it },
                            placeholder = "••••••••••••",
                            leadingIcon = R.drawable.ic_lock,
                            fieldStyle = AuthFieldStyle.LOGIN,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            isError = errors?.passwordError != null
                        )
                        AuthFieldError(errors?.passwordError)
                    }
                    AuthPrimaryButton(
                        text = "Sign In",
                        arrowIcon = R.drawable.ic_arrow_right,
                        onClick = { onLoginClick(email, password) },
                        isLoading = isLoading
                    )
                }
                AuthOrDivider(text = "Or continue with", showLines = true)
                AuthSocialRow(
                    appleIcon = R.drawable.ic_logo_apple,
                    googleIcon = R.drawable.ic_logo_google,
                    onSocialClick = onSocialLoginClick,
                    gap = 12.dp
                )
            }

            // ---------------- Garantía de seguridad ----------------
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_shield_check), contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("End-to-end encrypted biometric sign-in", style = authText(12, FontWeight.Normal, lineHeight = 16), color = AuthMuted)
            }

            // ---------------- Pie: registro ----------------
            Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 4.dp), contentAlignment = Alignment.Center) {
                AuthLinkText(
                    prefix = "Don't have an account? ",
                    link = "Sign up here",
                    sizeSp = 14,
                    color = AuthMuted,
                    onClick = onRegisterClick
                )
            }
        }
    }

    if (showForgotDialog) {
        AuthInfoDialog(
            title = "Reset password",
            message = "Password recovery isn't available in this demo. Sign in with any valid email or use Apple, Google or Face ID.",
            onDismiss = { showForgotDialog = false }
        )
    }
}

/**
 * Orbe de marca del Login: círculo de 64 dp con degradado radial verde (7 paradas del Figma),
 * aro blanco de 4 dp, sombra suave, anillo interno translúcido y la etiqueta "AI".
 */
@Composable
private fun LoginOrb() {
    Box(modifier = Modifier.size(64.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(10.dp, CircleShape)
                .border(4.dp, Color.White, CircleShape)
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF86EFAC), 0.225f to Color(0xFF5DE1A3), 0.45f to Color(0xFF34D399),
                            0.5875f to Color(0xFF2BAC7F), 0.725f to Color(0xFF218664), 0.8625f to Color(0xFF185F4A),
                            1f to Color(0xFF0E382F)
                        ),
                        // Centro del degradado en (22.4, 22.4) de 64 → 35 % del ancho; radio 58,8.
                        center = Offset(size.width * 0.35f, size.height * 0.35f),
                        radius = size.width * 0.919f
                    )
                )
                // Anillo interno decorativo: círculo blanco al 30 % de 20 dp.
                drawCircle(color = Color.White.copy(alpha = 0.3f), radius = 10.dp.toPx())
            }
        }
        AuthAiBadge(textColor = Color(0xFF6EE7B7), borderColor = Color.White, offsetX = 4.dp)
    }
}

/** @Preview: permite ver la pantalla en el panel de diseño de Android Studio sin ejecutar la app. */
@Preview(showBackground = true, widthDp = 390, heightDp = 884)
@Composable
private fun LoginScreenPreview() {
    VoraCoachTheme {
        LoginScreen(AuthUiState.Idle, {}, { _, _ -> }, {}, {})
    }
}
