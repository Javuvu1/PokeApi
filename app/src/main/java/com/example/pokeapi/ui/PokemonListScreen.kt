package com.example.pokeapi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.pokeapi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(viewModel: PokemonViewModel = viewModel()) {
    // Observa los datos de LiveData
    val pokemonList by viewModel.pokemonList.observeAsState(emptyList())

    // Ejecuta la carga de la lista de Pokémon cuando el Composable se monta
    LaunchedEffect(Unit) {
        viewModel.fetchPokemonList()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("PokeAPI") })
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de fondo
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.fondo),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Lista de Pokémon
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(pokemonList) { pokemon ->
                    PokemonItem(pokemon, viewModel)
                }
            }
        }
    }
}

@Composable
fun PokemonItem(pokemon: Pokemon, viewModel: PokemonViewModel) {
    // Observa los tipos del Pokémon desde LiveData
    val types by viewModel.pokemonTypes.observeAsState(emptyMap())
    val pokemonTypes = types[pokemon.id] ?: emptyList()

    // Cargar los tipos solo si no están ya presentes
    LaunchedEffect(pokemon.id) {
        if (pokemon.id !in types) {
            viewModel.fetchPokemonTypes(pokemon.id)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen del Pokémon
            Image(
                painter = rememberImagePainter(data = pokemon.imageUrl),
                contentDescription = "${pokemon.name} image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )

            // Nombre y Tipos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge
                )

                // Mostrar los tipos
                if (pokemonTypes.isNotEmpty()) {
                    Row {
                        pokemonTypes.forEach { type ->
                            Text(
                                text = type.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

