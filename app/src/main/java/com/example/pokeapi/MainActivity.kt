package com.example.pokeapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.pokeapi.model.Pokemon
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.navegacion.Navegacion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream


class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navegacion(auth)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }

}
