package com.example.pokegame.data.model

import com.google.gson.annotations.SerializedName

// Respuesta de GET pokemon/{nombre o id}
data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatSlot>
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?,
    val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefault: String?
)

data class TypeSlot(
    val slot: Int,
    val type: NamedResource
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedResource
)

// Se usa para tipos, stats y cualquier recurso con nombre
data class NamedResource(
    val name: String,
    val url: String
)