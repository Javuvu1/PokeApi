package com.example.pokeapi.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokeapi.R
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    auth: AuthManager,
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Manejo de inicio de sesión con Google
    val googleSignLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val account = auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
        if (account is AuthRes.Success) {
            val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
            scope.launch {
                val firebaseUser = auth.googleSignInCredential(credential)
                if (firebaseUser is AuthRes.Success) {
                    Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "¿No tienes cuenta? Regístrate",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .clickable { navigateToSignUp() },
            style = TextStyle(
                color = Purple40,
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_firebase),
                contentDescription = "Firebase",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Firebase Android", style = TextStyle(fontSize = 30.sp))
            Spacer(modifier = Modifier.height(30.dp))

            // Campo de entrada para el correo
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Campo de entrada para la contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Botón para iniciar sesión
            Button(
                onClick = {
                    scope.launch {
                        signIn(email, password, context, auth, navigateToHome)
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(50.dp)
            ) {
                Text("Iniciar Sesión".uppercase())
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Enlace para recuperar la contraseña
            Text(
                text = "¿Olvidaste tu contraseña?",
                modifier = Modifier.clickable { navigateToForgotPassword() },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = Purple40
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "-------- o --------", style = TextStyle(color = Color.Gray))
            Spacer(modifier = Modifier.height(25.dp))

            // Botón de acceso como invitado
            SocialMediaButton(
                onClick = {
                    scope.launch {
                        signAnonimous(auth, navigateToHome, context)
                    }
                },
                text = "Continuar como invitado",
                icon = R.drawable.ic_incognito,
                color = Color(0xFF363636)
            )
            Spacer(modifier = Modifier.height(15.dp))

            // Botón de acceso con Google
            SocialMediaButton(
                onClick = { auth.signInWithGoogle(googleSignLauncher) },
                text = "Continuar con Google",
                icon = R.drawable.ic_google,
                color = Color(0xFFF1F1F1)
            )
        }
    }
}

// Función para iniciar sesión anónimo
suspend fun signAnonimous(auth: AuthManager, navigateToHome: () -> Unit, context: Context) {
    val res = withContext(Dispatchers.IO) { auth.signInAnonymously() }
    if (res is AuthRes.Success) {
        Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
        navigateToHome()
    } else {
        Toast.makeText(context, (res as AuthRes.Error).errorMessage, Toast.LENGTH_SHORT).show()
    }
}

// Función para iniciar sesión con correo y contraseña
suspend fun signIn(email: String, password: String, context: Context, auth: AuthManager, navigateToHome: () -> Unit) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        val result = withContext(Dispatchers.IO) { auth.signInWithEmailAndPassword(email, password) }
        if (result is AuthRes.Success) {
            Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
            navigateToHome()
        } else {
            Toast.makeText(context, (result as AuthRes.Error).errorMessage, Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Email y password tienen que estar rellenos", Toast.LENGTH_SHORT).show()
    }
}

// Composable para botones de redes sociales
@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, if (icon == R.drawable.ic_incognito) color else Color.Gray),
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = if (icon == R.drawable.ic_incognito) Color.White else Color.Black)
        }
    }
}
