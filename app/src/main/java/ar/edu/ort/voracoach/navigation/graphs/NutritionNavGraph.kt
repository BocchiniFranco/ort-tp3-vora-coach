package ar.edu.ort.voracoach.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination

/**
 * FLUJO 5 - NUTRICIÓN (Integrante 3).
 *
 * PENDIENTE (Integrante 3): reemplazar cada placeholderDestination(...) por la pantalla
 * real, conectando NutritionViewModel (Room). Los argumentos ya están definidos y tipados.
 */
fun NavGraphBuilder.nutritionGraph(navController: NavHostController) {
    placeholderDestination(Screen.NutritionDashboard, navController)
    // 5.2: {date} obligatorio con formato ISO yyyy-MM-dd.
    placeholderDestination(
        Screen.DailyMeals, navController,
        arguments = listOf(navArgument(Screen.ARG_DATE) { type = NavType.StringType })
    )
    // 5.3: date y mealType OPCIONALES (nullable + defaultValue null).
    placeholderDestination(
        Screen.AddFood, navController,
        arguments = listOf(
            navArgument(Screen.ARG_DATE) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(Screen.ARG_MEAL_TYPE) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    )
    placeholderDestination(Screen.VoiceFoodLog, navController)
    placeholderDestination(Screen.FoodAnalysis, navController)
    placeholderDestination(Screen.PhotoAnalysis, navController)
    placeholderDestination(Screen.MealReviewConfirm, navController)
    // 5.8: {mealId} obligatorio (Long = clave primaria de Room).
    placeholderDestination(
        Screen.MealDetail, navController,
        arguments = listOf(navArgument(Screen.ARG_MEAL_ID) { type = NavType.LongType })
    )
}
