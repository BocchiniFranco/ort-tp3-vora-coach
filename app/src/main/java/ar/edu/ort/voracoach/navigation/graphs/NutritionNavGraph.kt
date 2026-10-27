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
import ar.edu.ort.voracoach.model.MealDates
import ar.edu.ort.voracoach.navigation.Screen
import ar.edu.ort.voracoach.navigation.navigateReplacing
import ar.edu.ort.voracoach.navigation.navigateToBottomDestination
import ar.edu.ort.voracoach.ui.components.VoraBottomDestination
import ar.edu.ort.voracoach.ui.screens.nutrition.AddFoodScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.DailyMealsScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.FoodAnalysisScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.MealDetailScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.MealFormActions
import ar.edu.ort.voracoach.ui.screens.nutrition.MealReviewScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.NutritionDashboardScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.PhotoAnalysisScreen
import ar.edu.ort.voracoach.ui.screens.nutrition.VoiceFoodLogScreen
import ar.edu.ort.voracoach.viewmodel.AnalysisUiState
import ar.edu.ort.voracoach.viewmodel.MealAnalysisViewModel
import ar.edu.ort.voracoach.viewmodel.MealDetailUiState
import ar.edu.ort.voracoach.viewmodel.NutritionViewModel

/** FLUJO 5 - NUTRICIÓN (Integrante 3). */
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
    composable(Screen.VoiceFoodLog.route) {
        VoiceFoodLogScreen(
            onCloseClick = { navController.popBackStack() },
            onDoneSpeakingClick = { navController.navigate(Screen.FoodAnalysis.route) },
            onTypeInsteadClick = { navController.backToAddFood(current = Screen.VoiceFoodLog) }
        )
    }

    composable(Screen.FoodAnalysis.route) {
        val viewModel: MealAnalysisViewModel = hiltViewModel()
        val uiState by viewModel.analysisState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) { viewModel.startVoiceAnalysis() }
        // 5.5 es un estado de paso: al terminar se reemplaza por 5.7 para que Atrás no vuelva acá
        LaunchedEffect(uiState) {
            val ready = uiState as? AnalysisUiState.Ready ?: return@LaunchedEffect
            viewModel.sendToReview(ready.analysis)
            viewModel.onReviewOpened()
            navController.navigateReplacing(Screen.MealReviewConfirm.route, Screen.FoodAnalysis)
        }

        FoodAnalysisScreen(
            uiState = uiState,
            onCloseClick = {
                viewModel.cancelAnalysis()
                navController.popBackStack()
            },
            onCancelClick = {
                viewModel.cancelAnalysis()
                navController.popBackStack()
            },
            onRetryClick = { viewModel.retryAnalysis(voice = true) }
        )
    }

    composable(Screen.PhotoAnalysis.route) {
        val viewModel: MealAnalysisViewModel = hiltViewModel()
        val uiState by viewModel.analysisState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) { viewModel.startPhotoAnalysis() }

        PhotoAnalysisScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onReviewClick = { analysis ->
                viewModel.sendToReview(analysis)
                navController.navigate(Screen.MealReviewConfirm.route)
            },
            onRetakeClick = { viewModel.startPhotoAnalysis(force = true) },
            onRetryClick = { viewModel.retryAnalysis(voice = false) }
        )
    }

    composable(Screen.MealReviewConfirm.route) {
        val viewModel: MealAnalysisViewModel = hiltViewModel()
        val uiState by viewModel.reviewState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.isSaved) {
            if (uiState.isSaved) {
                viewModel.onSaveHandled()
                navController.backToNutritionDashboard()
            }
        }

        MealReviewScreen(
            uiState = uiState,
            onBackClick = { navController.popBackStack() },
            onIncreaseGrams = viewModel::increaseGrams,
            onDecreaseGrams = viewModel::decreaseGrams,
            onRemoveItem = viewModel::removeItem,
            onAddIngredientClick = {
                navController.navigate(
                    Screen.AddFood.createRoute(date = MealDates.today(), mealType = uiState.mealType.name)
                )
            },
            onConfirmClick = viewModel::saveMeal,
            onSaveWithoutLoggingClick = {
                viewModel.discardReview()
                navController.backToAddFood(current = Screen.MealReviewConfirm)
            }
        )
    }
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

/**
 * Vuelve a 5.3 si está en la pila. Si no (se llegó por "Voice" del dashboard), la abre en
 * lugar de la pantalla actual.
 */
private fun NavHostController.backToAddFood(current: Screen) {
    if (!popBackStack(Screen.AddFood.route, inclusive = false)) {
        navigateReplacing(Screen.AddFood.createRoute(), current)
    }
}

/** Después de guardar, muestra el dashboard de Nutrición con la comida nueva. */
private fun NavHostController.backToNutritionDashboard() {
    if (!popBackStack(Screen.NutritionDashboard.route, inclusive = false)) {
        navigateToBottomDestination(VoraBottomDestination.NUTRITION)
    }
}
