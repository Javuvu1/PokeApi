package com.example.pokeapi.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokeapi.R
import com.example.pokeapi.crud.AddMovimientoDialog
import com.example.pokeapi.crud.DeleteMovimientoDialog
import com.example.pokeapi.crud.UpdateMovimientoDialog
import com.example.pokeapi.data.FirestoreManager
import com.example.pokeapi.model.Movimiento
import com.example.pokeapi.ui.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenDetalle(
    idPokemon: String,
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToLogin: () -> Unit,
) {
    val user = auth.getCurrentUser()
    val factoryInicio = InicioViewModelFactory(firestore)
    val inicioViewModel = viewModel(InicioViewModel::class.java, factory = factoryInicio)

    val factoryDetalle = DetalleViewModelFactory(firestore, idPokemon)
    val detalleViewModel = viewModel(DetalleViewModel::class.java, factory = factoryDetalle)

    val pokemon by inicioViewModel.pokemon.collectAsState()
    val uiStateInicio by inicioViewModel.uiState.collectAsState()
    val uiStateDetalle by detalleViewModel.uiState.collectAsState()

    LaunchedEffect(idPokemon) {
        inicioViewModel.getPokemonById(idPokemon)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = user?.displayName ?: "Anónimo",
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = user?.email ?: "Sin correo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(
                        ContextCompat.getColor(
                            LocalContext.current,
                            R.color.teal_200
                        )
                    )
                ),
                actions = {
                    IconButton(onClick = {
                        inicioViewModel.onLogoutSelected()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { detalleViewModel.onAddMovimientoSelected() },
                containerColor = Color.Gray
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir movimiento")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Lista de movimientos de ${pokemon?.name}",  style = TextStyle(fontSize = 24.sp))
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (uiStateInicio.showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { inicioViewModel.dismisShowLogoutDialog() },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        inicioViewModel.dismisShowLogoutDialog()
                    }
                )
            }

            if (uiStateDetalle.showAddMovimientoDialog) {
                AddMovimientoDialog(
                    onMovimientoAdded = { movimiento ->
                        detalleViewModel.addMovimiento(
                            Movimiento(
                                id = "",
                                pokemonId = pokemon?.id,
                                userId = auth.getCurrentUser()?.uid,
                                movimiento.nombre ?: "",
                                movimiento.tipo ?: "",
                                movimiento.clase ?: "",
                                movimiento.potencia ?: 0,
                                movimiento.precision ?: 0,
                                movimiento.pp ?: 0
                            )
                        )
                        detalleViewModel.dismisShowAddMovimientoDialog()
                    },
                    onDialogDismissed = { detalleViewModel.dismisShowAddMovimientoDialog() },
                    auth
                )
            }

            if (!uiStateDetalle.movimientos.isNullOrEmpty()) {


                LazyColumn(
                    modifier = Modifier.padding(top = 60.dp)
                ) {
                    items(uiStateDetalle.movimientos) { movimiento ->
                        MovimientoItem(
                            movimiento = movimiento,
                            deleteMovimiento = {
                                detalleViewModel.deleteMovimientoById(
                                    movimiento.id ?: ""
                                )
                            },
                            updateMovimiento = {
                                detalleViewModel.updateMovimiento(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay datos")
                }
            }
        }
    }
}

@Composable
fun MovimientoItem(
    movimiento: Movimiento,
    deleteMovimiento: () -> Unit,
    updateMovimiento: (Movimiento) -> Unit,
) {

    var showDeleteMovimientoDialog by remember { mutableStateOf(false) }
    var showUpdateMovimientoDialog by remember { mutableStateOf(false) }

    if (showDeleteMovimientoDialog) {
        DeleteMovimientoDialog(
            onConfirmDelete = {
                deleteMovimiento()
                showDeleteMovimientoDialog = false
            },
            onDismiss = { showDeleteMovimientoDialog = false }
        )
    }

    if (showUpdateMovimientoDialog) {
        UpdateMovimientoDialog(
            movimiento = movimiento,
            onMovimientoUpdated = { movimiento ->
                updateMovimiento(movimiento)
                showUpdateMovimientoDialog = false
            },
            onDialogDismissed = { showUpdateMovimientoDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)


    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                movimiento.nombre?.let { Text(text = it, style = MaterialTheme.typography.titleLarge) }
                Text(
                    text = "Tipo: ${movimiento.tipo}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Clase: ${movimiento.clase}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Potencia: ${movimiento.potencia}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Precision: ${movimiento.precision}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "PP: ${movimiento.pp}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(AbsoluteAlignment.Right)
        ) {
            IconButton(
                onClick = { showUpdateMovimientoDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Actualizar Movimiento"
                )
            }
            IconButton(
                onClick = { showDeleteMovimientoDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar Movimiento"
                )
            }
        }
    }
}