package com.example.pokeapi.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.pokeapi.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * AuthManager maneja la autenticación de usuarios con Firebase Authentication.
 */
class AuthManager(private val context: Context) {
    // Instancia de FirebaseAuth (lazily initialized)
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    /**
     * Registra un nuevo usuario con email y contraseña en Firebase Authentication.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return [AuthRes] con el usuario registrado o un mensaje de error.
     */
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user) // Retorna el usuario autenticado
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al crear el usuario")
        }
    }

    /**
     * Inicia sesión con email y contraseña en Firebase.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return [AuthRes] con el usuario autenticado o un mensaje de error.
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user!!) // Se asume que el usuario nunca será null
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    /**
     * Obtiene el usuario actualmente autenticado en Firebase.
     * @return [FirebaseUser] si hay sesión activa, null si no.
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Cierra la sesión del usuario tanto en Firebase como en Google Sign-In.
     */
    fun signOut() {
        auth.signOut() // Cierra sesión en Firebase
        googleSignInClient.signOut() // Cierra sesión en Google
        googleSignInClient.revokeAccess() // Revoca acceso de Google
    }

    /**
     * Envía un correo para restablecer la contraseña de un usuario registrado.
     * @param email Correo del usuario.
     * @return [AuthRes] indicando éxito o error.
     */
    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit) // Devuelve éxito sin datos
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al restablecer la contraseña")
        }
    }

    /**
     * Inicia sesión de manera anónima en Firebase.
     * @return [AuthRes] con el usuario anónimo o un mensaje de error.
     */
    suspend fun signInAnonymously(): AuthRes<FirebaseUser?> {
        return try {
            val user = auth.signInAnonymously().await().user
            AuthRes.Success(user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión anónima")
        }
    }

    /**
     * Maneja el resultado del inicio de sesión con Google.
     * @param task Resultado del intento de inicio de sesión con Google.
     * @return [AuthRes] con la cuenta de Google o un mensaje de error.
     */
    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount?> {
        return try {
            val account = task.getResult(ApiException::class.java) // Obtiene la cuenta autenticada
            AuthRes.Success(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión con Google")
        }
    }

    /**
     * Autentica en Firebase usando las credenciales de Google Sign-In.
     * @param credential Credenciales de Google.
     * @return [AuthRes] con el usuario autenticado o un mensaje de error.
     */
    suspend fun googleSignInCredential(credential: AuthCredential): AuthRes<FirebaseUser?> {
        return try {
            val firebaseUser = auth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                AuthRes.Success(it)
            } ?: throw Exception("User is null")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión con Google")
        }
    }

    // Cliente de Google Sign-In, configurado con el ID del proyecto de Firebase
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // Token del proyecto Firebase
            .requestEmail() // Solicita el email del usuario
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    /**
     * Inicia el flujo de autenticación con Google.
     * @param googleSignInLauncher Launcher para iniciar la actividad de inicio de sesión de Google.
     */
    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent) // Lanza la actividad de Google Sign-In
    }
}

/**
 * Clase sellada que representa el resultado de las operaciones de autenticación.
 */
sealed class AuthRes<out T> {
    data class Success<T>(val data: T) : AuthRes<T>() // Éxito con datos
    data class Error(val errorMessage: String) : AuthRes<Nothing>() // Error con mensaje
}
