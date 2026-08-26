package com.example.domain.model

data class ReadingSnap(
    val mangaId: Long,
    val chapterId: Long,
    val title: String,
    val coverUrl: String,
    val chapterNumber: Float,
    val chapterName: String = "",
    val pageIndex: Int,
    val scrollOffset: Int = 0,
    val progressPercent: Float,
    val timestamp: Long = System.currentTimeMillis()
)
