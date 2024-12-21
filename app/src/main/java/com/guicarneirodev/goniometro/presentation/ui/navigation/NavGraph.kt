package com.guicarneirodev.goniometro.presentation.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.guicarneirodev.goniometro.domain.repository.FirebaseRepository
import com.guicarneirodev.goniometro.presentation.ui.screens.home.HomeScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.login.LoginScreen
import com.guicarneirodev.goniometro.presentation.viewmodel.PatientsScreenViewModel
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.PatientsScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.register.RegisterScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.results.ResultsScreen
import com.guicarneirodev.goniometro.domain.repository.FirestorePatientRepository
import com.guicarneirodev.goniometro.data.service.RetrofitPdfService
import com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.GoniometroScreen
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.SelectionScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("selection") { SelectionScreen(navController) }
        composable("main") { GoniometroScreen(navController) }
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