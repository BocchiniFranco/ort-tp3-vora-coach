package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
// rememberLazyListState: permite hacer scroll programático al último mensaje.
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ChatAuthor
import ar.edu.ort.voracoach.model.ChatMessage
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraChip
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTopBar
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMintBorder
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.viewmodel.CoachConversationUiState

/**
 * 3.5 CoachConversationScreen (Demostración): chat de texto con VORA.
 *
 * - Los mensajes viven en CoachConversationViewModel (sobreviven a la rotación).
 * - El campo de texto es estado local de la UI (rememberSaveable), como en el ejemplo.
 * - imePadding(): el teclado empuja el campo hacia arriba en vez de taparlo (UI03).
 *
 * @param onSendMessage envía el texto al ViewModel.
 * @param onMicClick vuelve al modo voz (3.2 Listening).
 */
@Composable
fun CoachConversationScreen(
    uiState: CoachConversationUiState,
    onBackClick: () -> Unit,
    onMicClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var draft by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Cada vez que llega un mensaje nuevo (o aparece "typing"), se hace scroll al final.
    val itemCount = uiState.messages.size + if (uiState.isVoraTyping) 1 else 0
    LaunchedEffect(itemCount) {
        if (itemCount > 0) listState.animateScrollToItem(itemCount - 1)
    }

    // Envía y limpia el borrador (se reutiliza en el botón y en la tecla "Send" del teclado).
    val send = {
        if (draft.isNotBlank()) {
            onSendMessage(draft)
            draft = ""
        }
    }

    Column(modifier = modifier.fillMaxSize().imePadding()) {
        VoraTopBar(title = "Chat with Vora", onBack = onBackClick) {
            VoraIconCircleButton(icon = Icons.Rounded.Mic, contentDescription = "Talk to Vora", onClick = onMicClick)
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = VoraScreenPadding, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.messages, key = { it.id }) { message -> ChatBubble(message) }
            if (uiState.isVoraTyping) {
                item(key = "typing") {
                    ChatBubble(ChatMessage(id = -1, author = ChatAuthor.VORA, text = "Vora is typing..."), isTyping = true)
                }
            }
        }
        // Sugerencias rápidas desplazables.
        LazyRow(
            contentPadding = PaddingValues(horizontal = VoraScreenPadding, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.suggestions) { suggestion ->
                VoraChip(text = suggestion, selected = false, onClick = { onSendMessage(suggestion) })
            }
        }
        // Campo de texto + botón enviar.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = VoraScreenPadding, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                placeholder = { Text("Ask Vora anything...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { send() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VoraDarkGreen,
                    unfocusedBorderColor = VoraLightMintBorder,
                    focusedContainerColor = VoraSurface,
                    unfocusedContainerColor = VoraSurface,
                    cursorColor = VoraDarkGreen
                )
            )
            FilledIconButton(
                onClick = { send() },
                enabled = draft.isNotBlank() && !uiState.isVoraTyping,
                modifier = Modifier.size(52.dp),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = VoraDarkGreen, contentColor = Color.White)
            ) {
                Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "Send")
            }
        }
    }
}

/**
 * Burbuja de chat: el usuario a la derecha (verde oscuro), VORA a la izquierda (blanca con orbe).
 */
@Composable
private fun ChatBubble(message: ChatMessage, isTyping: Boolean = false) {
    val isUser = message.author == ChatAuthor.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            VoraOrbGraphic(state = if (isTyping) OrbState.PROCESSING else OrbState.IDLE, size = 32.dp)
            Box(Modifier.size(8.dp))
        }
        Text(
            text = message.text,
            style = MaterialTheme.typography.bodyLarge,
            color = when {
                isUser -> Color.White
                isTyping -> VoraTextSecondary
                else -> VoraTextPrimary
            },
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp, topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .background(if (isUser) VoraDarkGreen else VoraSurface)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7)
@Composable
private fun CoachConversationPreview() {
    VoraCoachTheme {
        CoachConversationScreen(CoachConversationUiState(), {}, {}, {})
    }
}
