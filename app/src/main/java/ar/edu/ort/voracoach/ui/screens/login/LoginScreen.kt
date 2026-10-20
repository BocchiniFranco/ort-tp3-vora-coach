// Subpaquete de la pantalla 1.1 Login (un subpaquete por pantalla, igual que el ejemplo).
package ar.edu.ort.voracoach.ui.screens.login

// --- Layouts y scroll ---
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
// --- Íconos ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
// --- Material 3 ---
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// --- Sistema de diseño VORA ---
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraOutlinedButton
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTextField
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
// --- Estado de la pantalla ---
import ar.edu.ort.voracoach.viewmodel.AuthUiState

/**
 * 1.1 LoginScreen (Demostración).
 *
 * Pantalla SIN estado de negocio ("stateless"): recibe [uiState] y expone eventos como
 * lambdas. Solo guarda localmente lo que el usuario escribe (rememberSaveable), igual que
 * LoginScreen del proyecto de ejemplo. VoraNavHost conecta los eventos con AuthViewModel
 * y con la navegación.
 *
 * @param onLoginClick valida y "loguea" (sin backend real).
 * @param onDemoLoginClick ingreso directo como Alex Morgan (útil en la defensa).
 * @param onRegisterClick navega a 1.2 Register.
 */
@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onLoginClick: (email: String, password: String) -> Unit,
    onDemoLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    // Diálogo informativo de "Forgot password?" (no hay recuperación real en la demo).
    var showForgotDialog by rememberSaveable { mutableStateOf(false) }

    val isLoading = uiState is AuthUiState.Loading
    // "as?" = cast seguro: null si el estado no es Error.
    val errors = uiState as? AuthUiState.Error

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // UI03: nada se corta en pantallas chicas
            .imePadding() // el teclado no tapa los campos
            .padding(horizontal = VoraScreenPadding, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        VoraOrbGraphic(state = OrbState.IDLE, size = 120.dp)
        Text("Welcome back", style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
        Text(
            text = "Log in to continue with your VORA Coach",
            style = MaterialTheme.typography.bodyLarge,
            color = VoraTextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))

        VoraTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "you@example.com",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Rounded.Email,
            errorText = errors?.emailError
        )
        VoraTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            leadingIcon = Icons.Rounded.Lock,
            errorText = errors?.passwordError
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { showForgotDialog = true }, enabled = !isLoading) {
                Text("Forgot password?", color = VoraDarkGreen)
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(54.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VoraDarkGreen, modifier = Modifier.size(28.dp))
            }
        } else {
            VoraPrimaryButton(text = "Log In", onClick = { onLoginClick(email, password) })
        }
        VoraOutlinedButton(text = "Continue as Alex Morgan (demo)", onClick = onDemoLoginClick)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", color = VoraTextSecondary, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onRegisterClick, enabled = !isLoading) {
                Text("Sign up", color = VoraMintProgress, style = MaterialTheme.typography.titleSmall)
            }
        }

        // Aviso de modo demostración: la consigna pide no simular backend de login.
        Text(
            text = "Demo mode: any valid email and a 6+ character password will sign you in.",
            style = MaterialTheme.typography.bodySmall,
            color = VoraDarkGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VoraLightMint)
                .padding(12.dp)
        )
    }

    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            title = { Text("Reset password") },
            text = { Text("Password recovery isn't available in this demo. Use the demo account to continue.") },
            confirmButton = {
                TextButton(onClick = { showForgotDialog = false }) { Text("OK", color = VoraDarkGreen) }
            }
        )
    }
}

/** @Preview: permite ver la pantalla en el panel de diseño de Android Studio sin ejecutar la app. */
@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun LoginScreenPreview() {
    VoraCoachTheme {
        LoginScreen(uiState = AuthUiState.Idle, onLoginClick = { _, _ -> }, onDemoLoginClick = {}, onRegisterClick = {})
    }
}
