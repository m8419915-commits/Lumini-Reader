package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mangas",
    indices = [Index(value = ["sourceId", "url"], unique = true)]
)
data class MangaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceId: Long,
    val sourceName: String = "Lumina Direct",
    val url: String,
    val title: String,
    val artist: String? = null,
    val author: String? = null,
    val description: String? = null,
    val genres: String = "", // Comma-delimited strings
    val status: Int = 1,
    val thumbnailUrl: String? = null,
    val favorite: Boolean = false,
    val lastUpdate: Long = System.currentTimeMillis(),
    val flags: Int = 0,
    val rating: Float = 4.9f
)

@Entity(
    tableName = "chapters",
    foreignKeys = [
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mangaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mangaId"), Index(value = ["mangaId", "url"], unique = true)]
)
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mangaId: Long,
    val url: String,
    val name: String,
    val scanlator: String? = null,
    val dateUpload: Long = 0,
    val chapterNumber: Float = -1f,
    val sourceOrder: Int = 0,
    val read: Boolean = false,
    val lastPageRead: Int = 0,
    val totalPages: Int = 10,
    val bookmark: Boolean = false,
    val isDownloaded: Boolean = false
)

@Entity(tableName = "reading_snaps")
data class ReadingSnapEntity(
    @PrimaryKey val mangaId: Long,
    val chapterId: Long,
    val title: String,
    val coverUrl: String?,
    val chapterNumber: Float,
    val chapterName: String = "",
    val pageIndex: Int,
    val scrollOffset: Int,
    val progressPercent: Float,
    val timestamp: Long
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val desc: String,
    val unlocked: Boolean,
    val xp: Int,
    val category: String,
    val progress: Float
)
