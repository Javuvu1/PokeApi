package com.example.pokeapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.SignUpScreen
import com.example.pokeapi.ui.theme.ForgotPasswordScreen
import com.example.pokeapi.ui.theme.LoginScreen

@Composable
fun SetUpNavigation(navController: NavHostController, auth: AuthManager) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                auth = auth,
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                navigateToHome = { navController.navigate(Screen.Home.route) },
                navigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                auth = auth,
                navigateToHome = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                auth = auth,
                navigateToLogin = { navController.popBackStack(Screen.Login.route, false) }
            )
        }

        composable(Screen.Home.route) {
            // Aquí debes colocar tu pantalla Home cuando la tengas.
        }
    }
}
