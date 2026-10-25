package ar.edu.ort.voracoach.ui.screens.nutrition

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DailyTotals
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.MealDates
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.model.NutritionGoals
import ar.edu.ort.voracoach.ui.components.VoraCircularProgress
import ar.edu.ort.voracoach.ui.components.VoraDividerLine
import ar.edu.ort.voracoach.ui.components.VoraIconCircleButton
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraSectionHeader
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.MealGroup
import ar.edu.ort.voracoach.viewmodel.NutritionDayUiState
import java.util.Locale
import kotlin.math.roundToInt

/** 5.1 Nutrition Dashboard (Figma "Nutrition / 01 — Dashboard"): totales y comidas del día desde Room. */
@Composable
fun NutritionDashboardScreen(
    uiState: NutritionDayUiState,
    onDaySelected: (Long) -> Unit,
    onTipClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onMealGroupClick: (MealType) -> Unit,
    onAddMealClick: (MealType) -> Unit,
    onVoiceClick: () -> Unit,
    onPhotoClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DashboardHeader(date = uiState.date, onCalendarClick = { showDatePicker = true })
        CalorieProgressCard(totals = uiState.totals, goals = uiState.goals)
        VoraTipBanner(message = uiState.tip, onClick = onTipClick)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            VoraSectionHeader(
                title = if (uiState.isToday) "Today's Meals" else "Meals · ${MealDates.label(uiState.date)}",
                actionText = "View History",
                onAction = onViewHistoryClick
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.groups.forEach { group ->
                    MealGroupRow(
                        group = group,
                        onClick = { onMealGroupClick(group.type) },
                        onAddClick = { onAddMealClick(group.type) }
                    )
                }
            }
        }
        TellVoraCard(onVoiceClick = onVoiceClick, onPhotoClick = onPhotoClick, onSearchClick = onSearchClick)
    }

    if (showDatePicker) {
        MealDatePickerDialog(
            date = uiState.date,
            onDateSelected = { millis ->
                onDaySelected(millis)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun DashboardHeader(date: String, onCalendarClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = MealDates.headerLabel(date),
                style = MaterialTheme.typography.labelMedium,
                color = VoraTextSecondary
            )
            Text(text = "Nutrition", style = MaterialTheme.typography.headlineMedium, color = VoraTextPrimary)
        }
        VoraIconCircleButton(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = "Choose day",
            onClick = onCalendarClick,
            iconTint = Color.Unspecified
        )
    }
}

@Composable
private fun CalorieProgressCard(totals: DailyTotals, goals: NutritionGoals) {
    val shape = RoundedCornerShape(20.dp)
    val progress = if (goals.calories > 0) (totals.calories / goals.calories).toFloat() else 0f
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "CALORIES CONSUMED",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = VoraTextSecondary
                )
                Text(
                    text = totals.calories.thousands(),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                    color = VoraTextPrimary
                )
                Text(
                    text = "Goal: ${goals.calories.thousands()} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoraTextTertiary
                )
            }
            VoraCircularProgress(
                progress = progress,
                centerText = "${(progress * 100).roundToInt()}%",
                size = 72.dp
            )
        }
        VoraDividerLine()
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MacroColumn("Protein", totals.protein, goals.protein, VoraGreen)
            MacroColumn("Carbs", totals.carbs, goals.carbs, VoraMint)
            MacroColumn("Fat", totals.fat, goals.fat, VoraLime)
        }
    }
}

@Composable
private fun RowScope.MacroColumn(label: String, current: Double, goal: Double, barColor: Color) {
    val fraction = if (goal > 0) (current / goal).toFloat().coerceIn(0f, 1f) else 0f
    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextSecondary
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 12.sp, color = VoraTextPrimary)) { append("${current.roundToInt()}g") }
                    withStyle(SpanStyle(fontSize = 10.sp, color = VoraTextTertiary)) { append("/${goal.roundToInt()}g") }
                },
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(VoraBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
private fun MealGroupRow(group: MealGroup, onClick: () -> Unit, onAddClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    val isEmpty = group.meals.isEmpty()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .clickable(onClick = if (isEmpty) onAddClick else onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = group.type.label,
                style = MaterialTheme.typography.titleSmall,
                color = VoraTextPrimary
            )
            Text(
                text = if (isEmpty) "Not logged yet" else group.summary,
                style = MaterialTheme.typography.bodySmall,
                color = if (isEmpty) VoraTextTertiary else VoraTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(12.dp))
        if (isEmpty) {
            Text(
                text = "+ Add",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier
                    .clip(VoraPillShape)
                    .background(VoraGreen)
                    .clickable(onClick = onAddClick)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        } else {
            Text(
                text = "${group.calories.thousands()} kcal",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = VoraTextPrimary
            )
        }
    }
}

@Composable
private fun TellVoraCard(onVoiceClick: () -> Unit, onPhotoClick: () -> Unit, onSearchClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraOrbGraphic(size = 28.dp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Tell Vora what you ate",
                style = MaterialTheme.typography.titleSmall,
                color = VoraMint
            )
        }
        Text(
            text = "Vora will automatically estimate the portions, ingredients, and log the nutrition details.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TellVoraButton("Voice", R.drawable.ic_mic_14, VoraLime, VoraGreen, Color.Unspecified, onVoiceClick)
            TellVoraButton("Photo", R.drawable.ic_camera, Color.White.copy(alpha = 0.12f), Color.White, Color.White, onPhotoClick)
            TellVoraButton("Search", R.drawable.ic_search, Color.White.copy(alpha = 0.12f), Color.White, Color.White, onSearchClick)
        }
    }
}

@Composable
private fun RowScope.TellVoraButton(
    text: String,
    @DrawableRes iconRes: Int,
    containerColor: Color,
    contentColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .weight(1f)
            .clip(VoraPillShape)
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(iconRes), contentDescription = null, tint = iconTint, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = contentColor
        )
    }
}

/** 1420.0 -> "1,420", como en el Figma. */
private fun Double.thousands(): String = String.format(Locale.US, "%,d", roundToInt())

@Preview(showBackground = true, widthDp = 390, heightDp = 1013)
@Composable
private fun NutritionDashboardScreenPreview() {
    fun meal(name: String, type: MealType, kcal: Double) =
        Meal(name = name, date = "2026-10-25", mealType = type, calories = kcal, protein = 30.0, carbs = 40.0, fat = 12.0)
    VoraCoachTheme {
        NutritionDashboardScreen(
            uiState = NutritionDayUiState(
                date = "2026-10-25",
                isToday = true,
                totals = DailyTotals(calories = 1420.0, protein = 104.0, carbs = 152.0, fat = 48.0, mealCount = 3),
                groups = listOf(
                    MealGroup(MealType.BREAKFAST, listOf(meal("Greek yoghurt", MealType.BREAKFAST, 420.0))),
                    MealGroup(MealType.LUNCH, listOf(meal("Chicken", MealType.LUNCH, 520.0))),
                    MealGroup(MealType.SNACK, listOf(meal("Protein shake", MealType.SNACK, 240.0))),
                    MealGroup(MealType.DINNER, emptyList())
                ),
                goals = NutritionGoals(2100.0, 140.0, 220.0, 65.0),
                tip = "You're on track today. A protein-rich dinner would help you reach your target."
            ),
            onDaySelected = {},
            onTipClick = {},
            onViewHistoryClick = {},
            onMealGroupClick = {},
            onAddMealClick = {},
            onVoiceClick = {},
            onPhotoClick = {},
            onSearchClick = {}
        )
    }
}
