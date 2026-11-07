package com.example.pokegame.domain

object TypeChart {

    val types = listOf(
        "normal", "fire", "water", "electric", "grass", "ice",
        "fighting", "poison", "ground", "flying", "psychic", "bug",
        "rock", "ghost", "dragon", "dark", "steel", "fairy"
    )

    private val namesEs = mapOf(
        "normal" to "Normal", "fire" to "Fuego", "water" to "Agua",
        "electric" to "Eléctrico", "grass" to "Planta", "ice" to "Hielo",
        "fighting" to "Lucha", "poison" to "Veneno", "ground" to "Tierra",
        "flying" to "Volador", "psychic" to "Psíquico", "bug" to "Bicho",
        "rock" to "Roca", "ghost" to "Fantasma", "dragon" to "Dragón",
        "dark" to "Siniestro", "steel" to "Acero", "fairy" to "Hada"
    )

    // Para cada tipo atacante: a quién le pega fuerte (x2)
    private val strong = mapOf(
        "normal" to listOf<String>(),
        "fire" to listOf("grass", "ice", "bug", "steel"),
        "water" to listOf("fire", "ground", "rock"),
        "electric" to listOf("water", "flying"),
        "grass" to listOf("water", "ground", "rock"),
        "ice" to listOf("grass", "ground", "flying", "dragon"),
        "fighting" to listOf("normal", "ice", "rock", "dark", "steel"),
        "poison" to listOf("grass", "fairy"),
        "ground" to listOf("fire", "electric", "poison", "rock", "steel"),
        "flying" to listOf("grass", "fighting", "bug"),
        "psychic" to listOf("fighting", "poison"),
        "bug" to listOf("grass", "psychic", "dark"),
        "rock" to listOf("fire", "ice", "flying", "bug"),
        "ghost" to listOf("psychic", "ghost"),
        "dragon" to listOf("dragon"),
        "dark" to listOf("psychic", "ghost"),
        "steel" to listOf("ice", "rock", "fairy"),
        "fairy" to listOf("fighting", "dragon", "dark")
    )

    // A quién le pega poco (x0.5)
    private val weak = mapOf(
        "normal" to listOf("rock", "steel"),
        "fire" to listOf("fire", "water", "rock", "dragon"),
        "water" to listOf("water", "grass", "dragon"),
        "electric" to listOf("electric", "grass", "dragon"),
        "grass" to listOf("fire", "grass", "poison", "flying", "bug", "dragon", "steel"),
        "ice" to listOf("fire", "water", "ice", "steel"),
        "fighting" to listOf("poison", "flying", "psychic", "bug", "fairy"),
        "poison" to listOf("poison", "ground", "rock", "ghost"),
        "ground" to listOf("grass", "bug"),
        "flying" to listOf("electric", "rock", "steel"),
        "psychic" to listOf("psychic", "steel"),
        "bug" to listOf("fire", "fighting", "poison", "flying", "ghost", "steel", "fairy"),
        "rock" to listOf("fighting", "ground", "steel"),
        "ghost" to listOf("dark"),
        "dragon" to listOf("steel"),
        "dark" to listOf("fighting", "dark", "fairy"),
        "steel" to listOf("fire", "water", "electric", "steel"),
        "fairy" to listOf("fire", "poison", "steel")
    )

    // A quién no le hace daño (x0)
    private val immune = mapOf(
        "normal" to listOf("ghost"),
        "electric" to listOf("ground"),
        "fighting" to listOf("ghost"),
        "poison" to listOf("steel"),
        "ground" to listOf("flying"),
        "psychic" to listOf("dark"),
        "ghost" to listOf("normal"),
        "dragon" to listOf("fairy")
    )

    fun nameEs(type: String): String = namesEs[type] ?: type

    // Multiplicador de un tipo atacante contra un solo tipo defensor
    fun multiplier(attack: String, defense: String): Double = when {
        defense in immune[attack].orEmpty() -> 0.0
        defense in strong[attack].orEmpty() -> 2.0
        defense in weak[attack].orEmpty() -> 0.5
        else -> 1.0
    }

    // Multiplicador total contra un Pokémon de uno o dos tipos
    fun multiplier(attack: String, defenderTypes: List<String>): Double =
        defenderTypes.fold(1.0) { total, type -> total * multiplier(attack, type) }
}
