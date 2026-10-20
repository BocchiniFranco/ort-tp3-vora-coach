// Subpaquete graphs: un archivo por flujo. Cada integrante edita SOLO el de sus flujos,
// así se evitan conflictos de merge en main y git blame refleja la autoría.
package ar.edu.ort.voracoach.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
// collectAsStateWithLifecycle: convierte el StateFlow del ViewModel en State de Compose
// y deja de observar cuando la pantalla no está visible (ahorra recursos).
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.navigateToHomeAfterLogin
import ar.edu.ort.voracoach.ui.screens.login.LoginScreen
import ar.edu.ort.voracoach.ui.screens.register.RegisterScreen
import ar.edu.ort.voracoach.viewmodel.AuthUiState
import ar.edu.ort.voracoach.viewmodel.AuthViewModel

/**
 * FLUJO 1 - ACCESO (Integrante 1): 1.1 Login y 1.2 Register.
 *
 * [authViewModel] se crea UNA vez en VoraNavHost y se comparte entre ambas pantallas
 * (y con 7.6 App Settings para cerrar sesión), igual que en el ejemplo de la cátedra.
 */
fun NavGraphBuilder.accessGraph(navController: NavHostController, authViewModel: AuthViewModel) {

    // 1.1 Login
    composable(Screen.Login.route) {
        val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
        // Efecto de navegación: cuando el estado pasa a Authenticated se va al Home una sola vez.
        LaunchedEffect(uiState) {
            if (uiState is AuthUiState.Authenticated) {
                navController.navigateToHomeAfterLogin()
                authViewModel.onNavigationHandled()
            }
        }
        LoginScreen(
            uiState = uiState,
            onLoginClick = authViewModel::signIn,
            onDemoLoginClick = authViewModel::signInAsDemoUser,
            onRegisterClick = {
                authViewModel.clearErrors()
                navController.navigate(Screen.Register.route)
            }
        )
    }

    // 1.2 Register
    composable(Screen.Register.route) {
        val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(uiState) {
            if (uiState is AuthUiState.Authenticated) {
                navController.navigateToHomeAfterLogin()
                authViewModel.onNavigationHandled()
            }
        }
        RegisterScreen(
            uiState = uiState,
            onCreateAccountClick = authViewModel::register,
            onBackToLoginClick = {
                authViewModel.clearErrors()
                navController.popBackStack()
            }
        )
    }
}
