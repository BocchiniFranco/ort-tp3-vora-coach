package ar.edu.ort.voracoach.navigation.graphs

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.navigateToLoginClearingBackStack
import ar.edu.ort.voracoach.ui.screens.profile.AppSettingsScreen
import ar.edu.ort.voracoach.ui.screens.profile.EditProfileScreen
import ar.edu.ort.voracoach.ui.screens.profile.GoalsPreferencesScreen
import ar.edu.ort.voracoach.ui.screens.profile.NutritionPreferencesScreen
import ar.edu.ort.voracoach.ui.screens.profile.ProfileDashboardScreen
import ar.edu.ort.voracoach.ui.screens.profile.VoiceAiSettingsScreen
import ar.edu.ort.voracoach.viewmodel.AuthViewModel
import ar.edu.ort.voracoach.viewmodel.ProfileViewModel

/**
 * FLUJO 7 - PERFIL (Integrante 3). Datos de demostración de Alex Morgan, sin backend.
 * Las opciones de Settings sin pantalla en el Figma avisan que son de demostración.
 */
fun NavGraphBuilder.profileGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    composable(Screen.ProfileDashboard.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val settings by viewModel.settings.collectAsStateWithLifecycle()

        ProfileDashboardScreen(
            settings = settings,
            onSettingsClick = { navController.navigate(Screen.AppSettings.route) },
            onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },
            onReviewPreferencesClick = { navController.navigate(Screen.VoiceAiSettings.route) },
            onGoalsClick = { navController.navigate(Screen.GoalsPreferences.route) },
            onPreferencesClick = { navController.navigate(Screen.NutritionPreferences.route) },
            onConnectedDataClick = { navController.navigate(Screen.HealthTrends.route) },
            onAccountClick = { navController.navigate(Screen.AppSettings.route) }
        )
    }

    composable(Screen.EditProfile.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val draft by viewModel.draft.collectAsStateWithLifecycle()
        val context = LocalContext.current

        EditProfileScreen(
            draft = draft,
            onDraftChange = viewModel::edit,
            onChangePhotoClick = { context.showDemoToast("Changing the photo") },
            onSaveClick = {
                viewModel.save()
                navController.popBackStack()
            },
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.GoalsPreferences.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val draft by viewModel.draft.collectAsStateWithLifecycle()

        GoalsPreferencesScreen(
            draft = draft,
            onDraftChange = viewModel::edit,
            onSaveClick = {
                viewModel.save()
                navController.popBackStack()
            },
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.NutritionPreferences.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val draft by viewModel.draft.collectAsStateWithLifecycle()

        NutritionPreferencesScreen(
            draft = draft,
            onDraftChange = viewModel::edit,
            onSaveClick = {
                viewModel.save()
                navController.popBackStack()
            },
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.VoiceAiSettings.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val draft by viewModel.draft.collectAsStateWithLifecycle()
        val context = LocalContext.current

        VoiceAiSettingsScreen(
            settings = draft,
            onSettingsChange = { transform ->
                viewModel.edit(transform)
                viewModel.save()
            },
            onPreviewVoiceClick = { context.showDemoToast("Voice preview") },
            onPrivacyClick = { navController.navigate(Screen.AppSettings.route) },
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.AppSettings.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val settings by viewModel.settings.collectAsStateWithLifecycle()
        val showLogoutDialog by viewModel.showLogoutDialog.collectAsStateWithLifecycle()
        val context = LocalContext.current

        AppSettingsScreen(
            settings = settings,
            showLogoutDialog = showLogoutDialog,
            onBackClick = { navController.popBackStack() },
            onAccountClick = { navController.navigate(Screen.EditProfile.route) },
            onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
            onDemoOptionClick = { option -> context.showDemoToast(option) },
            onLogoutClick = viewModel::requestLogout,
            onConfirmLogout = {
                viewModel.dismissLogout()
                authViewModel.signOut()
                // Vacía la pila: después del logout, Atrás no vuelve a la app
                navController.navigateToLoginClearingBackStack()
            },
            onDismissLogout = viewModel::dismissLogout
        )
    }
}

private fun android.content.Context.showDemoToast(feature: String) {
    Toast.makeText(this, "$feature is not available in this demo.", Toast.LENGTH_SHORT).show()
}
