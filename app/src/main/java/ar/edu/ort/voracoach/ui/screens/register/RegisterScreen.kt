// Subpaquete de la pantalla 1.2 Register.
package ar.edu.ort.voracoach.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTextField
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintProgress
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.viewmodel.AuthUiState

/**
 * 1.2 RegisterScreen (Demostración).
 *
 * Formulario de alta validado localmente por AuthViewModel (nombre, email, contraseña
 * y confirmación). No crea cuentas reales: al validar, ingresa como usuario de demo.
 *
 * @param onCreateAccountClick envía los datos para validar.
 * @param onBackToLoginClick vuelve a 1.1 (flecha "<", link "Log in" y botón Atrás del sistema).
 */
@Composable
fun RegisterScreen(
    uiState: AuthUiState,
    onCreateAccountClick: (name: String, email: String, password: String, confirmPassword: String) -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    // Aceptación de términos: el botón queda deshabilitado hasta marcarla.
    var acceptedTerms by rememberSaveable { mutableStateOf(false) }

    val isLoading = uiState is AuthUiState.Loading
    val errors = uiState as? AuthUiState.Error

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        VoraTopBar(title = "Create account", onBack = onBackToLoginClick)
        Column(
            modifier = Modifier.padding(horizontal = VoraScreenPadding, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Join VORA Coach", style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
            Text(
                "Your AI coach for training and nutrition, personalized to your goals.",
                style = MaterialTheme.typography.bodyLarge,
                color = VoraTextSecondary
            )
            VoraTextField(
                value = name, onValueChange = { name = it }, label = "Full name",
                placeholder = "Alex Morgan", leadingIcon = Icons.Rounded.Person, errorText = errors?.nameError
            )
            VoraTextField(
                value = email, onValueChange = { email = it }, label = "Email",
                keyboardType = KeyboardType.Email, leadingIcon = Icons.Rounded.Email, errorText = errors?.emailError
            )
            VoraTextField(
                value = password, onValueChange = { password = it }, label = "Password",
                keyboardType = KeyboardType.Password, isPassword = true, leadingIcon = Icons.Rounded.Lock,
                errorText = errors?.passwordError
            )
            VoraTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm password",
                keyboardType = KeyboardType.Password, isPassword = true, leadingIcon = Icons.Rounded.Lock,
                errorText = errors?.confirmPasswordError
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = { acceptedTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = VoraDarkGreen)
                )
                Text(
                    "I agree to the Terms of Service and Privacy Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextSecondary
                )
            }
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(54.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VoraDarkGreen, modifier = Modifier.size(28.dp))
                }
            } else {
                VoraPrimaryButton(
                    text = "Create Account",
                    enabled = acceptedTerms,
                    onClick = { onCreateAccountClick(name, email, password, confirmPassword) }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account?", color = VoraTextSecondary, style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onBackToLoginClick, enabled = !isLoading) {
                    Text("Log in", color = VoraMintProgress, style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun RegisterScreenPreview() {
    VoraCoachTheme {
        RegisterScreen(uiState = AuthUiState.Idle, onCreateAccountClick = { _, _, _, _ -> }, onBackToLoginClick = {})
    }
}
