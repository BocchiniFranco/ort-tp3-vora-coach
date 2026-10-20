package ar.edu.ort.voracoach.navigation.graphs

import androidx.compose.runtime.getValue
// hiltViewModel(): pide a Hilt el ViewModel @HiltViewModel ligado a esta entrada de navegación.
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.model.NotificationItem
import ar.edu.ort.voracoach.model.NotificationType
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.navigateToBottomDestination
import ar.edu.ort.voracoach.ui.components.VoraBottomDestination
import ar.edu.ort.voracoach.ui.screens.home.HomeDashboardScreen
import ar.edu.ort.voracoach.ui.screens.home.NotificationsScreen
import ar.edu.ort.voracoach.viewmodel.HomeViewModel
import ar.edu.ort.voracoach.viewmodel.NotificationsViewModel

/**
 * FLUJO 2 - INICIO (Integrante 1): 2.1 Home Dashboard y 2.2 Notifications.
 */
fun NavGraphBuilder.homeGraph(navController: NavHostController) {

    // 2.1 Home: cada tarjeta lleva a un destino coherente (NAV01).
    composable(Screen.HomeDashboard.route) {
        // El ViewModel se pide ACÁ ADENTRO: vive mientras el Home esté en la pila.
        val viewModel: HomeViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        HomeDashboardScreen(
            uiState = uiState,
            onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
            onProfileClick = { navController.navigateToBottomDestination(VoraBottomDestination.PROFILE) },
            onTalkToVoraClick = { navController.navigate(Screen.CoachReady.route) },
            onChatWithVoraClick = { navController.navigate(Screen.CoachConversation.route) },
            onDailyGoalClick = { navController.navigate(Screen.DailySummary.route) },
            onWorkoutClick = { navController.navigateToBottomDestination(VoraBottomDestination.WORKOUT) },
            onWeeklyPlanClick = { navController.navigate(Screen.WeeklyPlan.route) },
            onNutritionClick = { navController.navigateToBottomDestination(VoraBottomDestination.NUTRITION) },
            onLogMealClick = { navController.navigate(Screen.AddFood.createRoute()) },
            onWeeklyReviewClick = { navController.navigate(Screen.WeeklyReview.route) }
        )
    }

    // 2.2 Notifications: al tocar una, se marca leída y se navega según su tipo.
    composable(Screen.Notifications.route) {
        val viewModel: NotificationsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        NotificationsScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onMarkAllReadClick = viewModel::markAllAsRead,
            onNotificationClick = { notification ->
                viewModel.onNotificationOpened(notification.id)
                navController.openNotification(notification)
            }
        )
    }
}

/** Destino de cada tipo de notificación. Las pestañas raíz usan la navegación de BottomBar. */
private fun NavHostController.openNotification(notification: NotificationItem) {
    when (notification.type) {
        NotificationType.WORKOUT -> navigateToBottomDestination(VoraBottomDestination.WORKOUT)
        NotificationType.NUTRITION -> navigate(Screen.AddFood.createRoute())
        NotificationType.COACH -> navigate(Screen.CoachConversation.route)
        NotificationType.GOAL -> navigate(Screen.DailySummary.route)
        NotificationType.PROGRESS -> navigate(Screen.WeeklyReview.route)
    }
}
