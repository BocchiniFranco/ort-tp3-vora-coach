// Subpaquete del Flujo 2 (Inicio): 2.1 Home Dashboard y 2.2 Notifications.
package ar.edu.ort.voracoach.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
// FlowRow: acomoda los chips en varias líneas si no entran en una (UI03).
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraCircularProgress
import ar.edu.ort.voracoach.ui.components.VoraHeroCard
import ar.edu.ort.voracoach.ui.components.VoraHeroPill
import ar.edu.ort.voracoach.ui.components.VoraMacroProgressBar
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.HomeUiState
// Locale.US: separador de miles con coma ("1,420"), como en el diseño en inglés.
import java.util.Locale

/**
 * 2.1 HomeDashboardScreen (Demostración, destino "Home" de la BottomBar) —
 * réplica del frame "Home / 01 — Dashboard" del Figma.
 *
 * Cada bloque tocable dispara un evento distinto para cumplir NAV01 (nada "muerto"):
 *  - Avatar → 7.1 Profile · Campana → 2.2 Notifications
 *  - "Start voice session" → 3.1 Voice Idle · chips → Workout / 5.3 Add Food (breakfast)
 *  - Banner de la IA → 3.5 Conversation · Anillo "72% Daily Goal" → 6.1 Daily Summary
 *  - Tarjeta de nutrición → pestaña Nutrition · Pasos / distancia / kcal → 6.5 Health Trends
 *  - Entrenamiento recomendado → pestaña Workout · "Start workout" → 4.4 Workout Prep
 */
@Composable
fun HomeDashboardScreen(
    uiState: HomeUiState,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onStartVoiceSessionClick: () -> Unit,
    onStartMyWorkoutClick: () -> Unit,
    onLogBreakfastClick: () -> Unit,
    onTipClick: () -> Unit,
    onDailyGoalClick: () -> Unit,
    onNutritionClick: () -> Unit,
    onActivityClick: () -> Unit,
    onRecommendedWorkoutClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = uiState.today
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HomeHeader(uiState, onProfileClick, onNotificationsClick)
        CoachHeroPanel(onStartVoiceSessionClick, onStartMyWorkoutClick, onLogBreakfastClick)
        VoraTipBanner(message = uiState.tip, onClick = onTipClick)

        // ---------------- Today's Overview ----------------
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Today's Overview", style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp), color = VoraTextPrimary)
            // IntrinsicSize.Min: ambas tarjetas toman la altura de la más alta.
            Row(modifier = Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VoraCard(
                    modifier = Modifier.width(110.dp).fillMaxHeight(),
                    onClick = onDailyGoalClick,
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        VoraCircularProgress(
                            progress = today.dailyGoalPercent / 100f,
                            centerText = "${today.dailyGoalPercent}%",
                            size = 64.dp,
                            strokeWidth = 4.8.dp,
                            trackColor = VoraBackground
                        )
                        Text("Daily Goal", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraTextSecondary)
                    }
                }
                VoraCard(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = onNutritionClick,
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Nutrition", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraTextSecondary)
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = VoraTextPrimary)) {
                                        append("${today.caloriesConsumed.withThousands()} ")
                                    }
                                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, color = VoraTextTertiary)) {
                                        append("/ ${today.caloriesGoal.withThousands()} kcal")
                                    }
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            VoraMacroProgressBar(label = "Protein", current = today.proteinGrams, goal = today.proteinGoal, barColor = VoraGreen)
                            VoraMacroProgressBar(label = "Carbs", current = today.carbsGrams, goal = today.carbsGoal, barColor = VoraMint)
                        }
                    }
                }
            }
            // Pasos / distancia / kcal activas, separados por bordes verticales.
            VoraCard(onClick = onActivityClick, contentPadding = PaddingValues(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ActivityStat("STEPS", today.steps.withThousands(), Modifier.weight(1f), showDivider = false)
                    ActivityStat("DISTANCE", "${today.distanceKm} km", Modifier.weight(1f), showDivider = true)
                    ActivityStat("ACTIVE KCAL", "${today.activeKcal} kcal", Modifier.weight(1f), showDivider = true)
                }
            }
            RecommendedWorkoutCard(uiState, onRecommendedWorkoutClick, onStartWorkoutClick)
        }
    }
}

/** Encabezado: avatar de 40 dp, "Good morning" / "Alex Morgan" y botón de campana. */
@Composable
private fun HomeHeader(uiState: HomeUiState, onProfileClick: () -> Unit, onNotificationsClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).clickable(onClick = onProfileClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.img_avatar_alex),
                contentDescription = "Profile of ${uiState.user.fullName}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(uiState.greeting, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary)
                Text(uiState.user.fullName, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary)
            }
        }
        // Botón de campana del Figma (40 dp); el punto menta indica notificaciones sin leer.
        Image(
            painter = painterResource(if (uiState.unreadNotifications > 0) R.drawable.ic_bell_button_unread else R.drawable.ic_bell_button),
            contentDescription = "Notifications (${uiState.unreadNotifications} unread)",
            modifier = Modifier.size(40.dp).clip(CircleShape).clickable(onClick = onNotificationsClick)
        )
    }
}

/** Panel verde del coach: orbe, "AI ONLINE", pregunta, CTA lima y chips de acceso rápido. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoachHeroPanel(onStartVoiceSessionClick: () -> Unit, onStartMyWorkoutClick: () -> Unit, onLogBreakfastClick: () -> Unit) {
    VoraHeroCard(
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = VoraGreen.copy(alpha = 0.03f), spotColor = VoraGreen.copy(alpha = 0.05f)),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VoraOrbGraphic(state = OrbState.IDLE, size = 36.dp)
                Text("VORA Coach", style = MaterialTheme.typography.titleSmall, color = VoraMint)
            }
            VoraHeroPill(text = "AI ONLINE")
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("\"How can I support your fitness targets today?\"", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text("Tap the mic to talk naturally. I'll plan or log for you.", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f))
        }
        // CTA lima de ancho completo: "Start voice session".
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(VoraPillShape)
                .background(VoraLime)
                .clickable(onClick = onStartVoiceSessionClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painterResource(R.drawable.ic_mic_small), contentDescription = null, tint = VoraGreen, modifier = Modifier.size(16.dp))
            Text("Start voice session", style = MaterialTheme.typography.titleSmall, color = VoraGreen)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HeroChip("Start my workout", onStartMyWorkoutClick)
            HeroChip("Log breakfast", onLogBreakfastClick)
        }
    }
}

/** Chip translúcido sobre el panel verde (blanco al 12 %, texto 12 Medium blanco). */
@Composable
private fun HeroChip(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
        color = Color.White,
        modifier = Modifier
            .clip(VoraPillShape)
            .background(Color.White.copy(alpha = 0.12f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

/** Dato de actividad: etiqueta 11 tenue y valor 16 Bold, con borde izquierdo opcional. */
@Composable
private fun ActivityStat(label: String, value: String, modifier: Modifier, showDivider: Boolean) {
    Row(modifier = modifier.fillMaxHeight()) {
        if (showDivider) {
            Box(Modifier.width(1.dp).fillMaxHeight().background(VoraBorder))
            Spacer(Modifier.width(12.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal), color = VoraTextTertiary, maxLines = 1)
            Text(value, style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold), color = VoraTextPrimary, maxLines = 1)
        }
    }
}

/** Tarjeta "RECOMMENDED WORKOUT" con etiqueta TODAY, volumen estimado y botón "Start workout". */
@Composable
private fun RecommendedWorkoutCard(uiState: HomeUiState, onCardClick: () -> Unit, onStartWorkoutClick: () -> Unit) {
    val workout = uiState.todayWorkout
    VoraCard(onClick = onCardClick) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(painterResource(R.drawable.ic_calendar), contentDescription = null, tint = VoraGreen, modifier = Modifier.size(16.dp))
                Text("RECOMMENDED WORKOUT", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = VoraTextSecondary)
            }
            Text("TODAY", style = MaterialTheme.typography.labelSmall, color = VoraGreen, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(workout.title, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary)
            Text(
                "${workout.durationMinutes} min • ${workout.exercises} exercises • ${workout.level}",
                style = MaterialTheme.typography.bodyMedium,
                color = VoraTextSecondary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Estimated Vol: ${workout.estimatedVolumeKg.withThousands()} kg", style = MaterialTheme.typography.bodySmall, color = VoraTextTertiary)
            Text(
                "Start workout",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                color = Color.White,
                modifier = Modifier
                    .clip(VoraPillShape)
                    .background(VoraGreen)
                    .clickable(onClick = onStartWorkoutClick)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

/** Formatea enteros con separador de miles: 7842 -> "7,842". */
private fun Int.withThousands(): String = String.format(Locale.US, "%,d", this)

@Preview(showBackground = true, widthDp = 390, heightDp = 1000)
@Composable
private fun HomeDashboardPreview() {
    VoraCoachTheme {
        HomeDashboardScreen(
            uiState = HomeUiState(DemoData.alexMorgan, DemoData.today, DemoData.todayWorkout, DemoData.TIP_ON_TRACK, "Good morning", 2),
            onProfileClick = {}, onNotificationsClick = {}, onStartVoiceSessionClick = {}, onStartMyWorkoutClick = {},
            onLogBreakfastClick = {}, onTipClick = {}, onDailyGoalClick = {}, onNutritionClick = {},
            onActivityClick = {}, onRecommendedWorkoutClick = {}, onStartWorkoutClick = {}
        )
    }
}
