package com.example.pokegame.data.repository

import com.example.pokegame.data.model.PokemonDetail
import com.example.pokegame.data.model.PokemonListItem
import com.example.pokegame.data.remote.PokeApiService
import com.example.pokegame.data.remote.RetrofitClient

class PokemonRepository(
    private val api: PokeApiService = RetrofitClient.api
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<PokemonListItem>> =
        runCatching { api.getPokemonList(limit, offset).results }

    suspend fun getPokemonDetail(name: String): Result<PokemonDetail> =
        runCatching { api.getPokemonDetail(name) }
}