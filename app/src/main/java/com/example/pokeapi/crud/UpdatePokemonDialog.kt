package com.example.pokeapi.crud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pokeapi.model.Pokemon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePokemonDialog(
    pokemon: Pokemon,
    onPokemonUpdated: (Pokemon) -> Unit,
    onDialogDismissed: () -> Unit
) {
    var name by remember { mutableStateOf(pokemon.name!!) }
    var tipo1 by remember { mutableStateOf(pokemon.tipo1!!) }
    var tipo2 by remember { mutableStateOf(pokemon.tipo2!!) }
    var hp by remember { mutableIntStateOf(pokemon.hp!!) }
    var atk by remember { mutableIntStateOf(pokemon.atk!!) }
    var def by remember { mutableIntStateOf(pokemon.def!!) }
    var spatk by remember { mutableIntStateOf(pokemon.spatk!!) }
    var spdef by remember { mutableIntStateOf(pokemon.spdef!!) }
    var speed by remember { mutableIntStateOf(pokemon.speed!!) }

    val tiposPokemon = listOf(
        "Normal", "Fuego", "Agua", "Planta", "Eléctrico", "Hielo",
        "Lucha", "Veneno", "Tierra", "Volador", "Psíquico", "Bicho",
        "Roca", "Fantasma", "Dragón", "Siniestro", "Acero", "Hada", "Nada"
    )

    var expandedTipo1 by remember { mutableStateOf(false) }
    var expandedTipo2 by remember { mutableStateOf(false) }

    AlertDialog(
        title = { Text(text = "Actualizar pokemon") },
        onDismissRequest = {onDialogDismissed()},
        confirmButton = {
            Button(
                onClick = {
                    val newPokemon = Pokemon(
                        id = pokemon.id,
                        userId = pokemon.userId,
                        name = name,
                        tipo1 = tipo1,
                        tipo2 = tipo2,
                        hp = hp,
                        atk = atk,
                        def = def,
                        spatk = spatk,
                        spdef = spdef,
                        speed = speed,
                    )
                    onPokemonUpdated(newPokemon)
                    onDialogDismissed()
                    name = ""
                    tipo1 = ""
                    tipo2 = ""
                    hp = 0
                    atk = 0
                    def = 0
                    spatk = 0
                    spdef = 0
                    speed = 0
                }
            ) {
                Text(text = "Actualizar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDialogDismissed() }
            ) {
                Text(text = "Cancelar")
            }
        },
        text = {
            Column() {
                TextField(
                    value = name ?: "",
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Selector de Tipo 1
                ExposedDropdownMenuBox(
                    expanded = expandedTipo1,
                    onExpandedChange = { expandedTipo1 = it }
                ) {
                    TextField(
                        value = tipo1,
                        onValueChange = {},
                        label = { Text("Tipo 1") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedTipo1) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar lista"
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    DropdownMenu(
                        expanded = expandedTipo1,
                        onDismissRequest = { expandedTipo1 = false },
                        modifier = Modifier.height(250.dp)
                    ) {
                        tiposPokemon.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo) },
                                onClick = {
                                    tipo1 = tipo
                                    expandedTipo1 = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Selector de Tipo 2
                ExposedDropdownMenuBox(
                    expanded = expandedTipo2,
                    onExpandedChange = { expandedTipo2 = it }
                ) {
                    TextField(
                        value = tipo2,
                        onValueChange = {},
                        label = { Text("Tipo 2") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedTipo2) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar lista"
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    DropdownMenu(
                        expanded = expandedTipo2,
                        onDismissRequest = { expandedTipo2 = false },
                        modifier = Modifier.height(250.dp)
                    ) {
                        tiposPokemon.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo) },
                                onClick = {
                                    tipo2 = tipo
                                    expandedTipo2 = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = hp.toString(),
                    onValueChange = { hp = it.toIntOrNull() ?: 0 },
                    label = { Text("Vida") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = atk.toString(),
                    onValueChange = { atk = it.toIntOrNull() ?: 0 },
                    label = { Text("Ataque") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = def.toString(),
                    onValueChange = { def = it.toIntOrNull() ?: 0 },
                    label = { Text("Defensa") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = spatk.toString(),
                    onValueChange = { spatk = it.toIntOrNull() ?: 0 },
                    label = { Text("Ataque Especial") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = spdef.toString(),
                    onValueChange = { spdef = it.toIntOrNull() ?: 0 },
                    label = { Text("Defensa Especial") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = speed.toString(),
                    onValueChange = { speed = it.toIntOrNull() ?: 0 },
                    label = { Text("Velocidad") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
            }
        }
    )
}