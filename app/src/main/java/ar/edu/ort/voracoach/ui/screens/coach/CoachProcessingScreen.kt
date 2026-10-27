package ar.edu.ort.voracoach.ui.screens.coach

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
// rotate: gira el ícono de carga en cada frame de la animación.
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import kotlinx.coroutines.delay

/**
 * 3.3 CoachProcessingScreen (Demostración) — réplica de "Voice / 03 — Processing".
 *
 * Muestra lo que se "escuchó" y un indicador "Checking your nutrition target..." mientras
 * el anillo del orbe gira. A los pocos segundos avanza sola a 3.4 Speaking mediante
 * [onProcessingFinished]. "Cancel" o "<" vuelven atrás.
 */
@Composable
fun CoachProcessingScreen(
    question: String,
    statusMessage: String,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onProcessingFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnFinished by rememberUpdatedState(onProcessingFinished)
    // Tiempo simulado de "pensamiento" de la IA.
    LaunchedEffect(Unit) {
        delay(2_800)
        currentOnFinished()
    }
    val transition = rememberInfiniteTransition(label = "loader")
    val loaderRotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "loaderRotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraGreen)
            .verticalScroll(rememberScrollState())
    ) {
        VoiceHeader(title = "Processing", onBackClick = onBackClick, dark = true)
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            VoraOrbGraphic(state = OrbState.PROCESSING, size = 150.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("THINKING...", style = MaterialTheme.typography.titleSmall, color = VoraMint)
                // Burbuja con lo capturado.
                val shape = RoundedCornerShape(16.dp)
                Text(
                    text = "\"$question\"",
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.5.sp),
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), shape)
                        .padding(18.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    painterResource(R.drawable.ic_loader),
                    contentDescription = null,
                    tint = VoraMint,
                    modifier = Modifier.size(16.dp).rotate(loaderRotation)
                )
                Text(statusMessage, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f))
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 120.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Cancel",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.clickable(onClick = onCancelClick).padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
private fun CoachProcessingPreview() {
    VoraCoachTheme {
        CoachProcessingScreen(DemoData.COACH_DEMO_QUESTION, DemoData.COACH_PROCESSING_MESSAGE, {}, {}, {})
    }
}
