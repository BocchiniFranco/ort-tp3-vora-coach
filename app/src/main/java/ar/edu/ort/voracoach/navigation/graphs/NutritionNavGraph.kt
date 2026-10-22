package ar.edu.ort.voracoach.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.placeholderDestination
import ar.edu.ort.voracoach.ui.screens.nutrition.AddFoodScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.MealFormActions
import ar.edu.ort.voracoach.viewmodel.NutritionViewModel

/** FLUJO 5 - NUTRICIÓN (Integrante 3). Las que siguen con placeholderDestination están pendientes. */
fun NavGraphBuilder.nutritionGraph(navController: NavHostController) {
    placeholderDestination(Screen.NutritionDashboard, navController)
    // 5.2: {date} obligatorio con formato ISO yyyy-MM-dd.
    placeholderDestination(
        Screen.DailyMeals, navController,
        arguments = listOf(navArgument(Screen.ARG_DATE) { type = NavType.StringType })
    )
    // 5.3: date y mealType OPCIONALES (nullable + defaultValue null).
    composable(
        route = Screen.AddFood.route,
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
    ) {
        val viewModel: NutritionViewModel = hiltViewModel()
        val uiState by viewModel.addFoodState.collectAsStateWithLifecycle()

        // Al guardar vuelve a la pantalla anterior, que ya muestra la comida nueva
        LaunchedEffect(uiState.isSaved) {
            if (uiState.isSaved) {
                viewModel.onSaveHandled()
                navController.popBackStack()
            }
        }

        AddFoodScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onVoiceLoggingClick = { navController.navigate(Screen.VoiceFoodLog.route) },
            onSnapPhotoClick = { navController.navigate(Screen.PhotoAnalysis.route) },
            onSearchDatabaseClick = viewModel::openSearch,
            onLogManuallyClick = viewModel::openManualEntry,
            onSheetDismiss = viewModel::closeSheet,
            formActions = MealFormActions(
                onNameChange = viewModel::onNameChange,
                onMealTypeChange = viewModel::onMealTypeChange,
                onDateSelected = viewModel::onDateSelected,
                onCaloriesChange = viewModel::onCaloriesChange,
                onProteinChange = viewModel::onProteinChange,
                onCarbsChange = viewModel::onCarbsChange,
                onFatChange = viewModel::onFatChange,
                onSave = viewModel::saveMeal
            ),
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onSearchRetry = viewModel::retrySearch,
            onFoodSelected = viewModel::onFoodSelected
        )
    }
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
