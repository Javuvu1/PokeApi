package com.example.pokeapi.crud

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokeapi.R
import com.example.pokeapi.data.FirestoreManager
import com.example.pokeapi.ui.AuthManager

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScreenDetalle(
//    idPokemon: String,
//    auth: AuthManager,
//    firestore: FirestoreManager,
//    navigateToLogin: () -> Unit,
//) {
//    val user = auth.getCurrentUser()
//    val factoryInicio = InicioViewModelFactory(firestore)
//    val inicioViewModel = viewModel(InicioViewModel::class.java, factory = factoryInicio)
//
//    val factoryDetalle = DetalleViewModelFactory(firestore, idPokemon)
//    val detalleViewModel = viewModel(DetalleViewModel::class.java, factory = factoryDetalle)
//
//    val pokemon by inicioViewModel.pokemon.collectAsState()
//    val uiStateInicio by inicioViewModel.uiState.collectAsState()
//    val uiStateDetalle by detalleViewModel.uiState.collectAsState()
//
//    LaunchedEffect(idPokemon) {
//        inicioViewModel.getPokemonById(idPokemon)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(
//                        horizontalArrangement = Arrangement.Start,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        if (user?.photoUrl != null) {
//                            AsyncImage(
//                                model = ImageRequest.Builder(LocalContext.current)
//                                    .data(user?.photoUrl)
//                                    .crossfade(true)
//                                    .build(),
//                                contentDescription = "Imagen",
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .clip(CircleShape)
//                                    .size(40.dp)
//                            )
//                        } else {
//                            Image(
//                                painter = painterResource(R.drawable.ic_usuario),
//                                contentDescription = "Foto de perfil por defecto",
//                                modifier = Modifier
//                                    .padding(end = 8.dp)
//                                    .size(40.dp)
//                                    .clip(CircleShape)
//                            )
//
//                        }
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Column {
//                            Text(
//                                text = user?.displayName ?: "Anónimo",
//                                fontSize = 20.sp,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                            Text(
//                                text = user?.email ?: "Sin correo",
//                                fontSize = 12.sp,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(
//                        ContextCompat.getColor(
//                            LocalContext.current,
//                            R.color.gris_oscuro
//                        )
//                    )
//                ),
//                actions = {
//                    IconButton(onClick = {
//                        inicioViewModel.onLogoutSelected()
//                    }) {
//                        Icon(
//                            Icons.AutoMirrored.Outlined.ExitToApp,
//                            contentDescription = "Cerrar sesión"
//                        )
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { detalleViewModel.onAddProfesorSelected() },
//                containerColor = Color.Gray
//            ) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir profesor")
//            }
//        }
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it)
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                Text("Lista de profesores de la pokemon ${pokemon?.nombre}",  style = TextStyle(fontSize = 24.sp))
//                Spacer(modifier = Modifier.height(10.dp))
//            }
//
//            if (uiStateInicio.showLogoutDialog) {
//                LogoutDialog(
//                    onDismiss = { inicioViewModel.dismisShowLogoutDialog() },
//                    onConfirm = {
//                        auth.signOut()
//                        navigateToLogin()
//                        inicioViewModel.dismisShowLogoutDialog()
//                    }
//                )
//            }
//
//            if (uiStateDetalle.showAddProfesorDialog) {
//                AddProfesorDialog(
//                    onProfesorAdded = { profesor ->
//                        detalleViewModel.addProfesor(
//                            Profesor(
//                                id = "",
//                                pokemonId = pokemon?.id,
//                                userId = auth.getCurrentUser()?.uid,
//                                profesor.nombre ?: "",
//                                profesor.apellidos ?: "",
//                                profesor.email ?: ""
//                            )
//                        )
//                        detalleViewModel.dismisShowAddProfesorDialog()
//                    },
//                    onDialogDismissed = { detalleViewModel.dismisShowAddProfesorDialog() },
//                    auth
//                )
//            }
//
//            if (!uiStateDetalle.profesores.isNullOrEmpty()) {
//
//
//                LazyColumn(
//                    modifier = Modifier.padding(top = 60.dp)
//                ) {
//                    items(uiStateDetalle.profesores) { profesor ->
//                        ProfesorItem(
//                            profesor = profesor,
//                            deleteProfesor = {
//                                detalleViewModel.deleteProfesoraById(
//                                    profesor.id ?: ""
//                                )
//                            },
//                            updateProfesor = {
//                                detalleViewModel.updateProfesor(it)
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//                }
//            } else {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text("No hay datos")
//                }
//            }
//        }
//    }
//}

//@Composable
//fun ProfesorItem(
//    profesor: Profesor,
//    deleteProfesor: () -> Unit,
//    updateProfesor: (Profesor) -> Unit,
//) {
//
//    var showDeleteProfesorDialog by remember { mutableStateOf(false) }
//    var showUpdateProfesorDialog by remember { mutableStateOf(false) }
//
//    if (showDeleteProfesorDialog) {
//        DeleteProfesorDialog(
//            onConfirmDelete = {
//                deleteProfesor()
//                showDeleteProfesorDialog = false
//            },
//            onDismiss = { showDeleteProfesorDialog = false }
//        )
//    }
//
//    if (showUpdateProfesorDialog) {
//        UpdateProfesorDialog(
//            profesor = profesor,
//            onProfesorUpdated = { profesor ->
//                updateProfesor(profesor)
//                showUpdateProfesorDialog = false
//            },
//            onDialogDismissed = { showUpdateProfesorDialog = false }
//        )
//    }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//
//
//    ) {
//        Row(modifier = Modifier.padding(16.dp)) {
//            Column {
//                Text(text = "Profesor", style = MaterialTheme.typography.titleLarge)
//                Text(
//                    text = "Nombre: ${profesor.nombre}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//                Text(
//                    text = "Apellidos: ${profesor.apellidos}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//                Text(
//                    text = "Email: ${profesor.email}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .align(AbsoluteAlignment.Right)
//        ) {
//            IconButton(
//                onClick = { showUpdateProfesorDialog = true }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = "Actualizar Profesor"
//                )
//            }
//            IconButton(
//                onClick = { showDeleteProfesorDialog = true }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = "Borrar Profesor"
//                )
//            }
//        }
//    }
//}