package com.example.pokegame.ui.pokemondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokegame.data.model.PokemonDetail
import com.example.pokegame.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonDetailState(
    val pokemon: PokemonDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class PokemonDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = PokemonRepository()

    // El nombre llega como argumento de la ruta de navegación
    private val name: String = checkNotNull(savedStateHandle.get<String>("name"))

    private val _state = MutableStateFlow(PokemonDetailState())
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            repository.getPokemonDetail(name)
                .onSuccess { detail ->
                    _state.update { it.copy(pokemon = detail, isLoading = false) }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo cargar el Pokémon. Revisa tu conexión."
                        )
                    }
                }
        }
    }
}
