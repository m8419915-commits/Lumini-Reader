package com.example.domain.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.LuminaVioletPrimary

data class ExperiencePack(
    val id: String,
    val title: String,
    val genre: String,
    val isActive: Boolean,
    val readingDirection: String = "Right to Left",
    val isRtl: Boolean = true,
    val hapticIntensity: Float = 0.85f,
    val backgroundTheme: String = "Soul Society Theme",
    val audioProfile: String = "Action/Heavy",
    val isDraft: Boolean = false
)

data class TimelineItem(
    val id: String,
    val arcBadge: String, // e.g. "Arc 1", "Chapter 1", "Major Event"
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val isMajorEvent: Boolean = false,
    val isChronological: Boolean = true
)

data class MutationBehavior(
    val id: String,
    val title: String,
    val description: String,
    val isLocked: Boolean,
    val type: String // "orientation", "zoom", "gestures"
)

data class AiChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val recommendations: List<AiRecommendationCard> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class AiRecommendationCard(
    val title: String,
    val tag1: String,
    val tag2: String,
    val description: String,
    val coverUrl: String,
    val matchScore: Int = 95
)

data class MangaUpdateItem(
    val id: String,
    val mangaId: Long,
    val title: String,
    val chapterDisplay: String, // "CH. 1160"
    val timeAgo: String, // "2 hours ago"
    val coverUrl: String,
    val isDownloaded: Boolean = false
)
