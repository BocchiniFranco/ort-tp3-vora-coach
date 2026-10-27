// Paquete viewmodel: un par XUiState + XViewModel por pantalla o flujo (igual que el ejemplo de la cátedra).
package ar.edu.ort.voracoach.viewmodel

/**
 * Estados posibles de las pantallas de Acceso (1.1 Login y 1.2 Register).
 *
 * "sealed interface": el compilador conoce todas las implementaciones, así un
 * when (uiState) en la UI obliga a contemplar cada estado.
 */
sealed interface AuthUiState {
    /** Formulario listo para completar. */
    data object Idle : AuthUiState

    /** Validando (simulado): deshabilita los botones y muestra un indicador. */
    data object Loading : AuthUiState

    /** Sesión iniciada: VoraNavHost navega al Home y saca Login de la pila. */
    data object Authenticated : AuthUiState

    /**
     * Error de validación de un formulario.
     * Cada campo tiene su mensaje (null = campo válido) para mostrarlo debajo del input.
     */
    data class Error(
        val nameError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null
    ) : AuthUiState
}
