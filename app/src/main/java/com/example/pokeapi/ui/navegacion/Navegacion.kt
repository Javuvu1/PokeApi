package com.example.pokeapi.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokeapi.crud.ScreenInicio
import com.example.pokeapi.data.FirestoreManager
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.screen.ForgotPasswordScreen
import com.example.pokeapi.ui.screen.LoginScreen
import com.example.pokeapi.ui.screen.SignUpScreen

@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val firestore = FirestoreManager(auth, context)

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                auth,
                { navController.navigate(Screen.SignUp.route) },
                {
                    navController.navigate(Screen.ScreenInicio.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(auth) {
                navController.popBackStack()
            }
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(auth) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }

        composable(Screen.ScreenInicio.route) {
            ScreenInicio(
                auth,
                firestore,
                {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ScreenInicio.route) { inclusive = true }
                    }
                },
                { id ->
                    navController.navigate(Screen.ScreenDetalle.createRoute(id))
                }
            )
        }

        composable("screenDetalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            // Aquí llamarías a tu ScreenDetalle con el ID obtenido
            // ScreenDetalle(id, auth, firestore, { ... })
        }
    }
}
