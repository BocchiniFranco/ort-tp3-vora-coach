package ar.edu.ort.voracoach.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination
import ar.edu.ort.voracoach.viewmodel.AuthViewModel

/**
 * FLUJO 7 - PERFIL (Integrante 3).
 *
 * PENDIENTE (Integrante 3): reemplazar cada placeholderDestination(...) por la pantalla real.
 * Para el cierre de sesión de 7.6 App Settings usar:
 *     authViewModel.signOut()
 *     navController.navigateToLoginClearingBackStack()
 */
@Suppress("UNUSED_PARAMETER") // authViewModel se usará en 7.6 App Settings (logout).
fun NavGraphBuilder.profileGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    placeholderDestination(Screen.ProfileDashboard, navController)
    placeholderDestination(Screen.EditProfile, navController)
    placeholderDestination(Screen.GoalsPreferences, navController)
    placeholderDestination(Screen.NutritionPreferences, navController)
    placeholderDestination(Screen.VoiceAiSettings, navController)
    placeholderDestination(Screen.AppSettings, navController)
}
