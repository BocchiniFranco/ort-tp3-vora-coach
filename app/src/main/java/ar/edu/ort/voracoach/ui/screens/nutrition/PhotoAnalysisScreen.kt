package ar.edu.ort.voracoach.ui.screens.nutrition

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.AnalysisPreset
import ar.edu.ort.voracoach.model.AnalyzedFood
import ar.edu.ort.voracoach.model.Macros
import ar.edu.ort.voracoach.model.NutritionAnalysis
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraLime
import ar.edu.ort.voracoach.ui.theme.VoraMint
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraTextTertiary
import ar.edu.ort.voracoach.viewmodel.AnalysisUiState
import kotlin.math.roundToInt

/**
 * 5.6 Photo Analysis (Figma "Nutrition / 06 — Photo Analysis"). No usa la cámara: la foto es
 * la del Figma y el servidor devuelve la estimación de una foto de ejemplo.
 */
@Composable
fun PhotoAnalysisScreen(
    uiState: AnalysisUiState,
    onBackClick: () -> Unit,
    onReviewClick: (NutritionAnalysis) -> Unit,
    onRetakeClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val analysis = (uiState as? AnalysisUiState.Ready)?.analysis
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        NutritionBackHeader(title = "Analyse Meal", onBackClick = onBackClick)
        MealPhoto(items = analysis?.items.orEmpty())
        EstimateCard(uiState = uiState, onRetryClick = onRetryClick)
        Text(
            text = "\"Portions and quantities can be refined in the next step before saving.\"",
            style = MaterialTheme.typography.bodySmall,
            color = VoraTextSecondary
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            VoraPrimaryButton(
                text = "Review & Add",
                onClick = { analysis?.let(onReviewClick) },
                enabled = analysis != null
            )
            NutritionSecondaryButton(text = "Retake Photo", onClick = onRetakeClick)
        }
    }
}

@Composable
private fun MealPhoto(items: List<AnalyzedFood>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.photo_meal_analysis),
            contentDescription = "Photo of the meal",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Etiquetas de los dos primeros alimentos, en las posiciones del Figma
        items.getOrNull(0)?.let { PhotoTag(it, x = 40.dp, y = 60.dp) }
        items.getOrNull(1)?.let { PhotoTag(it, x = 180.dp, y = 110.dp) }
    }
}

@Composable
private fun PhotoTag(food: AnalyzedFood, x: Dp, y: Dp) {
    Text(
        text = "${food.name} (~${food.grams.roundToInt()}g)",
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = Modifier
            .offset(x = x, y = y)
            .clip(RoundedCornerShape(6.dp))
            .background(VoraGreen)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun EstimateCard(uiState: AnalysisUiState, onRetryClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "VORA ESTIMATE",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextSecondary,
                modifier = Modifier.weight(1f)
            )
            Text("Portions are estimated", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = VoraTextTertiary)
        }
        when (uiState) {
            is AnalysisUiState.Ready -> Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${uiState.analysis.totals.kcal.thousands()} kcal",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                    color = VoraTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroChip("P: ${uiState.analysis.totals.proteinG.roundToInt()}g", VoraGreen.copy(alpha = 0.08f))
                    MacroChip("C: ${uiState.analysis.totals.carbsG.roundToInt()}g", VoraMint.copy(alpha = 0.12f))
                    MacroChip("F: ${uiState.analysis.totals.fatG.roundToInt()}g", VoraLime.copy(alpha = 0.12f))
                }
            }
            is AnalysisUiState.Error -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(uiState.message, style = MaterialTheme.typography.bodyMedium, color = VoraStatusError)
                Text(
                    text = "Try again",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = VoraGreen,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onRetryClick)
                )
            }
            AnalysisUiState.Idle, is AnalysisUiState.Running -> Text(
                text = "Estimating portions...",
                style = MaterialTheme.typography.titleMedium,
                color = VoraTextTertiary
            )
        }
    }
}

@Composable
private fun MacroChip(text: String, background: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = VoraGreen,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PhotoAnalysisScreenPreview() {
    val analysis = NutritionAnalysis(
        items = listOf(
            AnalyzedFood("f05", "Grilled Chicken", 180.0, Macros(165.0, 31.0, 0.0, 3.6)),
            AnalyzedFood("f04", "Basmati Rice", 150.0, Macros(130.0, 2.7, 28.2, 0.3))
        ),
        totals = Macros(520.0, 38.0, 54.0, 14.0),
        isSimulated = true,
        requiresConfirmation = true,
        preset = AnalysisPreset.LUNCH
    )
    VoraCoachTheme {
        PhotoAnalysisScreen(
            uiState = AnalysisUiState.Ready(analysis),
            onBackClick = {},
            onReviewClick = {},
            onRetakeClick = {},
            onRetryClick = {}
        )
    }
}
