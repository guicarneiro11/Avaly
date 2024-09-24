package com.guicarneirodev.goniometro

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController, viewModel()) }
        composable("main") { MainScreen(navController) }
        composable(
            "patients/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
                ?: throw IllegalStateException("UserID não encontrado na backStackEntry.")
            val patientViewModel = getPatientViewModel(userId)
            PatientsScreen(viewModel = patientViewModel, navController = navController, userId = userId)
        }
        composable(
            "results/{userId}/{patientId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("patientId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
                ?: throw IllegalStateException("UserID não encontrado na backStackEntry.")
            val patientId = backStackEntry.arguments?.getString("patientId")
                ?: throw IllegalStateException("PatientID não encontrado na backStackEntry.")
            val resultsViewModel = getResultsViewModel(userId, patientId)
            ResultsScreen(navController = navController, viewModel = resultsViewModel)
        }
    }
}

fun getPatientViewModel(userId: String): PatientViewModel {
    val patientRepository = FirestorePatientRepository(userId)
    val pdfService = RetrofitPdfService()
    return PatientViewModel(patientRepository, pdfService)
}

fun getResultsViewModel(userId: String, patientId: String): ResultsViewModel {
    val resultsRepository = ResultsRepositoryImpl(userId, patientId)
    return ResultsViewModel(resultsRepository)
}