package com.example.pokegame.ui.team

import com.example.pokegame.data.model.TeamMember
import org.junit.Assert.assertEquals
import org.junit.Test

class TeamViewModelTest {

    private fun member(id: Int) = TeamMember(id, "pokemon$id", "", listOf("normal"))

    @Test
    fun agregaUnPokemon() {
        val vm = TeamViewModel()
        assertEquals(AddResult.ADDED, vm.add(member(1)))
        assertEquals(1, vm.team.value.size)
    }

    @Test
    fun rechazaRepetidos() {
        val vm = TeamViewModel()
        vm.add(member(1))
        assertEquals(AddResult.DUPLICATE, vm.add(member(1)))
        assertEquals(1, vm.team.value.size)
    }

    @Test
    fun rechazaElSeptimo() {
        val vm = TeamViewModel()
        (1..6).forEach { assertEquals(AddResult.ADDED, vm.add(member(it))) }
        assertEquals(AddResult.FULL, vm.add(member(7)))
        assertEquals(6, vm.team.value.size)
    }

    @Test
    fun quitaUnPokemon() {
        val vm = TeamViewModel()
        vm.add(member(1))
        vm.add(member(2))
        vm.remove(1)
        assertEquals(listOf(2), vm.team.value.map { it.id })
    }
}
