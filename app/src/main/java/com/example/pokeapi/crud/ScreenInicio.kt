package com.example.pokeapi.crud

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.pokeapi.model.Pokemon
import com.example.pokeapi.ui.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenInicio(
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToLogin: () -> Unit,
    navigateToDetalle: (String) -> Unit
) {
    val user = auth.getCurrentUser()
    val factory = InicioViewModelFactory(firestore)
    val inicioViewModel = viewModel(InicioViewModel::class.java, factory = factory)
    val uiState by inicioViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                        }
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
                            R.color.purple_500
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
                onClick = { inicioViewModel.onAddPokemonSelected() },
                containerColor = Color.Gray
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir pokemon")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Lista de pokemons",  style = TextStyle(fontSize = 24.sp))
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (uiState.showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { inicioViewModel.dismisShowLogoutDialog() },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        inicioViewModel.dismisShowLogoutDialog()
                    }
                )
            }

            if (uiState.showAddPokemonDialog) {
                AddPokemonDialog(
                    onPokemonAdded = { pokemon ->
                        inicioViewModel.addPokemon(
                            Pokemon(
                                id = "",
                                userId = auth.getCurrentUser()?.uid,
                                pokemon.name ?: "",
                                pokemon.tipo1 ?: "",
                                pokemon.tipo2 ?: "",
                                pokemon.hp ?: 0,
                                pokemon.atk ?: 0,
                                pokemon.def ?: 0,
                                pokemon.spatk ?: 0,
                                pokemon.spdef ?: 0,
                                pokemon.speed ?: 0,
                            )

                        )
                        inicioViewModel.dismisShowAddPokemonDialog()
                    },
                    onDialogDismissed = { inicioViewModel.dismisShowAddPokemonDialog() },
                    auth
                )
            }

            if (!uiState.pokemons.isNullOrEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    items(uiState.pokemons) { pokemon ->
                        PokemonItem(
                            pokemon = pokemon,
                            deletePokemon = {
                                inicioViewModel.deletePokemonById(
                                    pokemon.id ?: ""
                                )
                            },
                            updatePokemon = {
                                inicioViewModel.updatePokemon(it)
                            },
                            navigateToDetalle = { pokemon.id?.let { it1 -> navigateToDetalle(it1) } }
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
fun PokemonItem(
    pokemon: Pokemon,
    deletePokemon: () -> Unit,
    updatePokemon: (Pokemon) -> Unit,
    navigateToDetalle: (String) -> Unit
) {
    var showDeletePokemonDialog by remember { mutableStateOf(false) }
    var showUpdatePokemonDialog by remember { mutableStateOf(false) }

    if (showDeletePokemonDialog) {
        DeletePokemonDialog(
            onConfirmDelete = {
                deletePokemon()
                showDeletePokemonDialog = false
            },
            onDismiss = { showDeletePokemonDialog = false }
        )
    }

    if (showUpdatePokemonDialog) {
        UpdatePokemonDialog(
            pokemon = pokemon,
            onPokemonUpdated = { pokemon ->
                updatePokemon(pokemon)
                showUpdatePokemonDialog = false
            },
            onDialogDismissed = { showUpdatePokemonDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { pokemon.id?.let { navigateToDetalle(it) } },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            pokemon.name?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = "Tipo: ${pokemon.tipo1}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(8.dp))
                if (pokemon.tipo2?.isNotEmpty() == true) {
                    Text(text = "/ ${pokemon.tipo2}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Función para determinar el color de la barra según el valor
            fun getStatColor(value: Int): Color {
                return when {
                    value <= 70 -> Color.Red
                    value in 71..80 -> Color(0xFFFF6600) // Naranja oscuro
                    value in 81..90 -> Color(0xFFFFA500) // Naranja estándar
                    value in 91..100 -> Color.Yellow
                    value in 101..110 -> Color(0xFFBFFF00) // Amarillo verdoso
                    value in 111..120 -> Color.Green
                    value in 121..130 -> Color(0xFF00BFA5) // Verde azulado
                    value in 131..150 -> Color.Cyan
                    value in 151..170 -> Color(0xFF00AAFF) // Azul celeste
                    value in 171..200 -> Color(0xFF80D8FF) // Celeste claro
                    else -> Color(0xFFE0F7FA) // Azul muy claro para valores mayores a 200
                }
            }

            // Función para mostrar barra de progreso con color dinámico
            @Composable
            fun StatBar(label: String, value: Int, maxValue: Int = 255) {
                Column {
                    Text(text = "$label: $value", style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(
                        progress = { value / maxValue.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(MaterialTheme.shapes.small),
                        color = getStatColor(value), // Color dinámico según el valor
                        trackColor = Color(0xFFBDBDBD),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            pokemon.hp?.let { StatBar("HP", it) }
            pokemon.atk?.let { StatBar("Ataque", it) }
            pokemon.def?.let { StatBar("Defensa", it) }
            pokemon.spatk?.let { StatBar("Ataque Esp.", it) }
            pokemon.spdef?.let { StatBar("Defensa Esp.", it) }
            pokemon.speed?.let { StatBar("Velocidad", it) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { showUpdatePokemonDialog = true }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { showDeletePokemonDialog = true }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}


@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar Sesión") },
        text = {
            Text("¿Estás seguro de que deseas cerrar sesión?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}