// Subpaquete del Flujo 2 (Inicio): 2.1 Home Dashboard y 2.2 Notifications.
package ar.edu.ort.voracoach.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.DemoData
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraCard
import ar.edu.ort.voracoach.ui.components.VoraCircularProgress
import ar.edu.ort.voracoach.ui.components.VoraHeroCard
import ar.edu.ort.voracoach.ui.components.VoraHeroLabel
import ar.edu.ort.voracoach.ui.components.VoraHeroSecondaryButton
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraListRow
import ar.edu.ort.voracoach.ui.components.VoraMacroProgressBar
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraSectionHeader
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraAccentLime
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraDarkGreen
import ar.edu.ort.voracoach.ui.theme.VoraLightMint
import ar.edu.ort.voracoach.ui.theme.VoraSecondaryGreen
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.viewmodel.HomeUiState
// Locale.US: separador de miles con coma ("1,480"), como en el diseño en inglés.
import java.util.Locale

/**
 * 2.1 HomeDashboardScreen (Demostración, destino "Home" de la BottomBar).
 *
 * Cada bloque clickeable dispara un evento distinto para cumplir NAV01 (nada "muerto"):
 *  - Campana -> 2.2 Notifications · Avatar -> 7.1 Profile
 *  - Hero "VORA Coach / AI ONLINE" -> 3.1 Coach Ready (voz) o 3.5 Conversation (chat)
 *  - Anillo "72% Daily Goal" -> 6.1 Daily Summary
 *  - Entrenamiento de hoy -> pestaña Workout · Nutrición -> pestaña Nutrition / 5.3 Add Food
 *  - Banner de IA -> 3.5 Conversation · Weekly Review -> 6.6
 */
@Composable
fun HomeDashboardScreen(
    uiState: HomeUiState,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTalkToVoraClick: () -> Unit,
    onChatWithVoraClick: () -> Unit,
    onDailyGoalClick: () -> Unit,
    onWorkoutClick: () -> Unit,
    onWeeklyPlanClick: () -> Unit,
    onNutritionClick: () -> Unit,
    onLogMealClick: () -> Unit,
    onWeeklyReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = uiState.today
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = VoraScreenPadding, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ---------------- Encabezado: saludo + campana + avatar ----------------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${uiState.greeting},", style = MaterialTheme.typography.bodyLarge, color = VoraTextSecondary)
                Text(uiState.user.firstName, style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
            }
            NotificationBell(unread = uiState.unreadNotifications, onClick = onNotificationsClick)
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(VoraDarkGreen)
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center
            ) {
                Text(uiState.user.initials, color = VoraAccentLime, style = MaterialTheme.typography.titleSmall)
            }
        }

        // ---------------- Hero card del asistente ----------------
        VoraHeroCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    VoraHeroLabel(text = "AI Online", showDot = true)
                    Text("VORA Coach", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text(
                        "Hi ${uiState.user.firstName}! You're ${today.dailyGoalPercent}% to today's goal. Want a quick plan for dinner?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                VoraOrbGraphic(state = OrbState.IDLE, size = 84.dp, haloColor = VoraSecondaryGreen)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VoraLimeButton(text = "Talk to Vora", icon = Icons.Rounded.Mic, onClick = onTalkToVoraClick, modifier = Modifier.weight(1f))
                VoraHeroSecondaryButton(text = "Chat", icon = Icons.AutoMirrored.Rounded.Chat, onClick = onChatWithVoraClick)
            }
        }

        // ---------------- Progreso del día ----------------
        VoraCard(onClick = onDailyGoalClick) {
            Text("Today's Progress", style = MaterialTheme.typography.titleLarge, color = VoraTextPrimary)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                VoraCircularProgress(
                    progress = today.dailyGoalPercent / 100f,
                    centerText = "${today.dailyGoalPercent}%",
                    label = "Daily Goal",
                    size = 124.dp
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatLine(Icons.Rounded.LocalFireDepartment, "${today.caloriesConsumed.withThousands()} / ${today.caloriesGoal.withThousands()} kcal", "Calories")
                    StatLine(Icons.Rounded.Timer, "${today.workoutMinutes} min", "Workout")
                    StatLine(Icons.AutoMirrored.Rounded.DirectionsRun, today.steps.withThousands(), "Steps")
                }
            }
        }

        // ---------------- Entrenamiento de hoy ----------------
        VoraSectionHeader(title = "Today's Workout", actionText = "Weekly plan", onAction = onWeeklyPlanClick)
        VoraCard(onClick = onWorkoutClick) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(VoraLightMint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.FitnessCenter, contentDescription = null, tint = VoraDarkGreen)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(uiState.todayWorkout.title, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary)
                    Text(
                        "${uiState.todayWorkout.durationMinutes} min · ${uiState.todayWorkout.level} · ${uiState.todayWorkout.exercises} exercises",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoraTextSecondary
                    )
                }
            }
            VoraPrimaryButton(text = "Start Workout", onClick = onWorkoutClick)
        }

        // ---------------- Nutrición ----------------
        VoraSectionHeader(title = "Nutrition", actionText = "Log meal", onAction = onLogMealClick)
        VoraCard(onClick = onNutritionClick) {
            VoraMacroProgressBar(label = "Protein", current = today.proteinGrams, goal = today.proteinGoal)
            VoraMacroProgressBar(label = "Carbs", current = today.carbsGrams, goal = today.carbsGoal)
            VoraMacroProgressBar(label = "Fat", current = today.fatGrams, goal = today.fatGoal)
        }

        // ---------------- Recomendación de la IA ----------------
        VoraTipBanner(title = "Vora's tip", message = uiState.tip, onClick = onChatWithVoraClick)

        // ---------------- Acceso a la revisión semanal ----------------
        VoraCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
            VoraListRow(
                title = "Weekly Review",
                subtitle = "See how this week compares with the last one",
                icon = Icons.Rounded.CalendarMonth,
                onClick = onWeeklyReviewClick
            )
        }
        Spacer(Modifier.size(8.dp))
    }
}

/** Campana con badge rojo que muestra la cantidad de notificaciones sin leer. */
@Composable
private fun NotificationBell(unread: Int, onClick: () -> Unit) {
    Box {
        VoraIconCircleButton(icon = Icons.Rounded.Notifications, contentDescription = "Notifications", onClick = onClick)
        if (unread > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(VoraStatusError),
                contentAlignment = Alignment.Center
            ) {
                Text("$unread", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/** Línea de estadística: ícono + valor + etiqueta. */
@Composable
private fun StatLine(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, contentDescription = null, tint = VoraDarkGreen, modifier = Modifier.size(20.dp))
        Column {
            Text(value, style = MaterialTheme.typography.titleSmall, color = VoraTextPrimary)
            Text(label, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
        }
    }
}

/** Formatea enteros con separador de miles: 8432 -> "8,432". */
private fun Int.withThousands(): String = String.format(Locale.US, "%,d", this)

@Preview(showBackground = true, backgroundColor = 0xFFF5F8F7, heightDp = 1400)
@Composable
private fun HomeDashboardPreview() {
    VoraCoachTheme {
        HomeDashboardScreen(
            uiState = HomeUiState(DemoData.alexMorgan, DemoData.today, DemoData.todayWorkout, DemoData.TIP_ON_TRACK, "Good morning", 2),
            onNotificationsClick = {}, onProfileClick = {}, onTalkToVoraClick = {}, onChatWithVoraClick = {},
            onDailyGoalClick = {}, onWorkoutClick = {}, onWeeklyPlanClick = {}, onNutritionClick = {},
            onLogMealClick = {}, onWeeklyReviewClick = {}
        )
    }
}
