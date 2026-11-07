package com.example.pokegame.ui.team

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokegame.data.model.TeamMember

@Composable
fun TeamScreen(
    onBack: () -> Unit,
    onAnalyzeClick: () -> Unit,
    teamViewModel: TeamViewModel,
    modifier: Modifier = Modifier
) {
    val team by teamViewModel.team.collectAsState()
    val syncError by teamViewModel.syncError.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) {
            Text("Volver")
        }
        Text(
            text = "Mi equipo (${team.size}/${TeamViewModel.MAX_TEAM_SIZE})",
            style = MaterialTheme.typography.headlineSmall
        )
        syncError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = onAnalyzeClick,
            enabled = team.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Analizar equipo")
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(team, key = { it.id }) { member ->
                MemberCard(member, onRemove = { teamViewModel.remove(member.id) })
            }
            // Espacios que faltan para completar los 6
            items(TeamViewModel.MAX_TEAM_SIZE - team.size) {
                EmptySlot()
            }
        }
    }
}

@Composable
private fun MemberCard(member: TeamMember, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = member.spriteUrl,
                contentDescription = member.name,
                modifier = Modifier.size(64.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = member.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = member.types.joinToString(" / ") {
                        it.replaceFirstChar { c -> c.uppercase() }
                    }
                )
            }
            TextButton(onClick = onRemove) {
                Text("Quitar")
            }
        }
    }
}

@Composable
private fun EmptySlot() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(88.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Espacio libre")
        }
    }
}