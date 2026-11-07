package com.example.pokegame.ui.team

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokegame.data.model.TeamMember
import com.example.pokegame.data.repository.TeamDataSource
import com.example.pokegame.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AddResult { ADDED, FULL, DUPLICATE }

class TeamViewModel(
    private val repository: TeamDataSource = TeamRepository()
) : ViewModel() {

    private val _team = MutableStateFlow<List<TeamMember>>(emptyList())
    val team: StateFlow<List<TeamMember>> = _team.asStateFlow()

    private val _syncError = MutableStateFlow<String?>(null)
    val syncError: StateFlow<String?> = _syncError.asStateFlow()

    fun add(member: TeamMember): AddResult {
        val current = _team.value
        return when {
            current.any { it.id == member.id } -> AddResult.DUPLICATE
            current.size >= MAX_TEAM_SIZE -> AddResult.FULL
            else -> {
                _team.value = current + member
                save()
                AddResult.ADDED
            }
        }
    }

    fun remove(id: Int) {
        _team.update { list -> list.filter { it.id != id } }
        save()
    }

    // Trae el equipo guardado del usuario con sesión iniciada
    fun loadTeam() {
        viewModelScope.launch {
            repository.loadTeam()
                .onSuccess { saved ->
                    _team.value = saved
                    _syncError.value = null
                }
                .onFailure { e ->
                    Log.e("TeamViewModel", "Error al cargar el equipo", e)
                    _syncError.value = "No se pudo cargar tu equipo guardado."
                }
        }
    }

    // Solo limpia lo local (al cerrar sesión); no borra nada en la nube
    fun clearTeam() {
        _team.value = emptyList()
        _syncError.value = null
    }

    private fun save() {
        val snapshot = _team.value
        viewModelScope.launch {
            repository.saveTeam(snapshot)
                .onSuccess { _syncError.value = null }
                .onFailure { e ->
                    Log.e("TeamViewModel", "Error al guardar el equipo", e)
                    _syncError.value = "No se pudo guardar tu equipo en la nube."
                }
        }
    }

    companion object {
        const val MAX_TEAM_SIZE = 6
    }
}