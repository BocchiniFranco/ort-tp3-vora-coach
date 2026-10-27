package ar.edu.ort.voracoach.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import ar.edu.ort.voracoach.model.NutritionDemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.viewmodel.AnalysisUiState
import kotlinx.coroutines.delay

/**
 * 5.4 Voice Food Logging (Figma "Nutrition / 04 — Voice Food Logging"). Es una simulación:
 * no pide permiso de micrófono y la transcripción aparece sola, letra por letra.
 */
@Composable
fun VoiceFoodLogScreen(
    onCloseClick: () -> Unit,
    onDoneSpeakingClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transcript = "\"${NutritionDemoData.VOICE_TRANSCRIPT}\""
    // rememberSaveable: al rotar no vuelve a empezar la transcripción
    var visibleChars by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (visibleChars < transcript.length) {
            delay(TYPING_DELAY_MS)
            visibleChars++
        }
    }

    DarkAnalysisLayout(modifier) {
        DarkHeader(onCloseClick = onCloseClick) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(VoraMint))
            Spacer(Modifier.width(8.dp))
            Text("Voice Logging", style = MaterialTheme.typography.titleSmall, color = Color.White)
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            VoraOrbGraphic(size = 160.dp, state = OrbState.LISTENING)
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Listening...",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = VoraMint
                )
                Text(
                    text = "\"Keep talking. I'm listening.\"",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
        val shape = RoundedCornerShape(20.dp)
        Text(
            text = transcript.take(visibleChars),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium, lineHeight = 22.sp),
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(shape)
                .background(Color.White.copy(alpha = 0.05f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), shape)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Done Speaking",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = VoraGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(VoraPillShape)
                    .background(VoraMint)
                    .clickable(onClick = onDoneSpeakingClick)
                    .padding(vertical = 14.dp)
            )
            UnderlinedAction(text = "Type instead", alpha = 0.8f, onClick = onTypeInsteadClick)
        }
    }
}

/**
 * 5.5 Food Analysis (Figma "Nutrition / 05 — Food Analysis"): el análisis es real contra
 * el servidor y los pasos se marcan a medida que avanza.
 */
@Composable
fun FoodAnalysisScreen(
    uiState: AnalysisUiState,
    onCloseClick: () -> Unit,
    onCancelClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DarkAnalysisLayout(modifier) {
        DarkHeader(onCloseClick = onCloseClick) {
            Text(
                text = "VORA AI",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            VoraOrbGraphic(
                size = 140.dp,
                state = if (uiState is AnalysisUiState.Error) OrbState.ERROR else OrbState.PROCESSING
            )
            Text(
                text = if (uiState is AnalysisUiState.Error) "Analysis failed" else "Analyzing Meal...",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("CAPTURED INPUT", style = MaterialTheme.typography.labelMedium, color = VoraMint)
                Text(
                    text = "\"${NutritionDemoData.CAPTURED_INPUT}\"",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = Color.White
                )
            }
            if (uiState is AnalysisUiState.Error) {
                Text(uiState.message, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                VoraLimeButton(text = "Try again", onClick = onRetryClick, modifier = Modifier.fillMaxWidth())
            } else {
                val completed = when (uiState) {
                    is AnalysisUiState.Running -> uiState.completedSteps
                    is AnalysisUiState.Ready -> ANALYSIS_STEPS.size
                    else -> 0
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ANALYSIS_STEPS.forEachIndexed { index, label ->
                        AnalysisStepRow(
                            label = label,
                            state = when {
                                index < completed -> StepState.DONE
                                index == completed -> StepState.CURRENT
                                else -> StepState.PENDING
                            }
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            UnderlinedAction(text = "Cancel analysis", alpha = 0.6f, onClick = onCancelClick)
        }
    }
}

private val ANALYSIS_STEPS = listOf("Identifying foods...", "Estimating portions...", "Calculating nutrition...")

private enum class StepState { DONE, CURRENT, PENDING }

@Composable
private fun AnalysisStepRow(label: String, state: StepState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    when (state) {
                        StepState.DONE -> VoraMint
                        StepState.CURRENT -> VoraLime
                        StepState.PENDING -> Color.White.copy(alpha = 0.1f)
                    }
                )
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (state == StepState.CURRENT) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = if (state == StepState.PENDING) Color.White.copy(alpha = 0.4f) else Color.White
        )
    }
}

/** Fondo verde de 5.4 y 5.5, con scroll para pantallas bajas. */
@Composable
private fun DarkAnalysisLayout(modifier: Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraGreen)
            .verticalScroll(rememberScrollState())
    ) { content() }
}

@Composable
private fun DarkHeader(onCloseClick: () -> Unit, title: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) { title() }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
                .clickable(onClickLabel = "Close", onClick = onCloseClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(R.drawable.ic_close), contentDescription = "Close", tint = Color.White)
        }
    }
}

@Composable
private fun UnderlinedAction(text: String, alpha: Float, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
        ),
        color = Color.White.copy(alpha = alpha),
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onClick).padding(4.dp)
    )
}

private const val TYPING_DELAY_MS = 35L

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun VoiceFoodLogScreenPreview() {
    VoraCoachTheme { VoiceFoodLogScreen(onCloseClick = {}, onDoneSpeakingClick = {}, onTypeInsteadClick = {}) }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun FoodAnalysisScreenPreview() {
    VoraCoachTheme {
        FoodAnalysisScreen(
            uiState = AnalysisUiState.Running(completedSteps = 1),
            onCloseClick = {},
            onCancelClick = {},
            onRetryClick = {}
        )
    }
}
