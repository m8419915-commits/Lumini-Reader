package com.example.luminareader.data.model

enum class ScreenType {
    HOME,
    LIBRARY,
    HISTORY,
    EXPLORE,
    UPDATES,
    MORE,
    DETAIL,
    READER,
    UNIVERSE,
    DNA,
    FORGE,
    MUTATION,
    AI,
    JOURNEY,
    TIMELINE,
    BACKUP,
    SETTINGS,
    REPOSITORIES,
    MIGRATION
}

data class ScreenState(
    val type: ScreenType = ScreenType.HOME,
    val mangaId: Int? = null,
    val chapterId: Int? = null,
    val initialPage: Int = 0,
    val sourceId: String? = null
)

enum class ReaderMode {
    CONTINUOUS_WEBTOON,
    SINGLE_PAGE_LTR,
    SINGLE_PAGE_RTL,
    DUAL_PAGE_SPREAD
}

enum class BackgroundTint {
    PITCH_BLACK,
    CHARCOAL,
    SEPIA,
    DEEP_SLATE
}

data class ReaderConfig(
    val readerMode: ReaderMode = ReaderMode.CONTINUOUS_WEBTOON,
    val backgroundTint: BackgroundTint = BackgroundTint.PITCH_BLACK,
    val enableLuminaFlow: Boolean = true,
    val keepScreenOn: Boolean = true,
    val wifiOnlyDownload: Boolean = false,
    val hardwareAcceleration: Boolean = true,
    val zoomLevel: Float = 1.0f,
    val brightness: Int = 100,
    val cropWhiteBorders: Boolean = false,
    val colorFilter: String = "none"
)

enum class TrackerStatus {
    READING,
    COMPLETED,
    ON_HOLD,
    DROPPED,
    PLAN_TO_READ,
    REREADING
}

data class MangaTracker(
    val service: String,
    val serviceName: String,
    val color: Long,
    val isConnected: Boolean,
    val trackingId: String? = null,
    val title: String,
    val status: TrackerStatus = TrackerStatus.READING,
    val score: Float = 0f,
    val lastChapterRead: Int = 0,
    val totalChapters: Int = 0,
    val startDate: String? = null,
    val finishDate: String? = null
)

data class Manga(
    val id: Int,
    val title: String,
    val author: String,
    val artist: String,
    val description: String,
    val genre: List<String>,
    val status: String,
    val thumbnailUrl: String,
    val bannerUrl: String,
    val inLibrary: Boolean = false,
    val category: String = "Default",
    val rating: Float = 4.8f,
    val source: String = "MangaDex",
    val sourceId: String = "mangadex",
    val totalChapters: Int = 100,
    val latestChapter: String = "Ch. 1",
    val unreadCount: Int = 0,
    val lastReadAt: Long? = null,
    val trackers: List<MangaTracker> = emptyList()
)

data class Chapter(
    val id: Int,
    val mangaId: Int,
    val chapterNumber: Float,
    val title: String,
    val scanlator: String = "Official",
    val dateUpload: String = "Recently",
    val isRead: Boolean = false,
    val isDownloaded: Boolean = false,
    val pageCount: Int = 10,
    val pages: List<String> = emptyList()
)

data class ReadingSnap(
    val mangaId: Int,
    val title: String,
    val coverUrl: String,
    val chapterId: Int,
    val chapterNumber: Float,
    val pageIndex: Int,
    val totalPages: Int,
    val progressPercent: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class HistoryItem(
    val id: String,
    val mangaId: Int,
    val mangaTitle: String,
    val coverUrl: String,
    val chapterId: Int,
    val chapterNumber: Float,
    val chapterTitle: String? = null,
    val pageIndex: Int = 0,
    val totalPages: Int = 20,
    val timestamp: Long = System.currentTimeMillis(),
    val progressPercent: Int = 0,
    val dateGroup: String = "Today",
    val timeString: String = "Just now"
)

enum class TriState {
    ALL,
    INCLUDED,
    EXCLUDED,
    NONE
}

data class LibraryFilters(
    val downloaded: TriState = TriState.NONE,
    val unread: TriState = TriState.NONE,
    val started: TriState = TriState.NONE,
    val completed: TriState = TriState.NONE,
    val category: String = "All",
    val source: String = "all",
    val sortBy: String = "alphabetical",
    val sortOrder: String = "asc",
    val displayMode: String = "compact_grid"
)

data class Category(
    val id: String,
    val name: String,
    val order: Int,
    val count: Int = 0,
    val isDefault: Boolean = false
)

data class UniverseNode(
    val id: String,
    val name: String,
    val role: String,
    val powerLevel: String,
    val faction: String,
    val bio: String,
    val color: Long,
    val x: Float = 0.5f,
    val y: Float = 0.5f
)

data class UniverseEdge(
    val fromNodeId: String,
    val toNodeId: String,
    val relationLabel: String,
    val color: Long
)

data class DnaAttribute(
    val trait: String,
    val intensity: Int,
    val description: String,
    val color: Long
)

data class Achievement(
    val id: String,
    val title: String,
    val desc: String,
    val xp: Int,
    val unlocked: Boolean
)

data class ExperiencePack(
    val id: String,
    val title: String,
    val genre: String,
    val isActive: Boolean = false,
    val isDraft: Boolean = false,
    val readingDirection: String = "Vertical Webtoon",
    val hapticIntensity: Int = 75,
    val backgroundTheme: String = "AMOLED Pitch Black",
    val audioProfile: String = "Ambient Synth Resonance",
    val description: String = ""
)

data class MutationBehavior(
    val id: String,
    val title: String,
    val type: String,
    val description: String,
    val isLocked: Boolean = false
)

data class AiRecommendationCard(
    val id: String,
    val title: String,
    val coverUrl: String,
    val tag1: String,
    val tag2: String,
    val description: String,
    val matchScore: Int
)

data class AiChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val recommendations: List<AiRecommendationCard> = emptyList()
)

data class MangaUpdateItem(
    val id: String,
    val mangaId: Int,
    val title: String,
    val chapterDisplay: String,
    val coverUrl: String,
    val timeAgo: String,
    val isDownloaded: Boolean = false
)

data class SourceMeta(
    val id: String,
    val name: String,
    val lang: String,
    val baseUrl: String,
    val isPinned: Boolean = false,
    val isNsfw: Boolean = false,
    val supportsLatest: Boolean = true,
    val status: String = "online",
    val icon: String,
    val extensionPkg: String,
    val itemCount: Int = 0,
    val version: String = "1.0.0"
)

data class ExtensionPackage(
    val name: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Int,
    val lang: String,
    val isNsfw: Boolean = false,
    val apk: String,
    val icon: String,
    val repoId: String,
    val repoName: String,
    val isInstalled: Boolean = false,
    val hasUpdate: Boolean = false,
    val sources: List<SourceMeta> = emptyList()
)

data class ExtensionStore(
    val id: String,
    val name: String,
    val website: String,
    val isOfficial: Boolean,
    val isPinned: Boolean,
    val enabled: Boolean,
    val lastSynced: String,
    val totalExtensions: Int
)

data class SourceMigrationItem(
    val id: String,
    val mangaId: Int,
    val mangaTitle: String,
    val mangaCover: String,
    val fromSourceName: String,
    val toSourceName: String,
    val targetMangaTitle: String,
    val targetChapterCount: Int,
    val status: String = "ready",
    val matchScore: Int = 98
)

data class TimelineItem(
    val id: String,
    val title: String,
    val arcBadge: String,
    val description: String,
    val isMajorEvent: Boolean = true,
    val dateOrChapter: String
)
