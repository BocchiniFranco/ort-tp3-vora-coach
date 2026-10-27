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
import ar.edu.ort.voracoach.navigation.navigateToBottomDestination
import ar.edu.ort.voracoach.ui.components.VoraBottomDestination
import ar.edu.ort.voracoach.ui.screens.coach.CoachConversationScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachListeningScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachMicErrorScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachProcessingScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachReadyScreen
import ar.edu.ort.voracoach.ui.screens.coach.CoachVoiceResponseScreen
import ar.edu.ort.voracoach.viewmodel.CoachConversationViewModel

/**
 * FLUJO 3 - ASISTENTE VORA (Integrante 1): 6 estados del Figma "AI Voice Flow".
 *
 * Diseño de la pila (back stack): los estados TRANSITORIOS (Listening, Processing, Speaking,
 * Error) se reemplazan entre sí con navigateReplacing(). Ejemplo:
 *   Home → Idle → Listening  ⇒  Home → Idle → Processing  ⇒  Home → Idle → Speaking
 * Así "Atrás" siempre vuelve a Idle (no a un estado que se auto-avanza) y "<" en Idle
 * (closeCoach) vuelve a la pantalla que abrió el asistente.
 */
fun NavGraphBuilder.coachGraph(navController: NavHostController) {

    // 3.1 Voice Idle
    composable(Screen.CoachReady.route) {
        CoachReadyScreen(
            suggestions = DemoData.coachSuggestions,
            lastConversationText = DemoData.LAST_CONVERSATION_TEXT,
            lastConversationTime = DemoData.LAST_CONVERSATION_TIME,
            onBackClick = { navController.closeCoach() },
            onMicClick = { navController.navigate(Screen.CoachListening.route) },
            onTypeInsteadClick = { navController.navigate(Screen.CoachConversation.route) },
            onSuggestionClick = { navController.navigate(Screen.CoachConversation.route) },
            onLastConversationClick = { navController.navigate(Screen.CoachConversation.route) }
        )
    }

    // 3.2 Listening (se auto-avanza a Processing; sin voz detectada → Error)
    composable(Screen.CoachListening.route) {
        CoachListeningScreen(
            transcript = DemoData.COACH_DEMO_QUESTION,
            onBackClick = { navController.popBackStack() },
            onCancelClick = { navController.popBackStack() },
            onListeningFinished = {
                navController.navigateReplacing(Screen.CoachProcessing.route, current = Screen.CoachListening)
            },
            onNothingHeard = {
                navController.navigateReplacing(Screen.CoachMicError.route, current = Screen.CoachListening)
            }
        )
    }

    // 3.3 Processing (se auto-avanza a Speaking)
    composable(Screen.CoachProcessing.route) {
        CoachProcessingScreen(
            question = DemoData.COACH_DEMO_QUESTION,
            statusMessage = DemoData.COACH_PROCESSING_MESSAGE,
            onBackClick = { navController.popBackStack() },
            onCancelClick = { navController.popBackStack() },
            onProcessingFinished = {
                navController.navigateReplacing(Screen.CoachVoiceResponse.route, current = Screen.CoachProcessing)
            }
        )
    }

    // 3.4 Speaking (al terminar o con Stop → Conversation; "Tap to Interrupt" → Listening)
    composable(Screen.CoachVoiceResponse.route) {
        CoachVoiceResponseScreen(
            answer = DemoData.COACH_DEMO_ANSWER,
            durationSeconds = DemoData.COACH_ANSWER_DURATION_SECONDS,
            onBackClick = { navController.popBackStack() },
            onInterruptClick = {
                navController.navigateReplacing(Screen.CoachListening.route, current = Screen.CoachVoiceResponse)
            },
            onStopClick = {
                navController.navigateReplacing(Screen.CoachConversation.route, current = Screen.CoachVoiceResponse)
            },
            onFinished = {
                navController.navigateReplacing(Screen.CoachConversation.route, current = Screen.CoachVoiceResponse)
            }
        )
    }

    // 3.5 Conversation (chat con ViewModel propio)
    composable(Screen.CoachConversation.route) {
        val viewModel: CoachConversationViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        CoachConversationScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onMicClick = { navController.navigate(Screen.CoachListening.route) },
            onListenClick = { navController.navigate(Screen.CoachVoiceResponse.route) },
            onLogMealClick = { navController.navigate(Screen.AddFood.createRoute(mealType = "DINNER")) },
            onStartWorkoutClick = { navController.navigateToBottomDestination(VoraBottomDestination.WORKOUT) },
            onSendMessage = viewModel::sendMessage
        )
    }

    // 3.6 Error ("Sorry, I didn't catch that")
    composable(Screen.CoachMicError.route) {
        CoachMicErrorScreen(
            onBackClick = { navController.popBackStack() },
            onAllowAccessClick = {
                navController.navigateReplacing(Screen.CoachListening.route, current = Screen.CoachMicError)
            },
            onNotNowClick = { navController.popBackStack() },
            onTryAgainClick = {
                navController.navigateReplacing(Screen.CoachListening.route, current = Screen.CoachMicError)
            },
            onTypeInsteadClick = {
                navController.navigateReplacing(Screen.CoachConversation.route, current = Screen.CoachMicError)
            }
        )
    }
}
