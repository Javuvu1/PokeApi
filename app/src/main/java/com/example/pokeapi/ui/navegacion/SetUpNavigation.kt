package com.example.pokeapi.ui.navigation

// Importaciones necesarias para la navegación y la composición en Jetpack Compose
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.SignUpScreen
import com.example.pokeapi.ui.theme.ForgotPasswordScreen
import com.example.pokeapi.ui.theme.LoginScreen
import com.example.pokeapi.ui.PokemonListScreen
import com.example.pokeapi.ui.model.PokemonViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Función que configura la navegación de la aplicación.
 * @param navController Controlador de navegación para gestionar el cambio de pantallas.
 * @param auth Instancia de AuthManager para manejar la autenticación de usuarios.
 */
@Composable
fun SetUpNavigation(navController: NavHostController, auth: AuthManager) {
    // Se obtiene una instancia del ViewModel de Pokémon para la pantalla principal.
    val pokemonViewModel: PokemonViewModel = viewModel()

    // Definimos el NavHost, que contiene las rutas de navegación de la app.
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // La pantalla de inicio es el Login.
    ) {
        /**
         * Pantalla de inicio de sesión.
         * Contiene las opciones de navegar al registro, al home (si la autenticación es exitosa)
         * o a la pantalla de recuperación de contraseña.
         */
        composable(Screen.Login.route) {
            LoginScreen(
                auth = auth,
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                navigateToHome = { navController.navigate(Screen.Home.route) },
                navigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        /**
         * Pantalla de registro de usuario.
         * Una vez registrado, se redirige automáticamente a la pantalla Home.
         */
        composable(Screen.SignUp.route) {
            SignUpScreen(
                auth = auth,
                navigateToHome = { navController.navigate(Screen.Home.route) }
            )
        }

        /**
         * Pantalla para restablecer la contraseña.
         * Al completar el proceso, se vuelve a la pantalla de inicio de sesión.
         */
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                auth = auth,
                navigateToLogin = { navController.popBackStack(Screen.Login.route, false) }
            )
        }

        /**
         * Pantalla principal donde se muestra la lista de Pokémon.
         * Se inyecta el ViewModel y el NavController para la navegación.
         */
        composable(Screen.Home.route) {
            PokemonListScreen(viewModel = pokemonViewModel, navController = navController)
        }
    }
}
