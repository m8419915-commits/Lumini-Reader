package com.example.domain.model

data class Chapter(
    val id: Long = 0,
    val mangaId: Long,
    val url: String = "",
    val name: String,
    val scanlator: String = "Official Lumina Scan",
    val dateUpload: Long = System.currentTimeMillis(),
    val chapterNumber: Float = 1f,
    val sourceOrder: Int = 0,
    val read: Boolean = false,
    val lastPageRead: Int = 0,
    val totalPages: Int = 12,
    val bookmark: Boolean = false,
    val pageUrls: List<String> = emptyList(),
    val isDownloaded: Boolean = false
)
