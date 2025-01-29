package com.example.pokeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.ui.navigation.SetUpNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = AuthManager(applicationContext) // Pasamos el contexto

        setContent {
            val navController = rememberNavController()
            SetUpNavigation(navController = navController, auth = auth)
        }
    }
}
