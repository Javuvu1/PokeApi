package com.example.pokeapi.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    // Lista de Pokémon
    private val _pokemonList = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    // Mapa de tipos por ID de Pokémon
    private val _pokemonTypes = MutableLiveData<Map<String, List<String>>>(emptyMap())
    val pokemonTypes: LiveData<Map<String, List<String>>> = _pokemonTypes

    /**
     * Carga la lista de Pokémon desde la API
     */
    fun fetchPokemonList() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPokemonList()
                _pokemonList.postValue(response.results) // Usa postValue para actualizar LiveData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Carga los tipos de un Pokémon específico por su ID
     */
    fun fetchPokemonTypes(id: String) {
        viewModelScope.launch {
            try {
                // Verifica si el ID ya está cargado para evitar solicitudes redundantes
                val currentTypes = _pokemonTypes.value ?: emptyMap()
                if (currentTypes.containsKey(id)) {
                    return@launch
                }

                val details = RetrofitInstance.api.getPokemonDetails(id)
                val types = details.types.map { it.type.name }

                // Actualiza el mapa de tipos con el nuevo ID
                _pokemonTypes.postValue(currentTypes.toMutableMap().apply {
                    put(id, types)
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

