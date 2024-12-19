package com.guicarneirodev.goniometro.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.guicarneirodev.goniometro.data.repository.FirebaseRepository
import com.guicarneirodev.goniometro.presentation.ui.screens.home.HomeScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.login.LoginScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.main.MainScreen
import com.guicarneirodev.goniometro.presentation.viewmodel.PatientsScreenViewModel
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.PatientsScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.register.RegisterScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.results.ResultsScreen
import com.guicarneirodev.goniometro.data.repository.FirestorePatientRepository
import com.guicarneirodev.goniometro.data.service.RetrofitPdfService

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
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
            ResultsScreen(navController = navController, userId, patientId)
        }
    }
}

fun getPatientViewModel(userId: String): PatientsScreenViewModel {
    val patientRepository = FirestorePatientRepository(userId)
    val loginRepository = FirebaseRepository(
        FirebaseAuth.getInstance(),
        FirebaseFunctions.getInstance()
    )
    val pdfService = RetrofitPdfService(loginRepository)
    return PatientsScreenViewModel(patientRepository, pdfService)
}