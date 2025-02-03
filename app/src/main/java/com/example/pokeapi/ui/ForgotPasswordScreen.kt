package com.example.pokeapi.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.AuthRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Composable que representa la pantalla de recuperación de contraseña.
 * @param auth Instancia de AuthManager para manejar la autenticación.
 * @param navigateToLogin Función lambda para navegar de regreso a la pantalla de inicio de sesión.
 */
@Composable
fun ForgotPasswordScreen(auth: AuthManager, navigateToLogin: () -> Unit) {
    val context = LocalContext.current  // Obtiene el contexto de la aplicación.
    var email by remember { mutableStateOf("") } // Estado para almacenar el correo ingresado.

    val scope = rememberCoroutineScope() // Alcance de la corrutina para manejar la solicitud de recuperación.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla.
        Text(
            text = "Olvidó su contraseña",
            style = TextStyle(fontSize = 40.sp, color = Purple40)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Campo de texto para ingresar el correo electrónico.
        TextField(
            label = { Text(text = "Correo electrónico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Botón para recuperar la contraseña.
        Box(modifier = Modifier.padding(horizontal = 40.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        forgotPassword(email, auth, context, navigateToLogin)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Recuperar contraseña")
            }
        }
    }
}

/**
 * Función suspendida que maneja la solicitud de recuperación de contraseña.
 * @param email Correo electrónico ingresado por el usuario.
 * @param auth Instancia de AuthManager para realizar la solicitud de recuperación.
 * @param context Contexto de la aplicación para mostrar mensajes Toast.
 * @param navigateToLogin Función para redirigir al usuario a la pantalla de inicio de sesión.
 */
suspend fun forgotPassword(email: String, auth: AuthManager, context: Context, navigateToLogin: () -> Unit) {
    if (email.isNotEmpty()) {
        val res = withContext(Dispatchers.IO) { auth.resetPassword(email) }

        when (res) {
            is AuthRes.Success -> {
                withContext(Dispatchers.Main) { // Asegurar que la UI se actualice en el hilo principal.
                    Toast.makeText(
                        context,
                        "Se ha enviado un correo para restablecer la contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToLogin()
                }
            }

            is AuthRes.Error -> {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${res.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }

            else -> { /* No se necesita manejar otros casos, pero se deja por seguridad */ }
        }
    } else {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Por favor, ingrese un correo válido", Toast.LENGTH_SHORT).show()
        }
    }
}
