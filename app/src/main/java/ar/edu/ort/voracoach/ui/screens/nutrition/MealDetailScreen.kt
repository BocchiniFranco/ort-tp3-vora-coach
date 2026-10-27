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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.DailyTotals
import ar.edu.ort.voracoach.model.Meal
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.model.NutritionGoals
import ar.edu.ort.voracoach.ui.components.VoraCircularProgress
import ar.edu.ort.voracoach.ui.components.VoraDividerLine
import ar.edu.ort.voracoach.ui.components.VoraLoadingView
import ar.edu.ort.voracoach.ui.components.VoraPillShape
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTipBanner
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.MealDetailUiState
import ar.edu.ort.voracoach.viewmodel.MealGroup
import kotlin.math.roundToInt

/**
 * 5.8 Meal Details (Figma "Nutrition / 08 — Meal Details"). El Figma no tiene cómo eliminar:
 * cada ingrediente suma un "Remove" como el de la revisión de comida (5.7), con confirmación.
 */
@Composable
fun MealDetailScreen(
    uiState: MealDetailUiState,
    mealPendingDelete: Meal?,
    message: String?,
    onBackClick: () -> Unit,
    onRemoveClick: (Meal) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onEditMealClick: () -> Unit,
    onDuplicateMealClick: () -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            onMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize().background(VoraBackground)) {
        when (uiState) {
            is MealDetailUiState.Content -> MealDetailContent(
                content = uiState,
                onBackClick = onBackClick,
                onRemoveClick = onRemoveClick,
                onEditMealClick = onEditMealClick,
                onDuplicateMealClick = onDuplicateMealClick
            )
            // NotFound: la navegación vuelve atrás apenas lo recibe
            MealDetailUiState.Loading, MealDetailUiState.NotFound -> VoraLoadingView()
        }
        SnackbarHost(snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    if (mealPendingDelete != null) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            title = { Text("Remove ${mealPendingDelete.name}?") },
            text = { Text("It will be deleted from your log and the daily totals will update.") },
            confirmButton = {
                TextButton(onClick = onConfirmDelete) { Text("Remove", color = VoraStatusError) }
            },
            dismissButton = {
                TextButton(onClick = onDismissDelete) { Text("Cancel", color = VoraTextSecondary) }
            },
            containerColor = VoraSurface
        )
    }
}

@Composable
private fun MealDetailContent(
    content: MealDetailUiState.Content,
    onBackClick: () -> Unit,
    onRemoveClick: (Meal) -> Unit,
    onEditMealClick: () -> Unit,
    onDuplicateMealClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = "Back",
                tint = Color.Unspecified,
                modifier = Modifier.clip(CircleShape).clickable(onClick = onBackClick).size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = content.group.type.label,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(text = content.time, style = MaterialTheme.typography.bodyMedium, color = VoraTextTertiary)
        }
        TotalCaloriesCard(totals = content.totals, goals = content.goals)
        VoraTipBanner(message = content.tip)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
                color = VoraTextPrimary
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                content.group.meals.forEach { meal ->
                    IngredientRow(meal = meal, onRemoveClick = { onRemoveClick(meal) })
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MealActionButton(text = "Edit Meal", onClick = onEditMealClick)
            MealActionButton(text = "Duplicate Meal", onClick = onDuplicateMealClick)
        }
    }
}

@Composable
private fun TotalCaloriesCard(totals: DailyTotals, goals: NutritionGoals) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "TOTAL CALORIES",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = VoraTextSecondary
                )
                Text(
                    text = "${totals.calories.thousands()} kcal",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                    color = VoraTextPrimary
                )
            }
            // Parte del objetivo diario de calorías que cubre esta comida
            VoraCircularProgress(
                progress = if (goals.calories > 0) (totals.calories / goals.calories).toFloat() else 0f,
                centerText = "",
                size = 64.dp
            )
        }
        VoraDividerLine()
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            MacroValue("PROTEIN", totals.protein)
            MacroValue("CARBS", totals.carbs)
            MacroValue("FAT", totals.fat)
        }
    }
}

@Composable
private fun MacroValue(label: String, grams: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = VoraTextSecondary)
        Text(
            text = "${grams.roundToInt()}g",
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            color = VoraTextPrimary
        )
    }
}

@Composable
private fun IngredientRow(meal: Meal, onRemoveClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = meal.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
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
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "${meal.calories.thousands()} kcal",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextSecondary
            )
            Text(
                text = "Remove",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = VoraStatusError,
                modifier = Modifier.clip(VoraPillShape).clickable(onClick = onRemoveClick)
            )
        }
    }
}

/** Botón blanco con borde del Figma ("Edit Meal", "Duplicate Meal"). */
@Composable
private fun MealActionButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = VoraTextPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clip(VoraPillShape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, VoraPillShape)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MealDetailScreenPreview() {
    fun meal(name: String, kcal: Double) =
        Meal(name = name, date = "2026-10-25", mealType = MealType.LUNCH, calories = kcal, protein = 13.0, carbs = 18.0, fat = 5.0)
    VoraCoachTheme {
        MealDetailScreen(
            uiState = MealDetailUiState.Content(
                group = MealGroup(MealType.LUNCH, listOf(meal("Grilled Chicken Breast", 280.0), meal("Basmati Rice", 190.0))),
                date = "2026-10-25",
                time = "13:12",
                totals = DailyTotals(calories = 470.0, protein = 26.0, carbs = 36.0, fat = 10.0, mealCount = 2),
                goals = NutritionGoals(2100.0, 140.0, 220.0, 65.0),
                tip = "Great protein choice. You're now 36g away from your daily protein target."
            ),
            mealPendingDelete = null,
            message = null,
            onBackClick = {},
            onRemoveClick = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onEditMealClick = {},
            onDuplicateMealClick = {},
            onMessageShown = {}
        )
    }
}
