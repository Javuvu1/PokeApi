package com.example.pokeapi.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val _pokemonList = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    private val _pokemonTypes = MutableLiveData<Map<String, List<String>>>(emptyMap())
    val pokemonTypes: LiveData<Map<String, List<String>>> = _pokemonTypes


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


    fun fetchPokemonTypes(id: String) {
        viewModelScope.launch {
            try {

                val currentTypes = _pokemonTypes.value ?: emptyMap()
                if (currentTypes.containsKey(id)) {
                    return@launch
                }

                val details = RetrofitInstance.api.getPokemonDetails(id)
                val types = details.types.map { it.type.name }


                _pokemonTypes.postValue(currentTypes.toMutableMap().apply {
                    put(id, types)
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

