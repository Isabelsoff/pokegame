package com.example.pokegame.ui.pokemondetail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokegame.data.model.PokemonDetail
import com.example.pokegame.data.model.StatSlot
import com.example.pokegame.data.model.TeamMember
import com.example.pokegame.ui.team.AddResult
import com.example.pokegame.ui.team.TeamViewModel

@Composable
fun PokemonDetailScreen(
    onBack: () -> Unit,
    teamViewModel: TeamViewModel,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val pokemon = state.pokemon
    val error = state.error
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Volver")
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(error)
                    Button(onClick = { viewModel.load() }) {
                        Text("Reintentar")
                    }
                }
            }
            pokemon != null -> PokemonInfo(
                pokemon = pokemon,
                message = message,
                onAddToTeam = {
                    val member = TeamMember(
                        id = pokemon.id,
                        name = pokemon.name,
                        spriteUrl = pokemon.sprites.frontDefault.orEmpty(),
                        types = pokemon.types.map { it.type.name }
                    )
                    message = when (teamViewModel.add(member)) {
                        AddResult.ADDED -> "Agregado a tu equipo"
                        AddResult.FULL -> "Tu equipo ya tiene 6 Pokémon"
                        AddResult.DUPLICATE -> "Ya está en tu equipo"
                    }
                }
            )
        }
    }
}

@Composable
private fun PokemonInfo(pokemon: PokemonDetail, message: String?, onAddToTeam: () -> Unit) {
    // Usa la imagen grande si existe; si no, el sprite normal
    val image = pokemon.sprites.other?.officialArtwork?.frontDefault
        ?: pokemon.sprites.frontDefault

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = image,
            contentDescription = pokemon.name,
            modifier = Modifier.size(220.dp)
        )
        Text("#${pokemon.id}", style = MaterialTheme.typography.labelLarge)
        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Tipo: " + pokemon.types.joinToString(" / ") {
                it.type.name.replaceFirstChar { c -> c.uppercase() }
            }
        )
        // La API da la altura en decímetros y el peso en hectogramos
        Text("Altura: ${pokemon.height / 10.0} m   Peso: ${pokemon.weight / 10.0} kg")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onAddToTeam) {
            Text("Agregar al equipo")
        }
        message?.let { Text(it) }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Estadísticas base", style = MaterialTheme.typography.titleMedium)
        pokemon.stats.forEach { StatRow(it) }
    }
}

@Composable
private fun StatRow(stat: StatSlot) {
    val label = when (stat.stat.name) {
        "hp" -> "PS"
        "attack" -> "Ataque"
        "defense" -> "Defensa"
        "special-attack" -> "At. esp."
        "special-defense" -> "Def. esp."
        "speed" -> "Velocidad"
        else -> stat.stat.name
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.width(90.dp))
        Text(stat.baseStat.toString(), modifier = Modifier.width(40.dp))
        LinearProgressIndicator(
            progress = { stat.baseStat / 255f },
            modifier = Modifier.weight(1f)
        )
    }
}
