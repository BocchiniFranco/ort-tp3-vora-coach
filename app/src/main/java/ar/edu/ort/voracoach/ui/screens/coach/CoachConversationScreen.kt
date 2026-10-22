package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// rememberLazyListState: permite hacer scroll programático al último mensaje.
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.ChatAuthor
import ar.edu.ort.voracoach.model.ChatMessage
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.CoachConversationUiState

/**
 * 3.5 CoachConversationScreen (Demostración) — réplica de "Voice / 05 — Conversation".
 *
 * - Los mensajes viven en CoachConversationViewModel (sobreviven a la rotación).
 * - El campo de texto es estado local de la UI (rememberSaveable), como en el ejemplo.
 * - imePadding(): el teclado empuja el panel de escritura en vez de taparlo (UI03).
 *
 * Eventos: "Listen to response" → 3.4 Speaking · micrófono → 3.2 Listening ·
 * "+" → menú con atajos a 5.3 Add Food y a la pestaña Workout.
 */
@Composable
fun CoachConversationScreen(
    uiState: CoachConversationUiState,
    onBackClick: () -> Unit,
    onMicClick: () -> Unit,
    onListenClick: () -> Unit,
    onLogMealClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var draft by rememberSaveable { mutableStateOf("") }
    var attachMenuOpen by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Cada vez que llega un mensaje nuevo (o aparece "typing"), se hace scroll al final.
    val itemCount = 1 + uiState.messages.size + if (uiState.isVoraTyping) 1 else 0
    LaunchedEffect(itemCount) {
        listState.animateScrollToItem(itemCount - 1)
    }

    // Envía y limpia el borrador (se usa en el botón y en la tecla "Send" del teclado).
    val send = {
        if (draft.isNotBlank()) {
            onSendMessage(draft)
            draft = ""
        }
    }

    Column(modifier = modifier.fillMaxSize().background(VoraBackground).imePadding()) {
        // Encabezado blanco con borde inferior, punto "en línea" y orbe a la derecha.
        Column(modifier = Modifier.background(VoraSurface)) {
            VoiceHeader(title = "VORA Coach", onBackClick = onBackClick, dark = false, showOnlineDot = true) {
                VoraOrbGraphic(state = OrbState.IDLE, size = 28.dp)
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(VoraBorder))
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "date") {
                Text(
                    uiState.dateLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = VoraTextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(uiState.messages, key = { it.id }) { message ->
                if (message.author == ChatAuthor.USER) UserBubble(message) else VoraBubble(message, onListenClick)
            }
            if (uiState.isVoraTyping) {
                item(key = "typing") {
                    VoraBubble(ChatMessage(-1, ChatAuthor.VORA, "VORA is typing...", ""), onListenClick, isTyping = true)
                }
            }
        }
        // ---------------- Panel de escritura ----------------
        Column(modifier = Modifier.background(VoraSurface)) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(VoraBorder))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box {
                    CircleIconButton(R.drawable.ic_plus, "More actions", VoraBackground, VoraTextSecondary, 20) { attachMenuOpen = true }
                    DropdownMenu(expanded = attachMenuOpen, onDismissRequest = { attachMenuOpen = false }) {
                        DropdownMenuItem(text = { Text("Log a meal") }, onClick = { attachMenuOpen = false; onLogMealClick() })
                        DropdownMenuItem(text = { Text("Start a workout") }, onClick = { attachMenuOpen = false; onStartWorkoutClick() })
                    }
                }
                // Campo gris redondeado con el micrófono adentro.
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(VoraBackground)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (draft.isEmpty()) {
                            Text("Ask VORA anything...", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal), color = VoraTextSecondary)
                        }
                        BasicTextField(
                            value = draft,
                            onValueChange = { draft = it },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal, color = VoraTextPrimary),
                            cursorBrush = SolidColor(VoraGreen),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { send() }),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Icon(
                        painterResource(R.drawable.ic_mic),
                        contentDescription = "Talk to VORA",
                        tint = VoraTextTertiary,
                        modifier = Modifier.size(18.dp).clickable(onClick = onMicClick)
                    )
                }
                CircleIconButton(R.drawable.ic_send, "Send", VoraGreen, Color.White, 18) { send() }
            }
        }
    }
}

/** Burbuja del usuario: verde #123D35, 280 dp, esquina inferior derecha de 4 dp, hora a la derecha. */
@Composable
private fun UserBubble(message: ChatMessage) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 4.dp, bottomStart = 16.dp))
                .background(VoraGreen)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(message.text, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal), color = Color.White)
            Text(message.time, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal), color = Color.White.copy(alpha = 0.6f), textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
        }
    }
}

/**
 * Burbuja de VORA: orbe de 28 dp + tarjeta blanca de 270 dp con borde, esquina inferior
 * izquierda de 4 dp. Si el mensaje tiene audio, muestra "Listen to response (0:14)".
 */
@Composable
private fun VoraBubble(message: ChatMessage, onListenClick: () -> Unit, isTyping: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        VoraOrbGraphic(state = if (isTyping) OrbState.PROCESSING else OrbState.IDLE, size = 28.dp)
        val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp)
        Column(
            modifier = Modifier
                .width(270.dp)
                .clip(shape)
                .background(VoraSurface)
                .border(1.dp, VoraBorder, shape)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(if (message.audioDuration != null) 10.dp else 4.dp)
        ) {
            Text(
                message.text,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal, lineHeight = 19.6.sp),
                color = if (isTyping) VoraTextSecondary else VoraTextPrimary
            )
            if (message.audioDuration != null) {
                Box(Modifier.fillMaxWidth().height(1.dp).background(VoraBorder))
                Row(
                    modifier = Modifier.clickable(onClick = onListenClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(Modifier.size(24.dp).clip(CircleShape).background(VoraBackground), contentAlignment = Alignment.Center) {
                        Icon(painterResource(R.drawable.ic_play), contentDescription = null, tint = VoraGreen, modifier = Modifier.size(10.dp))
                    }
                    Text("Listen to response (${message.audioDuration})", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraTextSecondary)
                }
            } else if (message.time.isNotEmpty()) {
                Text(message.time, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal), color = VoraTextTertiary)
            }
        }
    }
}

/** Botón circular de 40 dp del panel de escritura ("+" y "enviar"). */
@Composable
private fun CircleIconButton(iconRes: Int, description: String, background: Color, tint: Color, iconDp: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(40.dp).clip(CircleShape).background(background).clickable(onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(painterResource(iconRes), contentDescription = description, tint = tint, modifier = Modifier.size(iconDp.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun CoachConversationPreview() {
    VoraCoachTheme {
        CoachConversationScreen(CoachConversationUiState(), {}, {}, {}, {}, {}, {})
    }
}
