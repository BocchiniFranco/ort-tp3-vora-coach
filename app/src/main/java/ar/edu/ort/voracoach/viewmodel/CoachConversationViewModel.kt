package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.voracoach.model.ChatAuthor
import ar.edu.ort.voracoach.model.ChatMessage
import ar.edu.ort.voracoach.model.DemoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado del chat con VORA (3.5 Coach Conversation).
 * @property isVoraTyping true mientras se "genera" la respuesta (muestra "Vora is typing...").
 */
data class CoachConversationUiState(
    val messages: List<ChatMessage> = DemoData.initialConversation,
    val suggestions: List<String> = DemoData.coachSuggestions,
    val isVoraTyping: Boolean = false
)

/**
 * ViewModel del chat de demostración.
 *
 * No hay IA real ni llamadas de red: las respuestas salen de DemoData.cannedReplyFor().
 * Al vivir en un ViewModel, la conversación sobrevive a la rotación de pantalla.
 */
@HiltViewModel
class CoachConversationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CoachConversationUiState())
    val uiState: StateFlow<CoachConversationUiState> = _uiState.asStateFlow()

    // Contador para generar IDs únicos de mensaje (clave estable para LazyColumn).
    private var nextId = DemoData.initialConversation.maxOf { it.id } + 1

    /**
     * Agrega el mensaje del usuario y, tras una pausa simulada, la respuesta de VORA.
     * Ignora textos vacíos o envíos mientras VORA está "escribiendo".
     */
    fun sendMessage(text: String) {
        val clean = text.trim()
        if (clean.isEmpty() || _uiState.value.isVoraTyping) return
        _uiState.update {
            it.copy(messages = it.messages + ChatMessage(nextId++, ChatAuthor.USER, clean), isVoraTyping = true)
        }
        viewModelScope.launch {
            delay(TYPING_DELAY_MS)
            _uiState.update {
                it.copy(
                    messages = it.messages + ChatMessage(nextId++, ChatAuthor.VORA, DemoData.cannedReplyFor(clean)),
                    isVoraTyping = false
                )
            }
        }
    }

    private companion object {
        const val TYPING_DELAY_MS = 1_200L
    }
}
