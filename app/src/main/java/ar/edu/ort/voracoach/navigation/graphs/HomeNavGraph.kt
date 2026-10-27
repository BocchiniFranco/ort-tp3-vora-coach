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
            onProfileClick = { navController.navigateToBottomDestination(VoraBottomDestination.PROFILE) },
            onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
            onStartVoiceSessionClick = { navController.navigate(Screen.CoachReady.route) },
            onStartMyWorkoutClick = { navController.navigateToBottomDestination(VoraBottomDestination.WORKOUT) },
            onLogBreakfastClick = { navController.navigate(Screen.AddFood.createRoute(mealType = "BREAKFAST")) },
            onTipClick = { navController.navigate(Screen.CoachConversation.route) },
            onDailyGoalClick = { navController.navigate(Screen.DailySummary.route) },
            onNutritionClick = { navController.navigateToBottomDestination(VoraBottomDestination.NUTRITION) },
            onActivityClick = { navController.navigate(Screen.HealthTrends.route) },
            onRecommendedWorkoutClick = { navController.navigateToBottomDestination(VoraBottomDestination.WORKOUT) },
            onStartWorkoutClick = { navController.navigate(Screen.WorkoutPrep.route) }
        )
    }

    // 2.2 Notifications: al tocar una, se marca leída y se navega según su tipo.
    composable(Screen.Notifications.route) {
        val viewModel: NotificationsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        NotificationsScreen(
            uiState = uiState,
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
        // "Your weekly health review is ready." → 6.6 Weekly Review
        NotificationType.COACH -> navigate(Screen.WeeklyReview.route)
        // "Upper Body Strength is ready." → 4.4 Workout Prep
        NotificationType.WORKOUT -> navigate(Screen.WorkoutPrep.route)
        // "Protein target milestone alert." → pestaña Nutrition
        NotificationType.NUTRITION -> navigateToBottomDestination(VoraBottomDestination.NUTRITION)
        // "4-day workout streak logged!" → 6.3 Workout Progress
        NotificationType.GOAL -> navigate(Screen.WorkoutProgress.route)
        NotificationType.PROGRESS -> navigate(Screen.ProgressDashboard.route)
    }
}
