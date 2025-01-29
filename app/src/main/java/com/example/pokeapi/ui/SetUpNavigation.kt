package com.example.pokeapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.PokemonListScreen
import com.example.pokeapi.ui.SignUpScreen
import com.example.pokeapi.ui.theme.ForgotPasswordScreen
import com.example.pokeapi.ui.theme.PokemonLoginScreen

@Composable
fun SetUpNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val auth = AuthManager(context) // Suponemos que el AuthManager es accesible
    val isUserLoggedIn = auth.isUserLoggedIn() // Método ficticio para verificar si el usuario está logueado

    NavHost(navController = navController, startDestination = if (isUserLoggedIn) "pokemonList" else "login") {
        composable("login") {
            PokemonLoginScreen(
                auth = auth,
                navigateToHome = { navController.navigate("pokemonList") },
                navigateToForgotPassword = { navController.navigate("forgotPassword") }
            )
        }

        composable("signUp") {
            SignUpScreen(
                auth = auth,
                navigateToHome = { navController.navigate("pokemonList") }
            )
        }

        composable("forgotPassword") {
            ForgotPasswordScreen(
                auth = auth,
                navigateToLogin = { navController.navigate("login") }
            )
        }

        composable("pokemonList") {
            PokemonListScreen(navController = navController)
        }
    }
}
