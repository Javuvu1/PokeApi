package com.example.pokeapi.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.data.FirestoreManager
import com.example.pokeapi.model.Movimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleViewModel(val firestoreManager: FirestoreManager, val idPokemon: String) : ViewModel() {

    val _uiState = MutableStateFlow(UiStateDetalle())
    val uiState: StateFlow<UiStateDetalle> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestoreManager.getMovimientoByPokemonId(idPokemon).collect { movimientos ->
                _uiState.update { uiState ->
                    uiState.copy(
                        movimientos = movimientos,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            firestoreManager.addMovimiento(movimiento)
        }
    }

    fun updateMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            firestoreManager.updateMovimiento(movimiento)
        }
    }

    fun deleteMovimientoById(movimientoId: String) {
        if (movimientoId.isEmpty()) return
        viewModelScope.launch {
            firestoreManager.deleteMovimientoById(movimientoId)
        }
    }

    fun onAddMovimientoSelected() {
        _uiState.update { it.copy(showAddMovimientoDialog = true) }
    }

    fun dismisShowAddMovimientoDialog() {
        _uiState.update { it.copy(showAddMovimientoDialog = false) }
    }
}

data class UiStateDetalle(
    val movimientos: List<Movimiento> = emptyList(),
    val isLoading: Boolean = false,
    val showAddMovimientoDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

class DetalleViewModelFactory(private val firestoreManager: FirestoreManager, private val idPokemon: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetalleViewModel(firestoreManager, idPokemon) as T
    }
}