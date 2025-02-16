package com.example.pokeapi.model

data class PokemonDB(
    val name: String = "",
    val userId: String = "",
    val tipo1: String = "",
    val tipo2: String = "",
    val hp: Int = 0,
    val atk: Int = 0,
    val def: Int = 0,
    val spatk: Int = 0,
    val spdef: Int = 0,
    val speed: Int = 0,
)
