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
import ar.edu.ort.voracoach.ui.screens.nutrition.DailyMealsScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.MealDetailScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.MealFormActions
import ar.edu.ort.voracoach.ui.screens.nutrition.NutritionDashboardScreen
import ar.edu.ort.voracoach.viewmodel.MealDetailUiState
import ar.edu.ort.voracoach.viewmodel.NutritionViewModel

/** FLUJO 5 - NUTRICIÓN (Integrante 3). Las que siguen con placeholderDestination están pendientes. */
fun NavGraphBuilder.nutritionGraph(navController: NavHostController) {
    composable(Screen.NutritionDashboard.route) {
        val viewModel: NutritionViewModel = hiltViewModel()
        val uiState by viewModel.dayState.collectAsStateWithLifecycle()

        NutritionDashboardScreen(
            uiState = uiState,
            onDaySelected = viewModel::selectDay,
            onTipClick = { navController.navigate(Screen.CoachConversation.route) },
            onViewHistoryClick = { navController.navigate(Screen.DailyMeals.createRoute(uiState.date)) },
            onMealGroupClick = { group ->
                navController.navigate(Screen.MealDetail.createRoute(group.meals.first().id))
            },
            onAddMealClick = { type ->
                navController.navigate(Screen.AddFood.createRoute(date = uiState.date, mealType = type.name))
            },
            onVoiceClick = { navController.navigate(Screen.VoiceFoodLog.route) },
            onPhotoClick = { navController.navigate(Screen.PhotoAnalysis.route) },
            onSearchClick = { navController.navigate(Screen.AddFood.createRoute(date = uiState.date)) }
        )
    }
    // 5.2: {date} obligatorio con formato ISO yyyy-MM-dd.
    composable(
        route = Screen.DailyMeals.route,
        arguments = listOf(navArgument(Screen.ARG_DATE) { type = NavType.StringType })
    ) {
        val viewModel: NutritionViewModel = hiltViewModel()
        val uiState by viewModel.dayState.collectAsStateWithLifecycle()

        DailyMealsScreen(
            uiState = uiState,
            onMealGroupClick = { group ->
                navController.navigate(Screen.MealDetail.createRoute(group.meals.first().id))
            },
            onAddMealClick = { type ->
                navController.navigate(Screen.AddFood.createRoute(date = uiState.date, mealType = type.name))
            }
        )
    }
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
    composable(
        route = Screen.MealDetail.route,
        arguments = listOf(navArgument(Screen.ARG_MEAL_ID) { type = NavType.LongType })
    ) {
        val viewModel: NutritionViewModel = hiltViewModel()
        val uiState by viewModel.mealDetailState.collectAsStateWithLifecycle()
        val mealPendingDelete by viewModel.mealPendingDelete.collectAsStateWithLifecycle()
        val message by viewModel.message.collectAsStateWithLifecycle()

        // Si la comida no existe o se eliminaron todos sus ítems, no queda nada que mostrar
        LaunchedEffect(uiState) {
            if (uiState is MealDetailUiState.NotFound) navController.popBackStack()
        }

        MealDetailScreen(
            uiState = uiState,
            mealPendingDelete = mealPendingDelete,
            message = message,
            onBackClick = { navController.popBackStack() },
            onRemoveClick = viewModel::requestDelete,
            onConfirmDelete = viewModel::confirmDelete,
            onDismissDelete = viewModel::dismissDelete,
            onEditMealClick = {
                (uiState as? MealDetailUiState.Content)?.let { content ->
                    navController.navigate(
                        Screen.AddFood.createRoute(date = content.date, mealType = content.group.type.name)
                    )
                }
            },
            onDuplicateMealClick = viewModel::duplicateMeal,
            onMessageShown = viewModel::onMessageShown
        )
    }
}
