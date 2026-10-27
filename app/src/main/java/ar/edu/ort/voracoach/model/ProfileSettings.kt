package ar.edu.ort.voracoach.model

/** Datos y preferencias del perfil de demostración. Los valores iniciales son los del Figma. */
data class ProfileSettings(
    // 7.2 Edit profile
    val firstName: String = DemoData.alexMorgan.firstName,
    val lastName: String = "Morgan",
    val dateOfBirth: String = "14 March 1995",
    val height: String = "178 cm",
    val weight: String = "76 kg",
    val gender: String = "Prefer not to say",
    // 7.3 Your goals
    val primaryGoals: Set<String> = setOf("Build strength", "Improve fitness"),
    val trainingDays: String = "4 days",
    val workoutPreferences: Set<String> = setOf("Strength"),
    // 7.4 Nutrition preferences
    val dietaryPreference: String = "Everything",
    val foodsEnjoyed: Set<String> = setOf("Chicken", "Eggs", "Fish", "Rice", "Pasta", "Greek yoghurt", "Vegetables", "Fruit"),
    val foodsToAvoid: List<String> = listOf("Peanuts", "Shellfish", "Mushrooms"),
    val nutritionPriorities: Set<String> = setOf("Hit protein targets", "Eat more consistently"),
    // 7.5 VORA Coach
    val coachingStyle: String = "Balanced",
    val speakResponsesAloud: Boolean = true,
    val autoPlayCoaching: Boolean = true,
    val allowInterruptions: Boolean = true,
    val responseStyle: String = "Balanced",
    val voiceFeedback: Boolean = true,
    val hapticFeedback: Boolean = true
) {
    val fullName: String get() = "$firstName $lastName".trim()
}
