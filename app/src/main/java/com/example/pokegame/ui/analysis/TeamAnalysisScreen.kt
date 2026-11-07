package com.example.pokegame.ui.analysis

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokegame.domain.TeamAnalyzer
import com.example.pokegame.domain.TypeChart
import com.example.pokegame.domain.TypeMatchup
import com.example.pokegame.ui.team.TeamViewModel

@Composable
fun TeamAnalysisScreen(
    onBack: () -> Unit,
    teamViewModel: TeamViewModel,
    modifier: Modifier = Modifier
) {
    val team by teamViewModel.team.collectAsState()
    val analysis = remember(team) { TeamAnalyzer.analyze(team) }

    // Tipos donde el equipo tiene más débiles que resistentes o inmunes
    val risky = analysis.matchups
        .filter { isRisky(it) }
        .sortedByDescending { it.weakCount - it.resistCount - it.immuneCount }

    LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp)) {
        item {
            TextButton(onClick = onBack) { Text("Volver") }
        }
        item {
            Text("Análisis del equipo", style = MaterialTheme.typography.headlineSmall)
        }

        if (team.isEmpty()) {
            item {
                Text(
                    text = "Agrega Pokémon a tu equipo para ver el análisis.",
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        } else {
            item { SectionTitle("Debilidades a cubrir") }
            if (risky.isEmpty()) {
                item { Text("Tu equipo no tiene debilidades marcadas.") }
            } else {
                items(risky) { m ->
                    Text(
                        "${TypeChart.nameEs(m.attackType)}: ${m.weakCount} débiles, " +
                            "${m.resistCount + m.immuneCount} resisten"
                    )
                }
            }

            item { SectionTitle("Tipos que tu equipo no golpea fuerte") }
            if (analysis.uncoveredTypes.isEmpty()) {
                item { Text("Tu equipo cubre todos los tipos.") }
            } else {
                item {
                    Text(analysis.uncoveredTypes.joinToString(", ") { TypeChart.nameEs(it) })
                }
            }

            item { SectionTitle("Tabla completa") }
            item { HeaderRow() }
            items(analysis.matchups) { MatchupRow(it) }
        }
    }
}

private fun isRisky(m: TypeMatchup) = m.weakCount > m.resistCount + m.immuneCount

@Composable
private fun SectionTitle(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun HeaderRow() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("Tipo", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Débil", modifier = Modifier.width(56.dp), fontWeight = FontWeight.Bold)
        Text("Resiste", modifier = Modifier.width(64.dp), fontWeight = FontWeight.Bold)
        Text("Inmune", modifier = Modifier.width(64.dp), fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MatchupRow(m: TypeMatchup) {
    val color = if (isRisky(m)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(TypeChart.nameEs(m.attackType), modifier = Modifier.weight(1f), color = color)
        Text(m.weakCount.toString(), modifier = Modifier.width(56.dp), color = color)
        Text(m.resistCount.toString(), modifier = Modifier.width(64.dp), color = color)
        Text(m.immuneCount.toString(), modifier = Modifier.width(64.dp), color = color)
    }
}
