package ar.edu.ort.voracoach.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ar.edu.ort.voracoach.model.ProfileSettings
import ar.edu.ort.voracoach.ui.components.VoraPrimaryButton

private val primaryGoals = listOf(
    "Build strength" to "Focus on weight training & mechanical load",
    "Improve fitness" to "Condition cardiovascular & energy systems",
    "Lose body fat" to null,
    "Build healthy habits" to null
)
private val trainingDayOptions = listOf("3 days", "4 days", "5 days", "6+ days")
private val workoutOptions = listOf("Strength", "Cardio", "HIIT", "Mobility", "Mixed")

/** 7.3 Goals & Preferences (Figma "Profile / 03 — Goals & Preferences", título "Your goals"). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsPreferencesScreen(
    draft: ProfileSettings,
    onDraftChange: ((ProfileSettings) -> ProfileSettings) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ProfileSubScreen(title = "Your goals", onBackClick = onBackClick) {
        ProfileDescription("Tell VORA what you're working towards. Your coaching adapts around this.")

        ProfileSectionLabel("Primary goal — what's your main goal?")
        primaryGoals.forEach { (goal, description) ->
            SelectableOptionCard(
                title = goal,
                subtitle = description,
                selected = goal in draft.primaryGoals,
                onClick = { onDraftChange { it.copy(primaryGoals = it.primaryGoals.toggle(goal)) } }
            )
        }

        ProfileSectionLabel("Training frequency")
        SegmentedOptions(
            options = trainingDayOptions,
            selected = draft.trainingDays,
            onSelect = { days -> onDraftChange { it.copy(trainingDays = days) } }
        )

        ProfileSectionLabel("Workout preference")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            workoutOptions.forEach { option ->
                PreferenceChip(
                    text = option,
                    selected = option in draft.workoutPreferences,
                    onClick = { onDraftChange { it.copy(workoutPreferences = it.workoutPreferences.toggle(option)) } }
                )
            }
        }

        ProfileSectionLabel("Coaching style — how should VORA coach you?")
        SelectableOptionCard(
            title = draft.coachingStyle,
            subtitle = "Practical guidance with gentle motivation",
            selected = true,
            onClick = {}
        )

        VoraPrimaryButton(text = "Save preferences", onClick = onSaveClick, modifier = Modifier.padding(top = 8.dp))
    }
}

/** Agrega el elemento si no está y lo quita si ya estaba. */
internal fun <T> Set<T>.toggle(item: T): Set<T> = if (item in this) this - item else this + item
