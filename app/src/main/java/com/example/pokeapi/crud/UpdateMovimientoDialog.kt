package com.example.pokeapi.crud

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pokeapi.model.Movimiento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMovimientoDialog(
    movimiento: Movimiento,
    onMovimientoUpdated: (Movimiento) -> Unit,
    onDialogDismissed: () -> Unit
) {
    var nombre by remember { mutableStateOf(movimiento.nombre) }
    var tipo by remember { mutableStateOf(movimiento.tipo) }
    var clase by remember { mutableStateOf(movimiento.clase) }
    var potencia by remember { mutableIntStateOf(movimiento.potencia!!) }
    var precision by remember { mutableIntStateOf(movimiento.precision!!) }
    var pp by remember { mutableIntStateOf(movimiento.pp!!) }

    // Estados para los menús desplegables
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedClase by remember { mutableStateOf(false) }

    // Listas de opciones
    val tiposPokemon = listOf(
        "Normal", "Fuego", "Agua", "Planta", "Eléctrico", "Hielo",
        "Lucha", "Veneno", "Tierra", "Volador", "Psíquico", "Bicho",
        "Roca", "Fantasma", "Dragón", "Siniestro", "Acero", "Hada"
    )

    val clasesMovimiento = listOf("Físico", "Especial", "Estado")

    AlertDialog(
        title = { Text(text = "Actualizar movimiento") },
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = {
                    val newMovimiento = Movimiento(
                        id = movimiento.id,
                        pokemonId = movimiento.pokemonId,
                        userId = movimiento.userId,
                        nombre = nombre,
                        tipo = tipo,
                        clase = clase,
                        potencia = potencia,
                        precision = precision,
                        pp = pp,

                    )
                    onMovimientoUpdated(newMovimiento)
                    nombre = ""
                    tipo = ""
                    clase = ""
                    potencia = 0
                    precision = 0
                    pp = 0

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
                    value = nombre ?: "",
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedTipo,
                    onExpandedChange = { expandedTipo = !expandedTipo }
                ) {
                    TextField(
                        value = tipo ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedTipo) Icons.Filled.KeyboardArrowUp
                                else Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar tipos"
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expandedTipo,
                        onDismissRequest = { expandedTipo = false },
                        modifier = Modifier.height(250.dp)
                    ) {
                        tiposPokemon.forEach { tipoItem ->
                            DropdownMenuItem(
                                text = { Text(tipoItem) },
                                onClick = {
                                    tipo = tipoItem
                                    expandedTipo = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Selector de Clase
                ExposedDropdownMenuBox(
                    expanded = expandedClase,
                    onExpandedChange = { expandedClase = !expandedClase }
                ) {
                    TextField(
                        value = clase ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Clase") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedClase) Icons.Filled.KeyboardArrowUp
                                else Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar clases"
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expandedClase,
                        onDismissRequest = { expandedClase = false }
                    ) {
                        clasesMovimiento.forEach { claseItem ->
                            DropdownMenuItem(
                                text = { Text(claseItem) },
                                onClick = {
                                    clase = claseItem
                                    expandedClase = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = potencia.toString(),
                    onValueChange = { potencia = it.toIntOrNull() ?: 0 },
                    label = { Text("Potencia") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = precision.toString(),
                    onValueChange = { precision = it.toIntOrNull() ?: 0 },
                    label = { Text("Precision") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = pp.toString(),
                    onValueChange = { pp = it.toIntOrNull() ?: 0 },
                    label = { Text("Pp") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                        KeyboardType.Number
                    )
                )
            }
        }
    )
}