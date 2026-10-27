package ar.edu.ort.voracoach.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.model.DailyTotals
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.MealDates
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.model.NutritionGoals
import ar.edu.ort.voracoach.ui.components.VoraDividerLine
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.MealGroup
import ar.edu.ort.voracoach.viewmodel.NutritionDayUiState

/** 5.2 Today's Meals (Figma "Nutrition / 02 — Today's Meals"): comidas de una fecha desde Room. */
@Composable
fun DailyMealsScreen(
    uiState: NutritionDayUiState,
    onMealGroupClick: (MealGroup) -> Unit,
    onAddMealClick: (MealType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("TIMELINE", style = MaterialTheme.typography.labelMedium, color = VoraTextSecondary)
                Text(
                    text = if (uiState.isToday) "Today's Meals" else MealDates.label(uiState.date),
                    style = MaterialTheme.typography.headlineMedium,
                    color = VoraTextPrimary
                )
            }
            Text(
                text = "${uiState.totals.calories.thousands()} kcal",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = VoraGreen,
                modifier = Modifier
                    .clip(VoraPillShape)
                    .background(VoraGreen.copy(alpha = 0.08f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            uiState.groups.forEach { group ->
                if (group.meals.isEmpty()) {
                    EmptyMealCard(type = group.type, onAddClick = { onAddMealClick(group.type) })
                } else {
                    MealGroupCard(group = group, onClick = { onMealGroupClick(group) })
                }
            }
        }
    }
}

@Composable
private fun MealGroupCard(group: MealGroup, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = group.type.label,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = VoraTextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${group.calories.thousands()} kcal",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = VoraGreen
            )
        }
        VoraDividerLine()
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            group.meals.forEach { meal -> MealItemRow(meal) }
        }
    }
}

@Composable
private fun MealItemRow(meal: Meal) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = meal.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = VoraTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = meal.macrosLabel(),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = VoraTextTertiary
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = "${meal.calories.thousands()} kcal",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = VoraTextSecondary
        )
    }
}

@Composable
private fun EmptyMealCard(type: MealType, onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dashedBorder(VoraBorder, cornerRadius = 16.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "${type.label} is not logged yet",
            style = MaterialTheme.typography.titleSmall,
            color = VoraTextSecondary
        )
        Text(
            text = "+ Add ${type.label}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
            modifier = Modifier
                .clip(VoraPillShape)
                .background(VoraGreen)
                .clickable(onClick = onAddClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

/** Borde punteado de 1 dp: Compose no trae uno, se dibuja con un PathEffect. */
private fun Modifier.dashedBorder(color: Color, cornerRadius: androidx.compose.ui.unit.Dp) = drawBehind {
    val stroke = 1.dp.toPx()
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx())))
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 818)
@Composable
private fun DailyMealsScreenPreview() {
    fun meal(name: String, type: MealType, kcal: Double) =
        Meal(name = name, date = "2026-10-25", mealType = type, calories = kcal, protein = 20.0, carbs = 30.0, fat = 8.0)
    VoraCoachTheme {
        DailyMealsScreen(
            uiState = NutritionDayUiState(
                date = "2026-10-25",
                isToday = true,
                totals = DailyTotals(calories = 1180.0, mealCount = 4),
                groups = listOf(
                    MealGroup(MealType.BREAKFAST, listOf(meal("Greek Yoghurt", MealType.BREAKFAST, 150.0), meal("Granola", MealType.BREAKFAST, 270.0))),
                    MealGroup(MealType.LUNCH, listOf(meal("Grilled Chicken Breast", MealType.LUNCH, 520.0))),
                    MealGroup(MealType.SNACK, listOf(meal("Whey Protein Shake", MealType.SNACK, 240.0))),
                    MealGroup(MealType.DINNER, emptyList())
                ),
                goals = NutritionGoals(2100.0, 140.0, 220.0, 65.0),
                tip = ""
            ),
            onMealGroupClick = {},
            onAddMealClick = {}
        )
    }
}
