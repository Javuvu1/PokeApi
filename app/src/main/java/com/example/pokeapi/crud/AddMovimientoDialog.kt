package com.example.pokeapi.crud

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pokeapi.model.Movimiento
import com.example.pokeapi.ui.AuthManager
import com.example.pokeapi.model.Pokemon

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
                TextField(
                    value = tipo,
                    onValueChange = { clase = it },
                    label = { Text("Tipo") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = clase,
                    onValueChange = { clase = it },
                    label = { Text("Clase") }
                )
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