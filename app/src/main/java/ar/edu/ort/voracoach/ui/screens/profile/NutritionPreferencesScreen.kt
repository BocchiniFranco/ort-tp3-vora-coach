package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ProfileSettings
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton
import ar.edu.ort.voracoach.ui.components.VoraTextField
import ar.edu.ort.voracoach.ui.theme.VoraGreen
import ar.edu.ort.voracoach.ui.theme.VoraStatusError

private val dietaryOptions = listOf("Everything", "Vegetarian", "Vegan", "Pescatarian", "Other")
private val enjoyOptions = listOf("Chicken", "Eggs", "Fish", "Rice", "Pasta", "Greek yoghurt", "Vegetables", "Fruit")
private val priorityOptions = listOf("Hit protein targets", "Eat more consistently")

/** 7.4 Nutrition Preferences (Figma "Profile / 04 — Nutrition Preferences"). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionPreferencesScreen(
    draft: ProfileSettings,
    onDraftChange: ((ProfileSettings) -> ProfileSettings) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var newIngredient by rememberSaveable { mutableStateOf("") }

    ProfileSubScreen(title = "Nutrition preferences", onBackClick = onBackClick) {
        ProfileDescription("Help VORA make food recommendations that actually fit your lifestyle.")

        ProfileSectionLabel("Dietary preference")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            dietaryOptions.forEach { option ->
                PreferenceChip(
                    text = option,
                    selected = option == draft.dietaryPreference,
                    onClick = { onDraftChange { it.copy(dietaryPreference = option) } }
                )
            }
        }

        ProfileSectionLabel("Foods you enjoy")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            enjoyOptions.forEach { food ->
                val enjoyed = food in draft.foodsEnjoyed
                PreferenceChip(
                    text = food,
                    selected = false,
                    onClick = { onDraftChange { it.copy(foodsEnjoyed = it.foodsEnjoyed.toggle(food)) } },
                    trailing = if (enjoyed) {
                        { Icon(Icons.Rounded.Check, contentDescription = "Enjoyed", tint = VoraGreen, modifier = Modifier.size(14.dp)) }
                    } else null
                )
            }
        }

        ProfileSectionLabel("Foods to avoid")
        VoraTextField(
            value = newIngredient,
            onValueChange = { newIngredient = it },
            label = "Add an ingredient",
            placeholder = "Add an ingredient...",
            leadingIcon = Icons.Rounded.Add
        )
        if (newIngredient.isNotBlank()) {
            PreferenceChip(
                text = "Add \"${newIngredient.trim()}\"",
                selected = true,
                onClick = {
                    val ingredient = newIngredient.trim()
                    onDraftChange { it.copy(foodsToAvoid = (it.foodsToAvoid + ingredient).distinct()) }
                    newIngredient = ""
                }
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            draft.foodsToAvoid.forEach { food ->
                PreferenceChip(
                    text = food,
                    selected = false,
                    onClick = { onDraftChange { it.copy(foodsToAvoid = it.foodsToAvoid - food) } },
                    trailing = {
                        Icon(Icons.Outlined.Cancel, contentDescription = "Remove", tint = VoraStatusError, modifier = Modifier.size(14.dp))
                    }
                )
            }
        }

        ProfileSectionLabel("Meal preferences")
        ProfileCard {
            LabelValueRow("Meals per day", "3 meals + 1 snack")
            LabelValueRow("Preferred breakfast", "Quick & high protein")
            LabelValueRow("Preferred dinner", "Balanced")
        }

        ProfileSectionLabel("What matters most to you?")
        priorityOptions.forEach { priority ->
            SelectableOptionCard(
                title = priority,
                selected = priority in draft.nutritionPriorities,
                onClick = { onDraftChange { it.copy(nutritionPriorities = it.nutritionPriorities.toggle(priority)) } }
            )
        }

        VoraPrimaryButton(text = "Save preferences", onClick = onSaveClick, modifier = Modifier.padding(top = 8.dp))
    }
}
