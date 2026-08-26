package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.LuminaRepository
import com.example.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    data object Home : Screen()
    data object Library : Screen()
    data object Explore : Screen()
    data object Updates : Screen()
    data object More : Screen()
    data object Analytics : Screen()
    data object Settings : Screen()
    data object JourneyMap : Screen()
    data object Mutation : Screen()
    data object Forge : Screen()
    data object BackupSync : Screen()
    data object AiAssistant : Screen()
    data object Repositories : Screen()
    data class MangaDetail(val mangaId: Long) : Screen()
    data class Reader(val mangaId: Long, val chapterId: Long, val initialPage: Int = 0, val initialScroll: Int = 0) : Screen()
    data class UniverseMap(val mangaId: Long) : Screen()
    data class MangaDna(val mangaId: Long) : Screen()
    data class Timeline(val mangaId: Long) : Screen()
}

data class LuminaUiState(
    val currentScreen: Screen = Screen.Home,
    val libraryMangas: List<Manga> = emptyList(),
    val allMangas: List<Manga> = emptyList(),
    val activeSnaps: List<ReadingSnap> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val streakDays: Int = 15,
    val selectedManga: Manga? = null,
    val selectedChapters: List<Chapter> = emptyList(),
    val currentChapter: Chapter? = null,
    val readerConfig: ReaderConfig = ReaderConfig(),
    val installedExtensions: Set<String> = emptySet(),
    val availableExtensions: List<ExtensionPackage> = emptyList(),
    val isRepoLoading: Boolean = false,
    val repoErrorMessage: String? = null,
    val repoUrlInput: String = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.pb",
    val searchQuery: String = "",
    val selectedGenreFilter: String = "All",
    val showSnapSwitcher: Boolean = false,
    val readingSpeedPpm: Float = 2.4f,
    val totalReadingMinutes: Int = 5052, // 84h 12m
    val chaptersReadCount: Int = 428,
    // Lumina Forge Packs
    val experiencePacks: List<ExperiencePack> = listOf(
        ExperiencePack(
            id = "bleach_pack",
            title = "My Bleach Experience",
            genre = "Shonen",
            isActive = true,
            readingDirection = "Right to Left",
            isRtl = true,
            hapticIntensity = 0.85f,
            backgroundTheme = "Soul Society Theme",
            audioProfile = "Action/Heavy",
            isDraft = false
        ),
        ExperiencePack(
            id = "midnight_reading",
            title = "Midnight Reading",
            genre = "Quiet",
            isActive = false,
            readingDirection = "Continuous Vertical",
            isRtl = false,
            hapticIntensity = 0.20f,
            backgroundTheme = "AMOLED Pitch Black",
            audioProfile = "Subtle Lo-Fi Rain",
            isDraft = true
        )
    ),
    // Lumina Mutation Behaviors
    val mutationBehaviors: List<MutationBehavior> = listOf(
        MutationBehavior(
            id = "b1",
            title = "Prefer Landscape for Action",
            description = "System detected 85% tilt rate during action-heavy sequences. Landscape auto-switch enabled.",
            isLocked = true,
            type = "orientation"
        ),
        MutationBehavior(
            id = "b2",
            title = "Auto-zoom on Details",
            description = "Double-tap frequency on dense panels analyzed. Smart zoom now focuses automatically.",
            isLocked = false,
            type = "zoom"
        ),
        MutationBehavior(
            id = "b3",
            title = "Gestures Adapted",
            description = "Swipe velocity tailored to reading speed. Page turn animation duration tuned to 180ms.",
            isLocked = true,
            type = "gestures"
        )
    ),
    // Updates
    val updateItems: List<MangaUpdateItem> = listOf(
        MangaUpdateItem(
            id = "u1",
            mangaId = 1L,
            title = "Neon Genesis: Requiem",
            chapterDisplay = "CH. 1160",
            timeAgo = "2 hours ago",
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
            isDownloaded = false
        ),
        MangaUpdateItem(
            id = "u2",
            mangaId = 2L,
            title = "Abyssal Blade",
            chapterDisplay = "CH. 42",
            timeAgo = "5 hours ago",
            coverUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80",
            isDownloaded = false
        ),
        MangaUpdateItem(
            id = "u3",
            mangaId = 3L,
            title = "Stellar Drift",
            chapterDisplay = "CH. 08",
            timeAgo = "Yesterday",
            coverUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80",
            isDownloaded = true
        )
    ),
    // Lumina AI Chat Messages
    val aiMessages: List<AiChatMessage> = listOf(
        AiChatMessage(
            id = "msg1",
            isFromUser = true,
            text = "Recommend something like Berserk"
        ),
        AiChatMessage(
            id = "msg2",
            isFromUser = false,
            text = "If you appreciate the dark, visceral tone and sweeping epic scale of Berserk, these cinematic masterpieces delve into similarly mature themes of survival, ambition, and warfare.",
            recommendations = listOf(
                AiRecommendationCard(
                    title = "Vagabond",
                    tag1 = "HISTORICAL",
                    tag2 = "ACTION",
                    description = "A profound, beautifully illustrated journey of a legendary swordsman exploring the meaning of strength and enlightenment.",
                    coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
                    matchScore = 98
                ),
                AiRecommendationCard(
                    title = "Vinland Saga",
                    tag1 = "EPIC",
                    tag2 = "ADVENTURE",
                    description = "A gripping tale of Vikings, revenge, and the arduous path to finding a land devoid of war and slavery.",
                    coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
                    matchScore = 96
                ),
                AiRecommendationCard(
                    title = "Kingdom",
                    tag1 = "MILITARY",
                    tag2 = "STRATEGY",
                    description = "Massive, strategic warfare during the Warring States period, following an orphan aiming to become the greatest general under the heavens.",
                    coverUrl = "https://images.unsplash.com/photo-1514539079130-25950c84af65?w=600&auto=format&fit=crop&q=80",
                    matchScore = 94
                )
            )
        )
    ),
    // Timeline Items
    val timelineItems: List<TimelineItem> = listOf(
        TimelineItem(
            id = "t1",
            arcBadge = "Arc 1",
            title = "The Beginning",
            description = "The seeds of conflict are sown as the ancient pact is broken. Ancient relics resonate across Neo-Tokyo.",
            imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            isMajorEvent = false,
            isChronological = true
        ),
        TimelineItem(
            id = "t2",
            arcBadge = "Chapter 1",
            title = "Awakening",
            description = "First steps into the wider world. Kaito discovers his ocular resonance capability.",
            imageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
            isMajorEvent = false,
            isChronological = true
        ),
        TimelineItem(
            id = "t3",
            arcBadge = "Major Event",
            title = "The Fall",
            description = "The betrayal that shattered the realm. Key characters are scattered, and the antagonist consolidates power over the central territories.",
            imageUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80",
            isMajorEvent = true,
            isChronological = true
        )
    ),
    // Backup & Sync State
    val syncAutoActive: Boolean = true,
    val syncDriveConnected: Boolean = true,
    val syncDropboxConnected: Boolean = false,
    val syncLibrary: Boolean = true,
    val syncProgress: Boolean = true,
    val syncCategories: Boolean = true,
    val syncSettings: Boolean = false,
    val lastBackupTime: String = "Today, 10:42 AM"
)

class LuminaMainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = LuminaRepository(application)

    private val _uiState = MutableStateFlow(LuminaUiState())
    val uiState: StateFlow<LuminaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAllMangas().collect { mangas ->
                _uiState.update { it.copy(allMangas = mangas) }
            }
        }

        viewModelScope.launch {
            repository.observeLibrary().collect { library ->
                _uiState.update { it.copy(libraryMangas = library) }
            }
        }

        viewModelScope.launch {
            repository.observeSnaps().collect { snaps ->
                _uiState.update { it.copy(activeSnaps = snaps) }
            }
        }

        viewModelScope.launch {
            repository.observeAchievements().collect { achs ->
                _uiState.update { it.copy(achievements = achs) }
            }
        }

        viewModelScope.launch {
            repository.streakDays.collect { streak ->
                _uiState.update { it.copy(streakDays = streak) }
            }
        }

        viewModelScope.launch {
            repository.readerConfig.collect { config ->
                _uiState.update { it.copy(readerConfig = config) }
            }
        }

        viewModelScope.launch {
            repository.installedExtensions.collect { installed ->
                _uiState.update { it.copy(installedExtensions = installed) }
            }
        }

        viewModelScope.launch {
            repository.availableExtensions.collect { exts ->
                _uiState.update { it.copy(availableExtensions = exts) }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
        when (screen) {
            is Screen.MangaDetail -> loadMangaDetails(screen.mangaId)
            is Screen.UniverseMap -> loadMangaDetails(screen.mangaId)
            is Screen.MangaDna -> loadMangaDetails(screen.mangaId)
            is Screen.Timeline -> loadMangaDetails(screen.mangaId)
            is Screen.Reader -> loadReader(screen.mangaId, screen.chapterId)
            else -> {}
        }
    }

    private fun loadMangaDetails(mangaId: Long) {
        viewModelScope.launch {
            repository.observeManga(mangaId).firstOrNull()?.let { manga ->
                _uiState.update { it.copy(selectedManga = manga) }
            }
            repository.observeChapters(mangaId).firstOrNull()?.let { chapters ->
                _uiState.update { it.copy(selectedChapters = chapters) }
            }
        }
    }

    private fun loadReader(mangaId: Long, chapterId: Long) {
        viewModelScope.launch {
            val manga = repository.observeManga(mangaId).firstOrNull()
            val chapter = repository.getChapter(chapterId)
            val chapters = repository.observeChapters(mangaId).firstOrNull() ?: emptyList()
            _uiState.update {
                it.copy(
                    selectedManga = manga,
                    currentChapter = chapter,
                    selectedChapters = chapters
                )
            }
        }
    }

    fun toggleFavorite(mangaId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(mangaId)
        }
    }

    fun toggleBookmark(chapterId: Long) {
        viewModelScope.launch {
            repository.toggleChapterBookmark(chapterId)
            _uiState.value.selectedManga?.let { loadMangaDetails(it.id) }
        }
    }

    fun downloadChapter(chapterId: Long) {
        viewModelScope.launch {
            repository.setChapterDownloaded(chapterId, true)
            _uiState.value.selectedManga?.let { loadMangaDetails(it.id) }
        }
    }

    fun downloadAllChapters(mangaId: Long) {
        viewModelScope.launch {
            val chapters = repository.observeChapters(mangaId).firstOrNull() ?: return@launch
            chapters.forEach { repository.setChapterDownloaded(it.id, true) }
            loadMangaDetails(mangaId)
        }
    }

    fun saveSnap(
        mangaId: Long,
        chapterId: Long,
        title: String,
        coverUrl: String,
        chapterNumber: Float,
        chapterName: String,
        pageIndex: Int,
        scrollOffset: Int,
        progressPercent: Float
    ) {
        viewModelScope.launch {
            repository.saveReadingSnap(
                ReadingSnap(
                    mangaId = mangaId,
                    chapterId = chapterId,
                    title = title,
                    coverUrl = coverUrl,
                    chapterNumber = chapterNumber,
                    chapterName = chapterName,
                    pageIndex = pageIndex,
                    scrollOffset = scrollOffset,
                    progressPercent = progressPercent
                )
            )
        }
    }

    fun deleteSnap(mangaId: Long) {
        viewModelScope.launch {
            repository.deleteSnap(mangaId)
        }
    }

    fun updateReaderConfig(config: ReaderConfig) {
        _uiState.update { it.copy(readerConfig = config) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setGenreFilter(genre: String) {
        _uiState.update { it.copy(selectedGenreFilter = genre) }
    }

    fun setShowSnapSwitcher(show: Boolean) {
        _uiState.update { it.copy(showSnapSwitcher = show) }
    }

    fun setRepoUrlInput(url: String) {
        _uiState.update { it.copy(repoUrlInput = url) }
    }

    fun fetchExtensions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRepoLoading = true, repoErrorMessage = null) }
            val result = repository.fetchRepoExtensions(_uiState.value.repoUrlInput)
            result.onSuccess { exts ->
                _uiState.update { it.copy(isRepoLoading = false, availableExtensions = exts) }
            }.onFailure { err ->
                _uiState.update { it.copy(isRepoLoading = false, repoErrorMessage = err.message ?: "Failed to parse repository index.pb") }
            }
        }
    }

    fun toggleExtension(pkg: ExtensionPackage) {
        viewModelScope.launch {
            repository.toggleExtensionInstall(pkg)
        }
    }

    // Lumina Mutation actions
    fun toggleMutationLock(id: String) {
        _uiState.update { state ->
            val updated = state.mutationBehaviors.map {
                if (it.id == id) it.copy(isLocked = !it.isLocked) else it
            }
            state.copy(mutationBehaviors = updated)
        }
    }

    fun resetMutation(id: String) {
        _uiState.update { state ->
            val updated = state.mutationBehaviors.map {
                if (it.id == id) it.copy(isLocked = false) else it
            }
            state.copy(mutationBehaviors = updated)
        }
    }

    // Backup & Sync actions
    fun toggleSyncItem(key: String, value: Boolean) {
        _uiState.update { state ->
            when (key) {
                "library" -> state.copy(syncLibrary = value)
                "progress" -> state.copy(syncProgress = value)
                "categories" -> state.copy(syncCategories = value)
                "settings" -> state.copy(syncSettings = value)
                else -> state
            }
        }
    }

    fun triggerSyncNow() {
        _uiState.update { it.copy(lastBackupTime = "Just now") }
    }

    // Updates actions
    fun downloadUpdate(id: String) {
        _uiState.update { state ->
            val updated = state.updateItems.map {
                if (it.id == id) it.copy(isDownloaded = true) else it
            }
            state.copy(updateItems = updated)
        }
    }

    fun downloadAllUpdates() {
        _uiState.update { state ->
            val updated = state.updateItems.map { it.copy(isDownloaded = true) }
            state.copy(updateItems = updated)
        }
    }

    // AI Assistant actions
    fun sendAiMessage(promptText: String) {
        if (promptText.isBlank()) return
        val userMsg = AiChatMessage(
            id = "user_${System.currentTimeMillis()}",
            isFromUser = true,
            text = promptText
        )
        val aiResponse = when {
            promptText.contains("Berserk", ignoreCase = true) -> AiChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                isFromUser = false,
                text = "If you appreciate the dark, visceral tone and sweeping epic scale of Berserk, these cinematic masterpieces delve into similarly mature themes of survival, ambition, and warfare.",
                recommendations = listOf(
                    AiRecommendationCard("Vagabond", "HISTORICAL", "ACTION", "A profound journey of a legendary swordsman exploring strength and enlightenment.", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80", 98),
                    AiRecommendationCard("Vinland Saga", "EPIC", "ADVENTURE", "A gripping tale of Vikings, revenge, and the arduous path to finding peace.", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80", 96),
                    AiRecommendationCard("Kingdom", "MILITARY", "STRATEGY", "Massive warfare during the Warring States period, following an orphan aiming to become the greatest general.", "https://images.unsplash.com/photo-1514539079130-25950c84af65?w=600&auto=format&fit=crop&q=80", 94)
                )
            )
            promptText.contains("Solo", ignoreCase = true) || promptText.contains("Level", ignoreCase = true) -> AiChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                isFromUser = false,
                text = "Here are high-octane hunter and progression powerhouses with god-tier art and adrenaline-fueled dungeon climbs.",
                recommendations = listOf(
                    AiRecommendationCard("Omniscient Reader", "SYSTEM", "ACTION", "The only reader who knows the epilogue of the ruined world.", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80", 99),
                    AiRecommendationCard("The Beginning After The End", "REINCARNATION", "MAGIC", "King Grey reborn into a new world filled with magic and monsters.", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80", 97)
                )
            )
            else -> AiChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                isFromUser = false,
                text = "Based on your reading DNA and history in Lumina Noir, here are tailored recommendations matching your pacing and thematic affinities.",
                recommendations = listOf(
                    AiRecommendationCard("Neon Genesis: Requiem", "CYBERPUNK", "ACTION", "The sprawling metropolis holds secrets deeper than the neon lights reach.", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80", 98),
                    AiRecommendationCard("Blade of the Phantom", "DARK FANTASY", "SUPERNATURAL", "Cursed blade wielder wandering the ethereal purgatory between realms.", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80", 95)
                )
            )
        }
        _uiState.update { it.copy(aiMessages = it.aiMessages + userMsg + aiResponse) }
    }

    // Forge actions
    fun activateExperiencePack(packId: String) {
        _uiState.update { state ->
            val updated = state.experiencePacks.map {
                it.copy(isActive = (it.id == packId))
            }
            state.copy(experiencePacks = updated)
        }
    }
}
