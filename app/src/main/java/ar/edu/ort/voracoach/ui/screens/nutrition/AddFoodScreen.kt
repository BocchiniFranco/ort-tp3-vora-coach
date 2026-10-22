package ar.edu.ort.voracoach.ui.screens.nutrition

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.Food
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.ui.components.OrbState
import ar.edu.ort.voracoach.ui.components.VoraLimeButton
import ar.edu.ort.voracoach.ui.components.VoraOrbGraphic
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraMintTranslucent
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
import ar.edu.ort.voracoach.viewmodel.AddFoodSheet
import ar.edu.ort.voracoach.viewmodel.AddFoodUiState
import ar.edu.ort.voracoach.viewmodel.MealFormState

/**
 * 5.3 Add Food (Figma "Nutrition / 03 — Add Food"): elige cómo registrar la comida.
 * El Figma no tiene pantalla para la carga manual ni para la búsqueda: se abren como
 * hojas sobre esta misma pantalla.
 */
@Composable
fun AddFoodScreen(
    uiState: AddFoodUiState,
    onBackClick: () -> Unit,
    onVoiceLoggingClick: () -> Unit,
    onSnapPhotoClick: () -> Unit,
    onSearchDatabaseClick: () -> Unit,
    onLogManuallyClick: () -> Unit,
    onSheetDismiss: () -> Unit,
    formActions: MealFormActions,
    onSearchQueryChange: (String) -> Unit,
    onSearchRetry: () -> Unit,
    onFoodSelected: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(start = VoraScreenPadding, end = VoraScreenPadding, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AddFoodHeader(onBackClick = onBackClick)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TalkToVoraCard(onVoiceLoggingClick = onVoiceLoggingClick)
            AddFoodOption(
                iconRes = R.drawable.ic_camera,
                iconBackground = VoraMintTranslucent,
                title = "Snap a Photo",
                subtitle = "Vora estimates sizes and nutrition info.",
                onClick = onSnapPhotoClick
            )
            AddFoodOption(
                iconRes = R.drawable.ic_search,
                iconBackground = VoraBackground,
                title = "Search Database",
                subtitle = "Search millions of local & global food items.",
                onClick = onSearchDatabaseClick
            )
            AddFoodOption(
                iconRes = R.drawable.ic_pen,
                iconBackground = VoraBackground,
                title = "Log Manually",
                subtitle = "Input weight, calories and macros yourself.",
                onClick = onLogManuallyClick
            )
        }
    }

    when (uiState.sheet) {
        AddFoodSheet.MANUAL -> ManualEntrySheet(
            form = uiState.form,
            actions = formActions,
            onDismiss = onSheetDismiss
        )
        AddFoodSheet.SEARCH -> FoodSearchSheet(
            search = uiState.search,
            onQueryChange = onSearchQueryChange,
            onRetry = onSearchRetry,
            onFoodSelected = onFoodSelected,
            onDismiss = onSheetDismiss
        )
        AddFoodSheet.NONE -> Unit
    }
}

@Composable
private fun AddFoodHeader(onBackClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.ic_back_arrow),
            contentDescription = "Back",
            tint = Color.Unspecified,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onBackClick)
                .size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Add Food",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = VoraTextPrimary
        )
    }
}

@Composable
private fun TalkToVoraCard(onVoiceLoggingClick: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape, ambientColor = VoraGreen.copy(alpha = 0.04f), spotColor = VoraGreen.copy(alpha = 0.07f))
            .clip(shape)
            .background(VoraGreen)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier.width(220.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Talk to VORA",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "\"I had grilled chicken breast with basmati rice.\"",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Spacer(Modifier.weight(1f))
            VoraOrbGraphic(size = 44.dp, state = OrbState.IDLE)
        }
        VoraLimeButton(
            text = "Start Voice Logging",
            onClick = onVoiceLoggingClick,
            icon = ImageVector.vectorResource(R.drawable.ic_mic_14),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddFoodOption(
    @DrawableRes iconRes: Int,
    iconBackground: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextPrimary
            )
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = VoraTextSecondary)
        }
        Spacer(Modifier.width(16.dp))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_small_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
    }
}

/** Acciones del formulario de carga manual, agrupadas para no repetir 9 lambdas por pantalla. */
data class MealFormActions(
    val onNameChange: (String) -> Unit,
    val onMealTypeChange: (MealType) -> Unit,
    val onDateSelected: (Long) -> Unit,
    val onCaloriesChange: (String) -> Unit,
    val onProteinChange: (String) -> Unit,
    val onCarbsChange: (String) -> Unit,
    val onFatChange: (String) -> Unit,
    val onSave: () -> Unit
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddFoodScreenPreview() {
    VoraCoachTheme {
        AddFoodScreen(
            uiState = AddFoodUiState(form = MealFormState(date = "2026-10-21", mealType = MealType.LUNCH)),
            onBackClick = {},
            onVoiceLoggingClick = {},
            onSnapPhotoClick = {},
            onSearchDatabaseClick = {},
            onLogManuallyClick = {},
            onSheetDismiss = {},
            formActions = MealFormActions({}, {}, {}, {}, {}, {}, {}, {}),
            onSearchQueryChange = {},
            onSearchRetry = {},
            onFoodSelected = {}
        )
    }
}
