package com.example.pokeapi.model

data class Movimiento (
    val id: String ?= null,
    val pokemonId: String?,
    val userId: String?,
    val nombre: String?,
    val tipo: String?,
    val clase: String?,
    val potencia: Int?,
    val precision: Int?,
    val pp: Int?,
)