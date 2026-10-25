package ar.edu.ort.voracoach.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.R
import ar.edu.ort.voracoach.model.Food
import ar.edu.ort.voracoach.model.MealDates
import ar.edu.ort.voracoach.model.MealType
import ar.edu.ort.voracoach.ui.components.VoraChip
import ar.edu.ort.voracoach.ui.components.VoraEmptyView
import ar.edu.ort.voracoach.ui.components.VoraErrorView
import ar.edu.ort.voracoach.ui.components.VoraLoadingView
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraScreenPadding
import ar.edu.ort.voracoach.ui.components.VoraTextField
import ar.edu.ort.voracoach.ui.theme.VoraBackground
import ar.edu.ort.voracoach.ui.theme.VoraBorder
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraStatusError
import ar.edu.ort.voracoach.ui.theme.VoraSurface
import ar.edu.ort.voracoach.ui.theme.VoraTextPrimary
import ar.edu.ort.voracoach.ui.theme.VoraTextSecondary
import ar.edu.ort.voracoach.viewmodel.FoodSearchState
import ar.edu.ort.voracoach.viewmodel.MealFormState
import java.util.Locale

// Las dos hojas no tienen frame en el Figma: siguen los campos, chips y botones del sistema de diseño.

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ManualEntrySheet(
    form: MealFormState,
    actions: MealFormActions,
    onDismiss: () -> Unit
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = VoraBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(start = VoraScreenPadding, end = VoraScreenPadding, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SheetTitle(title = "Log Manually", subtitle = "Enter the values for the whole portion.")

            VoraTextField(
                value = form.name,
                onValueChange = actions.onNameChange,
                label = "Food name",
                placeholder = "e.g. Grilled chicken breast",
                errorText = form.nameError
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FieldLabel("Meal")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MealType.entries.forEach { type ->
                        VoraChip(
                            text = type.label,
                            selected = type == form.mealType,
                            onClick = { actions.onMealTypeChange(type) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FieldLabel("Date")
                DateField(date = form.date, onClick = { showDatePicker = true })
            }

            VoraTextField(
                value = form.calories,
                onValueChange = actions.onCaloriesChange,
                label = "Calories (kcal)",
                placeholder = "0",
                errorText = form.caloriesError,
                keyboardType = KeyboardType.Decimal
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VoraTextField(
                    value = form.protein,
                    onValueChange = actions.onProteinChange,
                    label = "Protein (g)",
                    placeholder = "0",
                    errorText = form.proteinError,
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )
                VoraTextField(
                    value = form.carbs,
                    onValueChange = actions.onCarbsChange,
                    label = "Carbs (g)",
                    placeholder = "0",
                    errorText = form.carbsError,
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )
                VoraTextField(
                    value = form.fat,
                    onValueChange = actions.onFatChange,
                    label = "Fat (g)",
                    placeholder = "0",
                    errorText = form.fatError,
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )
            }

            if (form.saveError != null) {
                Text(form.saveError, style = MaterialTheme.typography.bodySmall, color = VoraStatusError)
            }

            VoraPrimaryButton(
                text = if (form.isSaving) "Saving..." else "Add to ${form.mealType.label}",
                onClick = actions.onSave,
                enabled = !form.isSaving
            )
        }
    }

    if (showDatePicker) {
        MealDatePickerDialog(
            date = form.date,
            onDateSelected = { millis ->
                actions.onDateSelected(millis)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchSheet(
    search: FoodSearchState,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onFoodSelected: (Food) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = VoraBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(start = VoraScreenPadding, end = VoraScreenPadding, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SheetTitle(title = "Search Database", subtitle = "Nutrition values are per 100 g.")
            VoraTextField(
                value = search.query,
                onValueChange = onQueryChange,
                label = "Food",
                placeholder = "e.g. oats, chicken, banana",
                leadingIcon = ImageVector.vectorResource(R.drawable.ic_search)
            )
            when {
                search.isLoading -> VoraLoadingView(message = "Searching foods...")
                search.error != null -> VoraErrorView(
                    title = "Search unavailable",
                    message = search.error,
                    onRetry = onRetry
                )
                search.hasSearched && search.results.isEmpty() -> VoraEmptyView(
                    title = "No foods found",
                    message = "Try another name, or log the food manually."
                )
                else -> LazyColumn(
                    modifier = Modifier.heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(search.results, key = { it.id }) { food ->
                        FoodResultRow(food = food, onClick = { onFoodSelected(food) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = VoraTextPrimary
        )
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = VoraTextSecondary)
    }
}

/** Misma etiqueta que usa VoraTextField, para que los chips y la fecha queden alineados. */
@Composable
private fun FieldLabel(text: String) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelMedium, color = VoraTextSecondary)
}

@Composable
private fun DateField(date: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(VoraSurface)
            .border(1.dp, VoraBorder, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = MealDates.label(date),
            style = MaterialTheme.typography.bodyLarge,
            color = VoraTextPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = "Choose date",
            tint = VoraGreen,
            modifier = Modifier.size(20.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MealDatePickerDialog(
    date: String,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = MealDates.toUtcMillis(date))
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { state.selectedDateMillis?.let(onDateSelected) ?: onDismiss() }
            ) { Text("OK", color = VoraGreen) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = VoraTextSecondary) }
        }
    ) {
        DatePicker(state = state)
    }
}

@Composable
private fun FoodResultRow(food: Food, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
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
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = VoraTextPrimary
            )
            Text(
                text = "P ${food.per100g.proteinG.grams()} · C ${food.per100g.carbsG.grams()} · F ${food.per100g.fatG.grams()}",
                style = MaterialTheme.typography.bodySmall,
                color = VoraTextSecondary
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = "${food.per100g.kcal.toInt()} kcal",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = VoraTextPrimary
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_small_right),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun Double.grams(): String =
    if (this % 1.0 == 0.0) "${toInt()}g" else String.format(Locale.US, "%.1fg", this)
