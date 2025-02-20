package com.example.pokeapi.crud

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteMovimientoDialog(onConfirmDelete: () -> Unit, onDismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Borrar movimiento") },
        text = { Text("¿Quieres borrar el movimiento?") },
        confirmButton = {
            Button(
                onClick = onConfirmDelete
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}