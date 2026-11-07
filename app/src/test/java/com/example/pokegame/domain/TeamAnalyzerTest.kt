package com.example.pokegame.domain

import com.example.pokegame.data.model.TeamMember
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TeamAnalyzerTest {

    private val charizard = TeamMember(6, "charizard", "", listOf("fire", "flying"))

    private fun matchup(type: String) =
        TeamAnalyzer.analyze(listOf(charizard)).matchups.first { it.attackType == type }

    @Test
    fun roca_haceCuatroVecesDanoAUnFuegoVolador() {
        assertEquals(4.0, TypeChart.multiplier("rock", charizard.types), 0.0)
    }

    @Test
    fun charizardEsDebilAAguaElectricoYRoca() {
        listOf("water", "electric", "rock").forEach {
            assertEquals(1, matchup(it).weakCount)
        }
    }

    @Test
    fun charizardEsInmuneATierra() {
        assertEquals(1, matchup("ground").immuneCount)
    }

    @Test
    fun coberturaDeFuegoYVolador() {
        val uncovered = TeamAnalyzer.analyze(listOf(charizard)).uncoveredTypes
        listOf("grass", "ice", "bug", "steel", "fighting").forEach {
            assertFalse(it in uncovered)
        }
        assertTrue("water" in uncovered)
    }
}
