package com.example.pokegame.domain

import com.example.pokegame.data.model.TeamMember

data class TypeMatchup(
    val attackType: String,
    val weakCount: Int,
    val resistCount: Int,
    val immuneCount: Int
)

data class TeamAnalysis(
    val matchups: List<TypeMatchup>,
    // Tipos a los que ningún miembro les pega fuerte con sus propios tipos
    val uncoveredTypes: List<String>
)

object TeamAnalyzer {

    fun analyze(team: List<TeamMember>): TeamAnalysis {
        val matchups = TypeChart.types.map { attack ->
            val multipliers = team.map { TypeChart.multiplier(attack, it.types) }
            TypeMatchup(
                attackType = attack,
                weakCount = multipliers.count { it > 1.0 },
                resistCount = multipliers.count { it > 0.0 && it < 1.0 },
                immuneCount = multipliers.count { it == 0.0 }
            )
        }

        val uncovered = TypeChart.types.filter { defense ->
            team.none { member ->
                member.types.any { TypeChart.multiplier(it, defense) > 1.0 }
            }
        }

        return TeamAnalysis(matchups, uncovered)
    }
}
