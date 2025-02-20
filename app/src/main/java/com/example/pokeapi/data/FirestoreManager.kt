package com.example.pokeapi.data

import com.example.pokeapi.model.Movimiento
import com.example.pokeapi.model.MovimientoDB
import com.example.pokeapi.model.PokemonDB
import com.example.pokeapi.model.Pokemon
import com.google.firebase.firestore.snapshots
import com.example.pokeapi.ui.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: android.content.Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    companion object{
        private const val COLLECTION_POKEMON = "pokemon"
        private const val COLLECTION_MOVIMIENTOS = "movimientos"
    }

    //    Funciones de los movimientos
    fun getMovimientoByPokemonId(pokemonId: String): Flow<List<Movimiento>> {
        return firestore.collection(COLLECTION_MOVIMIENTOS)
            .whereEqualTo("pokemonId", pokemonId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.toObject(MovimientoDB::class.java)?.let { movimientoDB ->
                        Movimiento(
                            id = ds.id,
                            pokemonId = movimientoDB.pokemonId,
                            userId = movimientoDB.userId,
                            nombre = movimientoDB.nombre,
                            tipo = movimientoDB.tipo,
                            clase = movimientoDB.clase,
                            potencia = movimientoDB.potencia,
                            precision = movimientoDB.precision,
                            pp = movimientoDB.pp,
                        )
                    }
                }
            }
    }

    suspend fun addMovimiento(movimiento: Movimiento) {
        firestore.collection(COLLECTION_MOVIMIENTOS).add(movimiento).await()
    }

    suspend fun updateMovimiento(movimiento: Movimiento) {
        val movimientoRef = movimiento.id?.let {
            firestore.collection(COLLECTION_MOVIMIENTOS).document(it)
        }
        movimientoRef?.set(movimiento)?.await()
    }

    suspend fun deleteMovimientoById(movimientoId: String) {
        firestore.collection("movimientos").document(movimientoId).delete().await()
    }


    //    Funciones de los pokemon
    fun getPokemon(): Flow<List<Pokemon>> {
        return firestore.collection(COLLECTION_POKEMON)
            .whereEqualTo("userId", userId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.toObject(PokemonDB::class.java)?.let { pokemonDB ->
                        Pokemon(
                            id = ds.id,
                            userId = pokemonDB.userId,
                            name = pokemonDB.name,
                            tipo1 = pokemonDB.tipo1,
                            tipo2 = pokemonDB.tipo2,
                            hp = pokemonDB.hp,
                            atk = pokemonDB.atk,
                            def = pokemonDB.def,
                            spatk = pokemonDB.spatk,
                            spdef = pokemonDB.spdef,
                            speed = pokemonDB.speed,
                        )
                    }
                }
            }
    }

    suspend fun addPokemon(pokemon: Pokemon) {
        firestore.collection(COLLECTION_POKEMON).add(pokemon).await()
    }

    suspend fun updatePokemon(pokemon: Pokemon) {
        val pokemonRef = pokemon.id?.let {
            firestore.collection("pokemon").document(it)
        }
        pokemonRef?.set(pokemon)?.await()
    }

    suspend fun deletePokemonById(pokemonId: String) {
        firestore.collection("pokemon").document(pokemonId).delete().await()
    }

    suspend fun getPokemonById(id: String): Pokemon? {
        return firestore.collection(COLLECTION_POKEMON).document(id)
            .get().await().toObject(PokemonDB::class.java)?.let {
                Pokemon(
                    id = id,
                    userId = it.userId,
                    name = it.name,
                    tipo1 = it.tipo1,
                    tipo2 = it.tipo2,
                    hp = it.hp,
                    atk = it.atk,
                    def = it.def,
                    spatk = it.spatk,
                    spdef = it.spdef,
                    speed = it.speed,
                )
            }!!
    }
}