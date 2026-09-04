package com.example.luminareader.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminareader.data.model.*
import com.example.luminareader.data.repository.LuminaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LuminaViewModel(
    private val repository: LuminaRepository = LuminaRepository()
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState(ScreenType.HOME))
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val backStack = mutableListOf<ScreenState>()

    private val _isSnapSwitcherOpen = MutableStateFlow(false)
    val isSnapSwitcherOpen: StateFlow<Boolean> = _isSnapSwitcherOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeToast = MutableStateFlow<String?>(null)
    val activeToast: StateFlow<String?> = _activeToast.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Data streams from repository
    val mangas: StateFlow<List<Manga>> = repository.mangas
    val chapters: StateFlow<List<Chapter>> = repository.chapters
    val readingSnaps: StateFlow<List<ReadingSnap>> = repository.readingSnaps
    val historyItems: StateFlow<List<HistoryItem>> = repository.historyItems
    val categories: StateFlow<List<Category>> = repository.categories
    val updates: StateFlow<List<MangaUpdateItem>> = repository.updates
    val extensions: StateFlow<List<ExtensionPackage>> = repository.extensions
    val extensionStores: StateFlow<List<ExtensionStore>> = repository.extensionStores
    val sources: StateFlow<List<SourceMeta>> = repository.sources
    val migrationItems: StateFlow<List<SourceMigrationItem>> = repository.migrationItems
    val universeNodes: StateFlow<List<UniverseNode>> = repository.universeNodes
    val universeEdges: StateFlow<List<UniverseEdge>> = repository.universeEdges
    val dnaAttributes: StateFlow<List<DnaAttribute>> = repository.dnaAttributes
    val achievements: StateFlow<List<Achievement>> = repository.achievements
    val experiencePacks: StateFlow<List<ExperiencePack>> = repository.experiencePacks
    val mutationBehaviors: StateFlow<List<MutationBehavior>> = repository.mutationBehaviors
    val timelineItems: StateFlow<List<TimelineItem>> = repository.timelineItems
    val readerConfig: StateFlow<ReaderConfig> = repository.readerConfig
    val libraryFilters: StateFlow<LibraryFilters> = repository.libraryFilters
    val aiMessages: StateFlow<List<AiChatMessage>> = repository.aiMessages

    val readingSpeedPpm = 3.2f
    val readingStreakDays = 5
    val totalReadingMinutes = 480

    fun navigate(type: ScreenType, mangaId: Int? = null, chapterId: Int? = null, initialPage: Int = 0, sourceId: String? = null) {
        val current = _screenState.value
        backStack.add(current)
        _screenState.value = ScreenState(type, mangaId, chapterId, initialPage, sourceId)
    }

    fun goBack(): Boolean {
        if (backStack.isNotEmpty()) {
            val previous = backStack.removeAt(backStack.size - 1)
            _screenState.value = previous
            return true
        }
        return false
    }

    val canGoBack: Boolean
        get() = backStack.isNotEmpty()

    fun setSnapSwitcherOpen(open: Boolean) {
        _isSnapSwitcherOpen.value = open
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _activeToast.value = message
            delay(2500)
            if (_activeToast.value == message) {
                _activeToast.value = null
            }
        }
    }

    fun toggleLibrary(mangaId: Int) {
        repository.toggleLibrary(mangaId)
        val manga = mangas.value.find { it.id == mangaId }
        val nowInLib = !(manga?.inLibrary ?: false)
        showToast(if (nowInLib) "Added to Library" else "Removed from Library")
    }

    fun markChapterRead(chapterId: Int, isRead: Boolean) {
        repository.markChapterRead(chapterId, isRead)
        showToast(if (isRead) "Marked as read" else "Marked as unread")
    }

    fun toggleChapterDownload(chapterId: Int) {
        repository.toggleChapterDownload(chapterId)
        showToast("Download status updated")
    }

    fun downloadAllForManga(mangaId: Int) {
        repository.downloadAllForManga(mangaId)
        showToast("Queued all chapters for download")
    }

    fun saveSnap(snap: ReadingSnap) {
        repository.saveSnap(snap)
        showToast("Lumina Snap saved (1-sec restore)")
    }

    fun restoreSnap(snap: ReadingSnap) {
        _isSnapSwitcherOpen.value = false
        navigate(ScreenType.READER, snap.mangaId, snap.chapterId, snap.pageIndex)
    }

    fun deleteSnap(mangaId: Int) {
        repository.deleteSnap(mangaId)
        showToast("Snap removed")
    }

    fun addHistoryItem(item: HistoryItem) {
        repository.addHistoryItem(item)
    }

    fun removeHistoryItem(id: String) {
        repository.removeHistoryItem(id)
        showToast("History entry removed")
    }

    fun clearHistory() {
        repository.clearHistory()
        showToast("Reading history cleared")
    }

    fun updateReaderConfig(config: ReaderConfig) {
        repository.updateReaderConfig(config)
    }

    fun updateLibraryFilters(filters: LibraryFilters) {
        repository.updateLibraryFilters(filters)
    }

    fun toggleExtensionInstall(packageName: String) {
        repository.toggleExtensionInstall(packageName)
        val ext = extensions.value.find { it.packageName == packageName }
        val nowInstalled = !(ext?.isInstalled ?: false)
        showToast(if (nowInstalled) "Extension installed" else "Extension uninstalled")
    }

    fun toggleMutationLock(id: String) {
        repository.toggleMutationLock(id)
    }

    fun setActivePack(id: String) {
        repository.setActivePack(id)
        showToast("Experience Pack activated")
    }

    fun sendAiMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = AiChatMessage(
            id = "user_${System.currentTimeMillis()}",
            text = text,
            isFromUser = true
        )
        repository.addAiMessage(userMsg)

        viewModelScope.launch {
            _isAiLoading.value = true
            delay(1000)

            val lower = text.lowercase()
            val replyText: String
            val recs = mutableListOf<AiRecommendationCard>()

            if (lower.contains("chapter") || lower.contains("summary") || lower.contains("recap")) {
                replyText = "Chapter Breakdown: The ongoing narrative pushes boundaries with intricate power balance shifts, ancestral revelations, and monumental clash sequences."
            } else if (lower.contains("character") || lower.contains("ichigo") || lower.contains("power")) {
                replyText = "Character Lore Analysis: The subject possesses hybrid lineage resonance across multiple dimensions. In the Lumina Universe Map, their spiritual pressure connects directly to core faction nodes."
            } else if (lower.contains("recommend") || lower.contains("find") || lower.contains("similar")) {
                replyText = "Curated Recommendations: Based on your dark supernatural and high-velocity reading DNA, these titles match your profile:"
                recs.add(AiRecommendationCard("rec_csm", "Chainsaw Man: Part 2", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80", "Dark Fantasy", "Action", "Visceral urban devil combat with rapid pacing and surreal depth.", 98))
                recs.add(AiRecommendationCard("rec_sl", "Solo Leveling: Ragnarok", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80", "Action RPG", "Supernatural", "Vertical scroll full color webtoon with high momentum dungeon progression.", 95))
            } else {
                replyText = "Lumina AI Copilot: Analyzed your request. Your reading velocity is averaging 3.2 PPM with a 5-day active flame streak. Let me know if you need timeline breakdowns, chapter lore, or source migrations."
            }

            val aiMsg = AiChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                text = replyText,
                isFromUser = false,
                recommendations = recs
            )
            repository.addAiMessage(aiMsg)
            _isAiLoading.value = false
        }
    }
}
