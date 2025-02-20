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
import com.example.pokeapi.model.Movimiento
import com.example.pokeapi.ui.AuthManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovimientoDialog(
    onMovimientoAdded: (Movimiento) -> Unit,
    onDialogDismissed: () -> Unit,
    auth: AuthManager,
) {

    var pokemonId by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var clase by remember { mutableStateOf("") }
    var potencia by remember { mutableIntStateOf(0) }
    var precision by remember { mutableIntStateOf(0) }
    var pp by remember { mutableIntStateOf(0) }

    var expandedTipo by remember { mutableStateOf(false) }
    var expandedClase by remember { mutableStateOf(false) }

    val clasesMovimiento = listOf("Físico", "Especial", "Estado")

    val tiposPokemon = listOf(
        "Normal", "Fuego", "Agua", "Planta", "Eléctrico", "Hielo",
        "Lucha", "Veneno", "Tierra", "Volador", "Psíquico", "Bicho",
        "Roca", "Fantasma", "Dragón", "Siniestro", "Acero", "Hada"
    )


    AlertDialog(
        title = { Text("Añadir movimiento") },
        onDismissRequest = { onDialogDismissed() },
        confirmButton = {
            Button(
                onClick = {
                    val newMovimiento = Movimiento(
                        pokemonId = pokemonId,
                        userId = auth.getCurrentUser()?.uid,
                        nombre = nombre,
                        tipo = tipo,
                        clase = clase,
                        potencia = potencia,
                        precision = precision,
                        pp = pp,
                    )
                    onMovimientoAdded(newMovimiento)
                    pokemonId = ""
                    nombre = ""
                    tipo = ""
                    clase = ""
                    potencia = 0
                    precision = 0
                    pp = 0
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDialogDismissed() }
            ) {
                Text("Cancelar")
            }
        },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Selector de Tipo
                ExposedDropdownMenuBox(
                    expanded = expandedTipo,
                    onExpandedChange = { expandedTipo = !expandedTipo }
                ) {
                    TextField(
                        value = tipo,
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
                        value = clase,
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = precision.toString(),
                    onValueChange = { precision = it.toIntOrNull() ?: 0 },
                    label = { Text("Precision") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = pp.toString(),
                    onValueChange = { pp = it.toIntOrNull() ?: 0 },
                    label = { Text("PP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    )
}