package com.example.pokeapi.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object login

@Serializable
object signUp

@Serializable
object forgotPassword

@Serializable
object screenInicio

@Serializable
data class screenDetalle(val id: String)

