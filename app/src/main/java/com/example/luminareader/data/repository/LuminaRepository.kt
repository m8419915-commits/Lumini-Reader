package com.example.luminareader.data.repository

import com.example.luminareader.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LuminaRepository {

    private val _mangas = MutableStateFlow<List<Manga>>(getInitialMangas())
    val mangas: StateFlow<List<Manga>> = _mangas.asStateFlow()

    private val _chapters = MutableStateFlow<List<Chapter>>(getInitialChapters())
    val chapters: StateFlow<List<Chapter>> = _chapters.asStateFlow()

    private val _readingSnaps = MutableStateFlow<List<ReadingSnap>>(getInitialSnaps())
    val readingSnaps: StateFlow<List<ReadingSnap>> = _readingSnaps.asStateFlow()

    private val _historyItems = MutableStateFlow<List<HistoryItem>>(getInitialHistory())
    val historyItems: StateFlow<List<HistoryItem>> = _historyItems.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(getInitialCategories())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _updates = MutableStateFlow<List<MangaUpdateItem>>(getInitialUpdates())
    val updates: StateFlow<List<MangaUpdateItem>> = _updates.asStateFlow()

    private val _extensions = MutableStateFlow<List<ExtensionPackage>>(getInitialExtensions())
    val extensions: StateFlow<List<ExtensionPackage>> = _extensions.asStateFlow()

    private val _extensionStores = MutableStateFlow<List<ExtensionStore>>(getInitialStores())
    val extensionStores: StateFlow<List<ExtensionStore>> = _extensionStores.asStateFlow()

    private val _sources = MutableStateFlow<List<SourceMeta>>(getInitialSources())
    val sources: StateFlow<List<SourceMeta>> = _sources.asStateFlow()

    private val _migrationItems = MutableStateFlow<List<SourceMigrationItem>>(getInitialMigrations())
    val migrationItems: StateFlow<List<SourceMigrationItem>> = _migrationItems.asStateFlow()

    private val _universeNodes = MutableStateFlow<List<UniverseNode>>(getInitialUniverseNodes())
    val universeNodes: StateFlow<List<UniverseNode>> = _universeNodes.asStateFlow()

    private val _universeEdges = MutableStateFlow<List<UniverseEdge>>(getInitialUniverseEdges())
    val universeEdges: StateFlow<List<UniverseEdge>> = _universeEdges.asStateFlow()

    private val _dnaAttributes = MutableStateFlow<List<DnaAttribute>>(getInitialDna())
    val dnaAttributes: StateFlow<List<DnaAttribute>> = _dnaAttributes.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(getInitialAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _experiencePacks = MutableStateFlow<List<ExperiencePack>>(getInitialExperiencePacks())
    val experiencePacks: StateFlow<List<ExperiencePack>> = _experiencePacks.asStateFlow()

    private val _mutationBehaviors = MutableStateFlow<List<MutationBehavior>>(getInitialMutations())
    val mutationBehaviors: StateFlow<List<MutationBehavior>> = _mutationBehaviors.asStateFlow()

    private val _timelineItems = MutableStateFlow<List<TimelineItem>>(getInitialTimeline())
    val timelineItems: StateFlow<List<TimelineItem>> = _timelineItems.asStateFlow()

    private val _readerConfig = MutableStateFlow(ReaderConfig())
    val readerConfig: StateFlow<ReaderConfig> = _readerConfig.asStateFlow()

    private val _libraryFilters = MutableStateFlow(LibraryFilters())
    val libraryFilters: StateFlow<LibraryFilters> = _libraryFilters.asStateFlow()

    private val _aiMessages = MutableStateFlow<List<AiChatMessage>>(getInitialAiMessages())
    val aiMessages: StateFlow<List<AiChatMessage>> = _aiMessages.asStateFlow()

    fun toggleLibrary(mangaId: Int) {
        _mangas.update { list ->
            list.map { if (it.id == mangaId) it.copy(inLibrary = !it.inLibrary) else it }
        }
    }

    fun markChapterRead(chapterId: Int, isRead: Boolean) {
        _chapters.update { list ->
            list.map { if (it.id == chapterId) it.copy(isRead = isRead) else it }
        }
    }

    fun toggleChapterDownload(chapterId: Int) {
        _chapters.update { list ->
            list.map { if (it.id == chapterId) it.copy(isDownloaded = !it.isDownloaded) else it }
        }
    }

    fun downloadAllForManga(mangaId: Int) {
        _chapters.update { list ->
            list.map { if (it.mangaId == mangaId) it.copy(isDownloaded = true) else it }
        }
    }

    fun saveSnap(snap: ReadingSnap) {
        _readingSnaps.update { list ->
            val filtered = list.filter { it.mangaId != snap.mangaId }
            listOf(snap) + filtered
        }
    }

    fun deleteSnap(mangaId: Int) {
        _readingSnaps.update { list -> list.filter { it.mangaId != mangaId } }
    }

    fun addHistoryItem(item: HistoryItem) {
        _historyItems.update { list ->
            listOf(item) + list.filter { it.mangaId != item.mangaId }
        }
    }

    fun removeHistoryItem(id: String) {
        _historyItems.update { list -> list.filter { it.id != id } }
    }

    fun clearHistory() {
        _historyItems.value = emptyList()
    }

    fun updateReaderConfig(config: ReaderConfig) {
        _readerConfig.value = config
    }

    fun updateLibraryFilters(filters: LibraryFilters) {
        _libraryFilters.value = filters
    }

    fun toggleExtensionInstall(packageName: String) {
        _extensions.update { list ->
            list.map {
                if (it.packageName == packageName) it.copy(isInstalled = !it.isInstalled, hasUpdate = false) else it
            }
        }
    }

    fun toggleMutationLock(id: String) {
        _mutationBehaviors.update { list ->
            list.map { if (it.id == id) it.copy(isLocked = !it.isLocked) else it }
        }
    }

    fun setActivePack(id: String) {
        _experiencePacks.update { list ->
            list.map { it.copy(isActive = it.id == id) }
        }
    }

    fun addAiMessage(msg: AiChatMessage) {
        _aiMessages.update { it + msg }
    }

    companion object {
        fun getInitialMangas(): List<Manga> = listOf(
            Manga(
                id = 1,
                title = "Bleach: Thousand-Year Blood War",
                author = "Tite Kubo",
                artist = "Tite Kubo",
                description = "The peace is suddenly broken when warning sirens blare through Soul Society. Residents are disappearing without a trace and nobody knows who is behind it. Meanwhile, a dark shadow extends toward Karakura Town.",
                genre = listOf("Shonen", "Supernatural", "Action", "Dark Fantasy"),
                status = "Completed",
                category = "Reading",
                thumbnailUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.9f,
                source = "MangaDex",
                totalChapters = 686,
                latestChapter = "Ch. 686",
                unreadCount = 0,
                trackers = listOf(
                    MangaTracker("anilist", "AniList", 0xFF02A9FF, true, "30012", "BLEACH", TrackerStatus.COMPLETED, 9.5f, 686, 686),
                    MangaTracker("myanimelist", "MyAnimeList", 0xFF2E51A2, true, "12", "Bleach", TrackerStatus.COMPLETED, 9.0f, 686, 686)
                )
            ),
            Manga(
                id = 2,
                title = "Chainsaw Man: Part 2",
                author = "Tatsuki Fujimoto",
                artist = "Tatsuki Fujimoto",
                description = "Denji wanted a normal high school life, but when devils and the mysterious War Devil Asa Mitaka cross his path, the chainsaws rev again in an unpredictable battle of instincts.",
                genre = listOf("Dark Fantasy", "Horror", "Psychological", "Action"),
                status = "Ongoing",
                category = "Reading",
                thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.85f,
                source = "MangaDex",
                totalChapters = 180,
                latestChapter = "Ch. 180",
                unreadCount = 4
            ),
            Manga(
                id = 3,
                title = "Solo Leveling: Ragnarok",
                author = "Chugong, Daul",
                artist = "Redice Studio",
                description = "Sung Suho, son of the Shadow Monarch Sung Jinwoo, awakens in a world where new dimensional gates threaten humanity. As the monarch lineage surges through him, the shadow army rises once again.",
                genre = listOf("Action RPG", "Supernatural", "Dungeon", "Fantasy"),
                status = "Ongoing",
                category = "Reading",
                thumbnailUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.92f,
                source = "Asura Scans",
                totalChapters = 92,
                latestChapter = "Ch. 92",
                unreadCount = 2
            ),
            Manga(
                id = 4,
                title = "Frieren: Beyond Journey's End",
                author = "Kanehito Yamada",
                artist = "Tsukasa Abe",
                description = "The adventure is over but life goes on for an elf mage just beginning to learn what living is all about. The elf mage Frieren and her courageous fellow adventurers have defeated the Demon King and brought peace to the land.",
                genre = listOf("Adventure", "Drama", "Fantasy", "Slice of Life"),
                status = "Ongoing",
                category = "Plan to Read",
                thumbnailUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.95f,
                source = "MangaDex",
                totalChapters = 135,
                latestChapter = "Ch. 135",
                unreadCount = 12
            ),
            Manga(
                id = 5,
                title = "Jujutsu Kaisen",
                author = "Gege Akutami",
                artist = "Gege Akutami",
                description = "Yuji Itadori swallows a cursed talisman and becomes host to Ryomen Sukuna, the King of Curses. Enrolled at Tokyo Jujutsu High, he plunges into lethal battles between sorcerers and cursed spirits.",
                genre = listOf("Supernatural", "Battle", "Dark Fantasy", "Action"),
                status = "Completed",
                category = "Completed",
                thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80",
                inLibrary = false,
                rating = 4.88f,
                source = "MangaDex",
                totalChapters = 271,
                latestChapter = "Ch. 271",
                unreadCount = 0
            ),
            Manga(
                id = 6,
                title = "One Piece",
                author = "Eiichiro Oda",
                artist = "Eiichiro Oda",
                description = "Monkey D. Luffy refuses to let anyone or anything stand in the way of his quest to become king of all pirates. With a course charted for the treacherous waters of the Grand Line, he embarks with his crew on the greatest odyssey.",
                genre = listOf("Adventure", "Fantasy", "Action", "Epic"),
                status = "Ongoing",
                category = "Reading",
                thumbnailUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.96f,
                source = "Flame Comics",
                totalChapters = 1125,
                latestChapter = "Ch. 1125",
                unreadCount = 1
            ),
            Manga(
                id = 7,
                title = "Dandadan",
                author = "Yukinobu Tatsu",
                artist = "Yukinobu Tatsu",
                description = "Momo Ayase, who believes in ghosts, and Okarun, who believes in aliens, argue over which is real. When they visit paranormal hotspots to prove each other wrong, both supernatural worlds collide.",
                genre = listOf("Supernatural", "Sci-Fi", "Comedy", "Action"),
                status = "Ongoing",
                category = "Reading",
                thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.91f,
                source = "MangaDex",
                totalChapters = 168,
                latestChapter = "Ch. 168",
                unreadCount = 5
            ),
            Manga(
                id = 8,
                title = "Berserk",
                author = "Kentaro Miura",
                artist = "Kentaro Miura",
                description = "Guts, known as the Black Swordsman, seeks sanctuary from demonic forces attracted to him and his woman by a demonic brand, and vengeance against the man who branded him as an unholy sacrifice.",
                genre = listOf("Dark Fantasy", "Horror", "Tragedy", "Military"),
                status = "Ongoing",
                category = "Completed",
                thumbnailUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
                bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80",
                inLibrary = true,
                rating = 4.98f,
                source = "MangaDex",
                totalChapters = 376,
                latestChapter = "Ch. 376",
                unreadCount = 0
            )
        )

        fun getInitialChapters(): List<Chapter> {
            val samplePages = listOf(
                "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=900&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=900&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=900&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1563089145-599997674d42?w=900&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=900&auto=format&fit=crop&q=80"
            )

            val list = mutableListOf<Chapter>()
            for (mangaId in 1..8) {
                for (chNum in 1..10) {
                    val id = mangaId * 100 + chNum
                    list.add(
                        Chapter(
                            id = id,
                            mangaId = mangaId,
                            chapterNumber = chNum.toFloat(),
                            title = "Chapter $chNum: Awakening Power",
                            scanlator = if (mangaId % 2 == 0) "MangaDex Official" else "Flame Scans",
                            dateUpload = "2024-08-${10 + chNum}",
                            isRead = chNum < 5,
                            isDownloaded = chNum < 3,
                            pageCount = samplePages.size,
                            pages = samplePages
                        )
                    )
                }
            }
            return list
        }

        fun getInitialSnaps(): List<ReadingSnap> = listOf(
            ReadingSnap(
                mangaId = 1,
                title = "Bleach: TYBW",
                coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
                chapterId = 105,
                chapterNumber = 5f,
                pageIndex = 3,
                totalPages = 5,
                progressPercent = 60
            ),
            ReadingSnap(
                mangaId = 3,
                title = "Solo Leveling: Ragnarok",
                coverUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80",
                chapterId = 308,
                chapterNumber = 8f,
                pageIndex = 2,
                totalPages = 5,
                progressPercent = 40
            ),
            ReadingSnap(
                mangaId = 2,
                title = "Chainsaw Man: Part 2",
                coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
                chapterId = 203,
                chapterNumber = 3f,
                pageIndex = 4,
                totalPages = 5,
                progressPercent = 80
            )
        )

        fun getInitialHistory(): List<HistoryItem> = listOf(
            HistoryItem(
                id = "h1",
                mangaId = 1,
                mangaTitle = "Bleach: Thousand-Year Blood War",
                coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
                chapterId = 105,
                chapterNumber = 5f,
                chapterTitle = "Chapter 5: The Dark Sun",
                pageIndex = 3,
                totalPages = 5,
                progressPercent = 60,
                dateGroup = "Today",
                timeString = "15 mins ago"
            ),
            HistoryItem(
                id = "h2",
                mangaId = 3,
                mangaTitle = "Solo Leveling: Ragnarok",
                coverUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80",
                chapterId = 308,
                chapterNumber = 8f,
                chapterTitle = "Chapter 8: Sovereign Call",
                pageIndex = 2,
                totalPages = 5,
                progressPercent = 40,
                dateGroup = "Today",
                timeString = "2 hours ago"
            ),
            HistoryItem(
                id = "h3",
                mangaId = 4,
                mangaTitle = "Frieren: Beyond Journey's End",
                coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80",
                chapterId = 404,
                chapterNumber = 4f,
                chapterTitle = "Chapter 4: Northern Lands",
                pageIndex = 5,
                totalPages = 5,
                progressPercent = 100,
                dateGroup = "Yesterday",
                timeString = "Yesterday 9:15 PM"
            )
        )

        fun getInitialCategories(): List<Category> = listOf(
            Category("cat_all", "All", 0, 7, isDefault = true),
            Category("cat_reading", "Reading", 1, 5),
            Category("cat_plan", "Plan to Read", 2, 1),
            Category("cat_completed", "Completed", 3, 2)
        )

        fun getInitialUpdates(): List<MangaUpdateItem> = listOf(
            MangaUpdateItem("u1", 2, "Chainsaw Man: Part 2", "Ch. 180: Blood Rain", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80", "12m ago"),
            MangaUpdateItem("u2", 3, "Solo Leveling: Ragnarok", "Ch. 92: Shadow Descent", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80", "1h ago"),
            MangaUpdateItem("u3", 6, "One Piece", "Ch. 1125: Egghead Resolution", "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80", "4h ago"),
            MangaUpdateItem("u4", 7, "Dandadan", "Ch. 168: Void Tunnel", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80", "1d ago")
        )

        fun getInitialExtensions(): List<ExtensionPackage> = listOf(
            ExtensionPackage(
                name = "MangaDex",
                packageName = "eu.kanade.tachiyomi.extension.all.mangadex",
                versionName = "1.4.218",
                versionCode = 218,
                lang = "all",
                apk = "mangadex-all-v1.4.218.apk",
                icon = "https://mangadex.org/favicon.ico",
                repoId = "keiyoushi_main",
                repoName = "Keiyoushi Official Extensions",
                isInstalled = true,
                hasUpdate = false
            ),
            ExtensionPackage(
                name = "Asura Scans",
                packageName = "eu.kanade.tachiyomi.extension.en.asurascans",
                versionName = "1.4.52",
                versionCode = 52,
                lang = "en",
                apk = "asurascans-en-v1.4.52.apk",
                icon = "https://asuracomic.net/favicon.ico",
                repoId = "keiyoushi_main",
                repoName = "Keiyoushi Official Extensions",
                isInstalled = true,
                hasUpdate = true
            ),
            ExtensionPackage(
                name = "Flame Comics",
                packageName = "eu.kanade.tachiyomi.extension.en.flamecomics",
                versionName = "1.3.12",
                versionCode = 12,
                lang = "en",
                apk = "flamecomics-en-v1.3.12.apk",
                icon = "https://flamecomics.xyz/favicon.ico",
                repoId = "keiyoushi_main",
                repoName = "Keiyoushi Official Extensions",
                isInstalled = false,
                hasUpdate = false
            ),
            ExtensionPackage(
                name = "Webtoons",
                packageName = "eu.kanade.tachiyomi.extension.en.webtoons",
                versionName = "1.3.4",
                versionCode = 4,
                lang = "en",
                apk = "webtoons-en-v1.3.4.apk",
                icon = "https://www.webtoons.com/favicon.ico",
                repoId = "keiyoushi_main",
                repoName = "Keiyoushi Official Extensions",
                isInstalled = true,
                hasUpdate = false
            ),
            ExtensionPackage(
                name = "Rawkuma",
                packageName = "eu.kanade.tachiyomi.extension.ja.rawkuma",
                versionName = "1.2.1",
                versionCode = 1,
                lang = "ja",
                apk = "rawkuma-ja-v1.2.1.apk",
                icon = "https://rawkuma.com/favicon.ico",
                repoId = "keiyoushi_main",
                repoName = "Keiyoushi Official Extensions",
                isInstalled = false,
                hasUpdate = false
            )
        )

        fun getInitialStores(): List<ExtensionStore> = listOf(
            ExtensionStore("keiyoushi_main", "Keiyoushi Official Extensions", "https://keiyoushi.github.io/extensions/", isOfficial = true, isPinned = true, enabled = true, lastSynced = "Just now", totalExtensions = 1240),
            ExtensionStore("komikku_community", "Komikku Community Index", "https://komikku.app", isOfficial = false, isPinned = false, enabled = true, lastSynced = "12 mins ago", totalExtensions = 180)
        )

        fun getInitialSources(): List<SourceMeta> = listOf(
            SourceMeta("mangadex", "MangaDex", "all", "https://mangadex.org", isPinned = true, icon = "https://mangadex.org/favicon.ico", extensionPkg = "eu.kanade.tachiyomi.extension.all.mangadex", itemCount = 84200),
            SourceMeta("asurascans", "Asura Scans", "en", "https://asuracomic.net", isPinned = true, icon = "https://asuracomic.net/favicon.ico", extensionPkg = "eu.kanade.tachiyomi.extension.en.asurascans", itemCount = 1420),
            SourceMeta("flamecomics", "Flame Comics", "en", "https://flamecomics.xyz", isPinned = false, icon = "https://flamecomics.xyz/favicon.ico", extensionPkg = "eu.kanade.tachiyomi.extension.en.flamecomics", itemCount = 890),
            SourceMeta("webtoons", "Webtoons", "en", "https://www.webtoons.com", isPinned = true, icon = "https://www.webtoons.com/favicon.ico", extensionPkg = "eu.kanade.tachiyomi.extension.en.webtoons", itemCount = 4500)
        )

        fun getInitialMigrations(): List<SourceMigrationItem> = listOf(
            SourceMigrationItem("mig_1", 1, "Bleach: Thousand-Year Blood War", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80", "MangaDex (Multi)", "Asura Scans (EN)", "Bleach: TYBW Official", 686, "ready", 99),
            SourceMigrationItem("mig_2", 3, "Solo Leveling: Ragnarok", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80", "Asura Scans (EN)", "Flame Comics (EN)", "Solo Leveling: Ragnarok (HQ)", 92, "ready", 97)
        )

        fun getInitialUniverseNodes(): List<UniverseNode> = listOf(
            UniverseNode("n1", "Ichigo Kurosaki", "Substitute Shinigami", "Beyond Captain Level", "Karakura Town / Soul Society", "Hybrid heritage combining Soul Reaper, Hollow, and Quincy ancestry. True Bankai wielded in final clash.", 0xFF00E5FF, 0.5f, 0.4f),
            UniverseNode("n2", "Rukia Kuchiki", "Captain (13th Division)", "Captain Level", "Soul Society (Gotei 13)", "Noble of Kuchiki clan. Master of Sode no Shirayuki and absolute zero Hakuren.", 0xFFA78BFA, 0.25f, 0.25f),
            UniverseNode("n3", "Yhwach", "Father of the Quincy", "God / Soul King Level", "Wandenreich", "Progenitor of Quincy lineage. Possesses The Almighty, seeing and altering future timelines.", 0xFFF43F5E, 0.75f, 0.25f),
            UniverseNode("n4", "Sosuke Aizen", "Former Captain (5th Division)", "Transcendent Trans-Hollow", "Mukken Prison / Rogue", "Architect of the Hogyoku evolution. His spiritual pressure remains bound in the deepest abyss.", 0xFF8B5CF6, 0.5f, 0.75f),
            UniverseNode("n5", "Kisuke Urahara", "Shopkeeper / Scientist", "Senior Captain Level", "Exiled Shinigami", "Genius strategist and inventor of the original Hogyoku. Master of prep-time combat.", 0xFF10B981, 0.2f, 0.65f)
        )

        fun getInitialUniverseEdges(): List<UniverseEdge> = listOf(
            UniverseEdge("n1", "n2", "Bond of Destiny / Soul Awakening", 0xFF00E5FF),
            UniverseEdge("n1", "n3", "Lineage Nemesis / Quincy Progenitor", 0xFFF43F5E),
            UniverseEdge("n1", "n4", "Former Arch-Enemy / Reluctant Ally", 0xFF8B5CF6),
            UniverseEdge("n1", "n5", "Mentor & Equipment Creator", 0xFF10B981),
            UniverseEdge("n4", "n5", "Intellectual Rivalry", 0xFFF59E0B)
        )

        fun getInitialDna(): List<DnaAttribute> = listOf(
            DnaAttribute("Action Intensity", 96, "High preference for complex kinetic battle choreographies.", 0xFF00E5FF),
            DnaAttribute("Dark Narrative Tone", 88, "Drawn to morally complex worlds with high psychological stakes.", 0xFF8B5CF6),
            DnaAttribute("Lore Depth", 92, "Invests heavily into expansive multi-arc worldbuilding.", 0xFF10B981),
            DnaAttribute("Pacing Velocity", 84, "Prefers high-momentum narrative arcs with rapid progression.", 0xFFF59E0B),
            DnaAttribute("Artistic Density", 94, "Values double-spread composition and atmospheric ink work.", 0xFFF43F5E)
        )

        fun getInitialAchievements(): List<Achievement> = listOf(
            Achievement("a1", "Shadow Sovereign", "Read 500+ chapters of Action RPG manga", 500, true),
            Achievement("a2", "Bankai Master", "Complete a 600+ chapter legendary epic", 1000, true),
            Achievement("a3", "Night Owl Immersion", "Read continuously for over 3 hours past midnight", 350, true),
            Achievement("a4", "Speed of Light", "Maintain a reading velocity above 4.0 PPM for 10 chapters", 400, false),
            Achievement("a5", "Lore Cartographer", "Inspect all universe connection nodes in Universe Map", 250, true)
        )

        fun getInitialExperiencePacks(): List<ExperiencePack> = listOf(
            ExperiencePack("p1", "Battle Shonen Kinetic", "Action", isActive = true, readingDirection = "Vertical Webtoon", hapticIntensity = 85, backgroundTheme = "AMOLED Pitch Black", audioProfile = "Ambient Synth Resonance", description = "High-impact haptic feedback on explosive spreads with continuous seamless scrolling."),
            ExperiencePack("p2", "Atmospheric Seinen", "Dark Fantasy", isActive = false, readingDirection = "Right-to-Left Manga", hapticIntensity = 40, backgroundTheme = "Charcoal Void", audioProfile = "Subtle Low Drone", description = "Deep contrast styling with page turn animations tuned for high ink density."),
            ExperiencePack("p3", "Webtoon Color Stream", "Webtoon", isActive = false, readingDirection = "Continuous Webtoon", hapticIntensity = 60, backgroundTheme = "Deep Slate", audioProfile = "Dynamic Flow", description = "Lumina Flow adaptive color bleeding from edge panels.")
        )

        fun getInitialMutations(): List<MutationBehavior> = listOf(
            MutationBehavior("m1", "Kinetic Inertia Scrolling", "Navigation", "Smooth physics-based inertia tuned for vertical infinite manhwa reading.", isLocked = false),
            MutationBehavior("m2", "Dynamic Ambient Edge Bleed", "Display", "Extracts dominant panel colors and projects ambient neon glow onto AMOLED bezels.", isLocked = false),
            MutationBehavior("m3", "1-Second Snap Memory", "Engine", "Persists exact viewport coordinate and chapter progress instantly to local state.", isLocked = false),
            MutationBehavior("m4", "Double Tap Smart Zoom", "Gesture", "Intelligently frames speech bubbles and combat panels with instant smooth centering.", isLocked = true)
        )

        fun getInitialTimeline(): List<TimelineItem> = listOf(
            TimelineItem("t1", "The Soul Society Infiltration", "Soul Society Arc", "Ichigo, Chad, Uryu, and Orihime enter the Seireitei to rescue Rukia Kuchiki from execution.", true, "Ch. 71 - 181"),
            TimelineItem("t2", "The Hueco Mundo Decisive War", "Arrancar Saga", "Espada ranks deployed; Ichigo awakens Vasto Lorde Hollow metamorphosis atop Las Noches.", true, "Ch. 240 - 350"),
            TimelineItem("t3", "Decisive Karakura Clash", "Fake Karakura Arc", "Final battle against Sosuke Aizen using the Final Getsuga Tensho (Mugetsu).", true, "Ch. 390 - 423"),
            TimelineItem("t4", "Thousand-Year Blood War", "TYBW Arc", "The Quincy Wandenreich emerges from shadows to invade the Soul King Palace.", true, "Ch. 480 - 686")
        )

        fun getInitialAiMessages(): List<AiChatMessage> = listOf(
            AiChatMessage(
                id = "m1",
                text = "Welcome to Lumina AI. I am your intelligent manga co-pilot. I can provide chapter recaps, power system breakdowns, timeline explanations, and personalized recommendations.",
                isFromUser = false,
                recommendations = listOf(
                    AiRecommendationCard("rec_1", "Chainsaw Man", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80", "Dark Fantasy", "Action", "Intense dark urban fantasy with supernatural devil pacts and rapid pacing.", 98),
                    AiRecommendationCard("rec_2", "Solo Leveling", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80", "Action RPG", "Dungeon", "Shadow Monarch progression with jaw-dropping vertical color battle spreads.", 95)
                )
            )
        )
    }
}
