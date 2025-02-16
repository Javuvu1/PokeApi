package com.example.pokeapi.model

data class Pokemon(
    val id: String ?= null,
    val userId: String?,
    val name: String?,
    val tipo1: String?,
    val tipo2: String?,
    val hp: Int?,
    val atk: Int?,
    val def: Int?,
    val spatk: Int?,
    val spdef: Int?,
    val speed: Int?,
)
