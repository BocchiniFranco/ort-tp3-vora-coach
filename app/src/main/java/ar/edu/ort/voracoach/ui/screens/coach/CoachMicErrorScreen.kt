package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary

/**
 * 3.6 CoachMicErrorScreen (Demostración) — réplica de "Voice / 06 — Error".
 *
 * Aparece cuando en Listening se toca el micrófono sin haber "hablado". Salidas (NAV02):
 *  - "Allow Access" y "Try again" → 3.2 Listening. Es una simulación: la app NO pide el
 *    permiso RECORD_AUDIO ni lo declara en el manifest (NAV05).
 *  - "Not Now" o "<" → vuelve al asistente · "Type instead" → 3.5 Conversation.
 */
@Composable
fun CoachMicErrorScreen(
    onBackClick: () -> Unit,
    onAllowAccessClick: () -> Unit,
    onNotNowClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
    ) {
        VoiceHeader(title = "System Alert", onBackClick = onBackClick, dark = false)
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            VoraOrbGraphic(state = OrbState.ERROR, size = 120.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Sorry, I didn't catch that", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = VoraTextPrimary, textAlign = TextAlign.Center)
                Text(
                    "Try speaking again or type your request instead. Make sure your microphone is enabled.",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = VoraTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
            // Tarjeta "Microphone access needed".
            val shape = RoundedCornerShape(20.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(VoraSurface)
                    .border(1.dp, VoraBorder, shape)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFFCDEDE)), contentAlignment = Alignment.Center) {
                        Icon(painterResource(R.drawable.ic_mic), contentDescription = null, tint = VoraStatusError, modifier = Modifier.size(18.dp))
                    }
                    Text("Microphone access needed", style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp), color = VoraTextPrimary)
                }
                Text(
                    "VORA uses your microphone to listen to your voice commands and provide real-time hands-free coaching.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp),
                    color = VoraTextSecondary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Allow Access",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(VoraPillShape)
                            .background(VoraGreen)
                            .clickable(onClick = onAllowAccessClick)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                    Text(
                        "Not Now",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = VoraTextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(VoraPillShape)
                            .border(1.dp, VoraBorder, VoraPillShape)
                            .clickable(onClick = onNotNowClick)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 64.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Try again",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                color = VoraGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(VoraPillShape)
                    .background(VoraMint)
                    .clickable(onClick = onTryAgainClick)
                    .padding(horizontal = 32.dp, vertical = 14.dp)
            )
            Text(
                "Type instead",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline),
                color = VoraTextSecondary,
                modifier = Modifier.clickable(onClick = onTypeInsteadClick).padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun CoachMicErrorPreview() {
    VoraCoachTheme {
        CoachMicErrorScreen({}, {}, {}, {}, {})
    }
}
