package ar.edu.ort.voracoach.viewmodel

// Patterns.EMAIL_ADDRESS: expresión regular de Android para validar emails.
import android.util.Patterns
// ViewModel: sobrevive a rotaciones de pantalla; viewModelScope cancela corrutinas al destruirse.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// @HiltViewModel: Hilt sabe construir este ViewModel e inyectarle sus dependencias.
import dagger.hilt.android.lifecycle.HiltViewModel
// delay: pausa una corrutina sin bloquear el hilo principal.
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
// launch: inicia una corrutina dentro del viewModelScope.
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel del Flujo 1 (Acceso). Es COMPARTIDO entre Login y Register: VoraNavHost lo
 * obtiene una sola vez fuera de los composable(...) (mismo criterio que el ejemplo).
 *
 * IMPORTANTE (consigna): NO hay autenticación real ni endpoint falso de login.
 * Solo se validan los formularios localmente y se simula una breve carga; cualquier
 * credencial con formato válido ingresa como el usuario de demostración Alex Morgan.
 *
 * @Inject constructor(): Hilt construye el ViewModel; hoy no tiene dependencias, pero si
 * mañana se integra Firebase Auth se agregaría el repositorio como parámetro aquí.
 */
@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    // Estado interno mutable; la UI solo recibe la versión inmutable (StateFlow).
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * 1.1 Login: valida email y contraseña; si son válidos simula el ingreso.
     */
    fun signIn(email: String, password: String) {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        if (emailError != null || passwordError != null) {
            _uiState.value = AuthUiState.Error(emailError = emailError, passwordError = passwordError)
            return
        }
        simulateAuthentication()
    }

    /**
     * 1.2 Register: valida nombre, email, contraseña y confirmación.
     */
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        val nameError = if (name.isBlank()) "Enter your name" else null
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        val confirmError = if (confirmPassword != password) "Passwords don't match" else null
        if (listOf(nameError, emailError, passwordError, confirmError).any { it != null }) {
            _uiState.value = AuthUiState.Error(nameError, emailError, passwordError, confirmError)
            return
        }
        simulateAuthentication()
    }

    /** Ingreso de demostración (botón "Continue as Alex Morgan"). */
    fun signInAsDemoUser() = simulateAuthentication()

    /**
     * Lo llama VoraNavHost después de navegar al Home, para que el estado Authenticated
     * no vuelva a disparar la navegación (por ejemplo al rotar la pantalla).
     */
    fun onNavigationHandled() {
        _uiState.value = AuthUiState.Idle
    }

    /** Cierre de sesión (7.6 App Settings): vuelve al estado inicial. */
    fun signOut() {
        _uiState.value = AuthUiState.Idle
    }

    /** Limpia los errores al cambiar de pantalla (Login <-> Register). */
    fun clearErrors() {
        if (_uiState.value is AuthUiState.Error) _uiState.value = AuthUiState.Idle
    }

    // Simula la latencia de un servidor para mostrar el estado Loading (sin red real).
    private fun simulateAuthentication() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            delay(SIMULATED_DELAY_MS)
            _uiState.value = AuthUiState.Authenticated
        }
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Enter your email"
        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Enter a valid email"
        else -> null
    }

    private fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Enter your password"
        password.length < MIN_PASSWORD_LENGTH -> "Use at least $MIN_PASSWORD_LENGTH characters"
        else -> null
    }

    // companion object: constantes compartidas por todas las instancias.
    private companion object {
        const val SIMULATED_DELAY_MS = 700L
        const val MIN_PASSWORD_LENGTH = 6
    }
}
