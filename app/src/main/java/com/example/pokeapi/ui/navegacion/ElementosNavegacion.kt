package com.example.pokeapi.ui.navegacion

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signUp")
    object ForgotPassword : Screen("forgotPassword")
    object ScreenInicio : Screen("screenInicio")
    data class ScreenDetalle(val id: String) : Screen("screenDetalle/{id}") {
        companion object {
            fun createRoute(id: String) = "screenDetalle/$id"
        }
    }
}

