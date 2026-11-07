package com.example.pokegame.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokegame.data.model.PokemonListItem
import com.example.pokegame.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonListState(
    val pokemon: List<PokemonListItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false
)

class PokemonListViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    private val pageSize = 30

    init {
        loadNextPage()
    }

    // Carga la siguiente página; sirve también para reintentar tras un error
    fun loadNextPage() {
        val current = _state.value
        if (current.isLoading || current.endReached) return

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            repository.getPokemonList(pageSize, current.pokemon.size)
                .onSuccess { page ->
                    _state.update {
                        it.copy(
                            pokemon = it.pokemon + page,
                            isLoading = false,
                            endReached = page.size < pageSize
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo cargar la lista. Revisa tu conexión."
                        )
                    }
                }
        }
    }
}
