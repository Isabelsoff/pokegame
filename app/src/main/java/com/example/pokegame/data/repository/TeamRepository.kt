package com.example.pokegame.data.repository

import com.example.pokegame.data.model.TeamMember
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface TeamDataSource {
    suspend fun saveTeam(team: List<TeamMember>): Result<Unit>
    suspend fun loadTeam(): Result<List<TeamMember>>
}

class TeamRepository : TeamDataSource {

    // lazy para no tocar Firebase hasta que haga falta
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private fun teamDoc() = auth.currentUser?.uid?.let { uid ->
        db.collection("users").document(uid).collection("teams").document("principal")
    }

    override suspend fun saveTeam(team: List<TeamMember>): Result<Unit> = runCatching {
        val doc = teamDoc() ?: error("No hay sesión iniciada")
        val members = team.map {
            mapOf(
                "id" to it.id,
                "name" to it.name,
                "spriteUrl" to it.spriteUrl,
                "types" to it.types
            )
        }
        doc.set(mapOf("members" to members)).await()
        Unit
    }

    override suspend fun loadTeam(): Result<List<TeamMember>> = runCatching {
        val doc = teamDoc() ?: error("No hay sesión iniciada")
        val snapshot = doc.get().await()
        val members = snapshot.get("members") as? List<*> ?: emptyList<Any>()
        members.mapNotNull { toMember(it as? Map<*, *>) }
    }

    private fun toMember(map: Map<*, *>?): TeamMember? {
        if (map == null) return null
        val id = (map["id"] as? Number)?.toInt() ?: return null
        val name = map["name"] as? String ?: return null
        val spriteUrl = map["spriteUrl"] as? String ?: ""
        val types = (map["types"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
        return TeamMember(id, name, spriteUrl, types)
    }
}