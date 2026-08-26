package com.example.domain.model

data class Manga(
    val id: Long = 0,
    val sourceId: Long = 1,
    val sourceName: String = "Lumina Direct",
    val url: String = "",
    val title: String,
    val artist: String = "",
    val author: String = "",
    val description: String = "",
    val genres: List<String> = emptyList(),
    val status: Int = 1, // 1: Ongoing, 2: Completed, 3: Hiatus
    val thumbnailUrl: String = "",
    val favorite: Boolean = false,
    val lastUpdate: Long = System.currentTimeMillis(),
    val flags: Int = 0,
    val rating: Float = 4.9f,
    val totalChapters: Int = 0,
    val dnaAttributes: List<DnaAttribute> = emptyList(),
    val universeNodes: List<UniverseNode> = emptyList(),
    val universeEdges: List<UniverseEdge> = emptyList()
)
