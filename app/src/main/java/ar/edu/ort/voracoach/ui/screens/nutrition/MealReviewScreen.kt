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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.model.AnalyzedFood
import ar.edu.ort.voracoach.model.Macros
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.ui.components.VoraEmptyView
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.viewmodel.MealReviewUiState
import ar.edu.ort.voracoach.viewmodel.ReviewItem
import kotlin.math.roundToInt

/**
 * 5.7 Food Confirmation (Figma "Nutrition / 07 — Food Confirmation"): el usuario ajusta la
 * estimación antes de guardarla. Recién al confirmar se escribe en Room.
 */
@Composable
fun MealReviewScreen(
    uiState: MealReviewUiState,
    onBackClick: () -> Unit,
    onIncreaseGrams: (Int) -> Unit,
    onDecreaseGrams: (Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onAddIngredientClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onSaveWithoutLoggingClick: () -> Unit,
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
        NutritionBackHeader(title = "Review Meal", onBackClick = onBackClick)
        if (!uiState.hasAnalysis) {
            VoraEmptyView(
                title = "Nothing to review",
                message = "Log a meal by voice or photo and VORA will estimate it for you.",
                actionText = "Go back",
                onAction = onBackClick
            )
            return@Column
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            uiState.items.forEachIndexed { index, item ->
                ReviewItemCard(
                    item = item,
                    onIncrease = { onIncreaseGrams(index) },
                    onDecrease = { onDecreaseGrams(index) },
                    onRemove = { onRemoveItem(index) }
                )
            }
        }
        Text(
            text = "+ Add Ingredient",
            style = MaterialTheme.typography.titleSmall,
            color = VoraGreen,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onAddIngredientClick)
        )
        MealTotalsCard(uiState)
        if (uiState.saveError != null) {
            Text(uiState.saveError, style = MaterialTheme.typography.bodySmall, color = VoraStatusError)
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            VoraPrimaryButton(
                text = if (uiState.isSaving) "Saving..." else "Add to ${uiState.mealType.label}",
                onClick = onConfirmClick,
                enabled = uiState.items.isNotEmpty() && !uiState.isSaving
            )
            NutritionSecondaryButton(text = "Save without logging", onClick = onSaveWithoutLoggingClick)
        }
    }
}

@Composable
private fun ReviewItemCard(item: ReviewItem, onIncrease: () -> Unit, onDecrease: () -> Unit, onRemove: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = item.food.name,
                style = MaterialTheme.typography.titleSmall,
                color = VoraTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "${item.calories.thousands()} kcal",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = VoraTextPrimary
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            StepperButton(text = "-", contentDescription = "Less", onClick = onDecrease)
            Text(
                text = "${item.grams}g",
                style = MaterialTheme.typography.titleSmall,
                color = VoraTextPrimary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            StepperButton(text = "+", contentDescription = "More", onClick = onIncrease)
            Spacer(Modifier.weight(1f))
            Text(
                text = "Remove",
                style = MaterialTheme.typography.bodySmall,
                color = VoraStatusError,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onRemove).padding(4.dp)
            )
        }
    }
}

@Composable
private fun StepperButton(text: String, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(VoraBackground)
            .clickable(onClickLabel = contentDescription, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = VoraTextPrimary)
    }
}

@Composable
private fun MealTotalsCard(uiState: MealReviewUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoraGreen)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "MEAL TOTALS",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = VoraMint,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${uiState.calories.thousands()} kcal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.12f)))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TotalValue("PROTEIN", uiState.protein)
            TotalValue("CARBS", uiState.carbs)
            TotalValue("FAT", uiState.fat)
        }
    }
}

@Composable
private fun TotalValue(label: String, grams: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = VoraMint)
        Text(
            text = "${grams.roundToInt()}g",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MealReviewScreenPreview() {
    VoraCoachTheme {
        MealReviewScreen(
            uiState = MealReviewUiState(
                items = listOf(
                    ReviewItem(AnalyzedFood("f05", "Grilled Chicken Breast", 180.0, Macros(155.0, 31.0, 0.0, 3.6)), 180),
                    ReviewItem(AnalyzedFood("f04", "Basmati Rice", 150.0, Macros(127.0, 2.7, 28.2, 0.3)), 150)
                ),
                mealType = MealType.LUNCH,
                hasAnalysis = true
            ),
            onBackClick = {},
            onIncreaseGrams = {},
            onDecreaseGrams = {},
            onRemoveItem = {},
            onAddIngredientClick = {},
            onConfirmClick = {},
            onSaveWithoutLoggingClick = {}
        )
    }
}
