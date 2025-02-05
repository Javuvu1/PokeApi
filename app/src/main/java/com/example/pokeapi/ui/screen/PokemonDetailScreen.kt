package com.example.pokeapi.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PokemonDetailScreen(pokemonName: String) {
    Text(text = "Detalles de $pokemonName")
}

@Preview
@Composable
fun PreviewPokemonDetailScreen() {
    PokemonDetailScreen(pokemonName = "Pikachu")
}
