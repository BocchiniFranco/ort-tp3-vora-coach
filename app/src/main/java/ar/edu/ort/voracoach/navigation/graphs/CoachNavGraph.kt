package ar.edu.ort.voracoach.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.closeCoach
import ar.edu.ort.voracoach.navigation.navigateReplacing
import ar.edu.ort.voracoach.ui.screens.coach.CoachConversationScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachListeningScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachMicErrorScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachProcessingScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachReadyScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachVoiceResponseScreen
import ar.edu.ort.voracoach.viewmodel.CoachConversationViewModel

/**
 * FLUJO 3 - ASISTENTE VORA (Integrante 1): 6 estados simulados.
 *
 * Diseño de la pila (back stack): los estados TRANSITORIOS (Listening, Processing) se
 * reemplazan entre sí con navigateReplacing(). Ejemplo:
 *   Home -> Ready -> Listening => Home -> Ready -> Processing => Home -> Ready -> Response
 * Así "Atrás" desde la respuesta vuelve a Ready (no a un estado que se auto-avanza) y la
 * "X" (closeCoach) vuelve directo a la pantalla que abrió el asistente.
 */
fun NavGraphBuilder.coachGraph(navController: NavHostController) {

    // 3.1 Coach Ready
    composable(Screen.CoachReady.route) {
        CoachReadyScreen(
            userFirstName = DemoData.alexMorgan.firstName,
            suggestions = DemoData.coachSuggestions,
            onCloseClick = { navController.closeCoach() },
            onMicClick = { navController.navigate(Screen.CoachListening.route) },
            onTypeInsteadClick = { navController.navigate(Screen.CoachConversation.route) },
            onSuggestionClick = { navController.navigate(Screen.CoachConversation.route) },
            onMicSettingsClick = { navController.navigate(Screen.CoachMicError.route) }
        )
    }

    // 3.2 Coach Listening (se auto-avanza a Processing)
    composable(Screen.CoachListening.route) {
        CoachListeningScreen(
            transcript = DemoData.COACH_DEMO_QUESTION,
            onCloseClick = { navController.closeCoach() },
            onCancelClick = { navController.popBackStack() },
            onListeningFinished = {
                navController.navigateReplacing(Screen.CoachProcessing.route, current = Screen.CoachListening)
            }
        )
    }

    // 3.3 Coach Processing (se auto-avanza a Voice Response)
    composable(Screen.CoachProcessing.route) {
        CoachProcessingScreen(
            question = DemoData.COACH_DEMO_QUESTION,
            steps = DemoData.coachProcessingSteps,
            onCloseClick = { navController.closeCoach() },
            onCancelClick = { navController.popBackStack() },
            onProcessingFinished = {
                navController.navigateReplacing(Screen.CoachVoiceResponse.route, current = Screen.CoachProcessing)
            }
        )
    }

    // 3.4 Coach Voice Response
    composable(Screen.CoachVoiceResponse.route) {
        CoachVoiceResponseScreen(
            question = DemoData.COACH_DEMO_QUESTION,
            answer = DemoData.COACH_DEMO_ANSWER,
            onCloseClick = { navController.closeCoach() },
            onLogMealClick = { navController.navigate(Screen.AddFood.createRoute(mealType = "DINNER")) },
            onContinueInChatClick = {
                navController.navigateReplacing(Screen.CoachConversation.route, current = Screen.CoachVoiceResponse)
            },
            onAskAgainClick = {
                navController.navigateReplacing(Screen.CoachListening.route, current = Screen.CoachVoiceResponse)
            }
        )
    }

    // 3.5 Coach Conversation (chat con ViewModel propio)
    composable(Screen.CoachConversation.route) {
        val viewModel: CoachConversationViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        CoachConversationScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onMicClick = { navController.navigate(Screen.CoachListening.route) },
            onSendMessage = viewModel::sendMessage
        )
    }

    // 3.6 Coach Mic Error
    composable(Screen.CoachMicError.route) {
        CoachMicErrorScreen(
            onCloseClick = { navController.closeCoach() },
            onTryAgainClick = {
                navController.navigateReplacing(Screen.CoachListening.route, current = Screen.CoachMicError)
            },
            onTypeInsteadClick = {
                navController.navigateReplacing(Screen.CoachConversation.route, current = Screen.CoachMicError)
            }
        )
    }
}
