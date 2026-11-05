package com.example.pokegame.data.model

// Respuesta de GET pokemon?limit=..&offset=..
data class PokemonListResponse(
    val count: Int,
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
) {
    // El id viene al final de la url: .../pokemon/25/
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toInt()

    val spriteUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}