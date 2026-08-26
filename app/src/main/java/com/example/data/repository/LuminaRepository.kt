package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.core.extension.KeiyoushiRepoParser
import com.example.data.local.*
import com.example.domain.model.*
import com.example.ui.theme.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LuminaRepository(
    private val context: Context,
    private val dao: LuminaDao = LuminaDatabase.getDatabase(context).luminaDao(),
    private val repoParser: KeiyoushiRepoParser = KeiyoushiRepoParser()
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _readerConfig = MutableStateFlow(ReaderConfig())
    val readerConfig: StateFlow<ReaderConfig> = _readerConfig.asStateFlow()

    private val _streakDays = MutableStateFlow(7)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _installedExtensions = MutableStateFlow<Set<String>>(setOf("com.lumina.extension.mangadex", "com.lumina.extension.direct"))
    val installedExtensions: StateFlow<Set<String>> = _installedExtensions.asStateFlow()

    private val _availableExtensions = MutableStateFlow<List<ExtensionPackage>>(emptyList())
    val availableExtensions: StateFlow<List<ExtensionPackage>> = _availableExtensions.asStateFlow()

    init {
        scope.launch {
            seedInitialDatabase()
            loadDefaultExtensions()
        }
    }

    fun updateReaderConfig(config: ReaderConfig) {
        _readerConfig.value = config
    }

    fun observeAllMangas(): Flow<List<Manga>> {
        return dao.observeAllMangas().map { entities ->
            entities.map { it.toDomain(getMangaDna(it.id), getUniverseNodes(it.id), getUniverseEdges(it.id)) }
        }
    }

    fun observeLibrary(): Flow<List<Manga>> {
        return dao.observeLibrary().map { entities ->
            entities.map { it.toDomain(getMangaDna(it.id), getUniverseNodes(it.id), getUniverseEdges(it.id)) }
        }
    }

    fun observeManga(mangaId: Long): Flow<Manga?> {
        return dao.observeMangaById(mangaId).map { entity ->
            entity?.toDomain(getMangaDna(entity.id), getUniverseNodes(entity.id), getUniverseEdges(entity.id))
        }
    }

    fun observeChapters(mangaId: Long): Flow<List<Chapter>> {
        return dao.observeChapters(mangaId).map { entities ->
            entities.map { entity ->
                Chapter(
                    id = entity.id,
                    mangaId = entity.mangaId,
                    url = entity.url,
                    name = entity.name,
                    scanlator = entity.scanlator ?: "Lumina Official",
                    dateUpload = entity.dateUpload,
                    chapterNumber = entity.chapterNumber,
                    sourceOrder = entity.sourceOrder,
                    read = entity.read,
                    lastPageRead = entity.lastPageRead,
                    totalPages = entity.totalPages,
                    bookmark = entity.bookmark,
                    pageUrls = generateSamplePages(entity.mangaId, entity.chapterNumber.toInt(), entity.totalPages),
                    isDownloaded = entity.isDownloaded
                )
            }
        }
    }

    suspend fun getChapter(chapterId: Long): Chapter? = withContext(Dispatchers.IO) {
        val entity = dao.getChapterById(chapterId) ?: return@withContext null
        Chapter(
            id = entity.id,
            mangaId = entity.mangaId,
            url = entity.url,
            name = entity.name,
            scanlator = entity.scanlator ?: "Lumina Official",
            dateUpload = entity.dateUpload,
            chapterNumber = entity.chapterNumber,
            sourceOrder = entity.sourceOrder,
            read = entity.read,
            lastPageRead = entity.lastPageRead,
            totalPages = entity.totalPages,
            bookmark = entity.bookmark,
            pageUrls = generateSamplePages(entity.mangaId, entity.chapterNumber.toInt(), entity.totalPages),
            isDownloaded = entity.isDownloaded
        )
    }

    fun observeSnaps(): Flow<List<ReadingSnap>> {
        return dao.observeSnaps().map { list ->
            list.map {
                ReadingSnap(
                    mangaId = it.mangaId,
                    chapterId = it.chapterId,
                    title = it.title,
                    coverUrl = it.coverUrl ?: "",
                    chapterNumber = it.chapterNumber,
                    chapterName = it.chapterName,
                    pageIndex = it.pageIndex,
                    scrollOffset = it.scrollOffset,
                    progressPercent = it.progressPercent,
                    timestamp = it.timestamp
                )
            }
        }
    }

    fun observeAchievements(): Flow<List<Achievement>> {
        return dao.observeAchievements().map { list ->
            list.map {
                Achievement(
                    id = it.id,
                    title = it.title,
                    desc = it.desc,
                    unlocked = it.unlocked,
                    xp = it.xp,
                    category = it.category,
                    progress = it.progress
                )
            }
        }
    }

    suspend fun toggleFavorite(mangaId: Long) = withContext(Dispatchers.IO) {
        val manga = dao.getMangaById(mangaId) ?: return@withContext
        dao.setFavorite(mangaId, !manga.favorite)
    }

    suspend fun saveReadingSnap(snap: ReadingSnap) = withContext(Dispatchers.IO) {
        dao.saveSnap(
            ReadingSnapEntity(
                mangaId = snap.mangaId,
                chapterId = snap.chapterId,
                title = snap.title,
                coverUrl = snap.coverUrl,
                chapterNumber = snap.chapterNumber,
                chapterName = snap.chapterName,
                pageIndex = snap.pageIndex,
                scrollOffset = snap.scrollOffset,
                progressPercent = snap.progressPercent,
                timestamp = System.currentTimeMillis()
            )
        )
        // Unlock speed reader or session achievement
        dao.unlockAchievement("first_snap")
    }

    suspend fun deleteSnap(mangaId: Long) = withContext(Dispatchers.IO) {
        dao.deleteSnap(mangaId)
    }

    suspend fun updateChapterProgress(chapterId: Long, read: Boolean, lastPage: Int) = withContext(Dispatchers.IO) {
        dao.updateChapterReadProgress(chapterId, read, lastPage)
        if (read) {
            dao.unlockAchievement("chapter_master")
        }
    }

    suspend fun toggleChapterBookmark(chapterId: Long) = withContext(Dispatchers.IO) {
        val chapter = dao.getChapterById(chapterId) ?: return@withContext
        dao.setChapterBookmark(chapterId, !chapter.bookmark)
    }

    suspend fun setChapterDownloaded(chapterId: Long, downloaded: Boolean) = withContext(Dispatchers.IO) {
        dao.setChapterDownloaded(chapterId, downloaded)
        if (downloaded) {
            dao.unlockAchievement("offline_vault")
        }
    }

    suspend fun fetchRepoExtensions(repoUrl: String): Result<List<ExtensionPackage>> = withContext(Dispatchers.IO) {
        val result = repoParser.fetchRepoIndex(repoUrl)
        if (result.isSuccess) {
            val list = result.getOrDefault(emptyList()).map { pkg ->
                pkg.copy(isInstalled = _installedExtensions.value.contains(pkg.packageName))
            }
            _availableExtensions.value = list
        }
        result
    }

    fun toggleExtensionInstall(pkg: ExtensionPackage) {
        val current = _installedExtensions.value.toMutableSet()
        if (current.contains(pkg.packageName)) {
            current.remove(pkg.packageName)
        } else {
            current.add(pkg.packageName)
        }
        _installedExtensions.value = current
        _availableExtensions.value = _availableExtensions.value.map {
            if (it.packageName == pkg.packageName) it.copy(isInstalled = current.contains(it.packageName)) else it
        }
    }

    private suspend fun seedInitialDatabase() = withContext(Dispatchers.IO) {
        val count = dao.getMangaById(1)
        if (count != null) return@withContext

        val manga1 = MangaEntity(
            id = 1,
            sourceId = 100,
            sourceName = "Keiyoushi Webtoon",
            url = "/manga/solo-astral",
            title = "Solo Astral Leveling",
            author = "Chugong • DUBU",
            artist = "Redice Studio",
            description = "In a world where dimensional gates connect our reality with dungeon realms, Sung Jin-Woo is an E-Rank hunter known as the Weakest Hunter of All Mankind. After surviving a perilous double dungeon trial, he awakens the mysterious 'Lumina System' granting him the unique ability to infinitely level up and command a cosmic shadow legion.",
            genres = "Action, Fantasy, System, Dark Fantasy, Supernatural",
            status = 1,
            thumbnailUrl = "android.resource://${context.packageName}/${R.drawable.cover_solo_astral_1787734014926}",
            favorite = true,
            rating = 4.98f
        )

        val manga2 = MangaEntity(
            id = 2,
            sourceId = 101,
            sourceName = "Keiyoushi Manga",
            url = "/manga/cyberpunk-phantom",
            title = "Cyberpunk: Phantom Edge",
            author = "Hiroshi Takahashi",
            artist = "Studio Neon",
            description = "Neo-Shinjuku 2099. In a sprawling megalopolis ruled by mega-corporations and black-market cybernetics, Ren, an elite shadow operative with prototype optical camo and high-frequency violet katana blades, uncovers a conspiracy to digitize human souls.",
            genres = "Cyberpunk, Sci-Fi, Action, Thriller, Seinen",
            status = 1,
            thumbnailUrl = "android.resource://${context.packageName}/${R.drawable.cover_cyber_phantom_1787734029636}",
            favorite = true,
            rating = 4.92f
        )

        val manga3 = MangaEntity(
            id = 3,
            sourceId = 100,
            sourceName = "Keiyoushi Webtoon",
            url = "/manga/shadow-alchemist",
            title = "The Shadow Alchemist",
            author = "Elena Vance",
            artist = "Grimoire Works",
            description = "Cast out from the Imperial Arcane Academy for studying forbidden dark transmutation, Valen discovers an ancient grimoire of the Forgotten Celestials. With amber runes and shadow alchemy, he builds an arcane empire from the catacombs.",
            genres = "Dark Fantasy, Alchemy, Mystery, Magic, Drama",
            status = 1,
            thumbnailUrl = "android.resource://${context.packageName}/${R.drawable.cover_shadow_alchem_1787734044487}",
            favorite = false,
            rating = 4.87f
        )

        val manga4 = MangaEntity(
            id = 4,
            sourceId = 102,
            sourceName = "Lumina Direct",
            url = "/manga/neon-odyssey",
            title = "Neon Genesis Odyssey",
            author = "Kenji Sato",
            artist = "Starlight Visuals",
            description = "At the edge of the known cosmos, an exploration crew encounters a monolithic sentient Dyson sphere. As spatial anomalies distort time and gravity, they must decipher the cosmic language of an extinct civilization before the stellar core collapses.",
            genres = "Space Opera, Sci-Fi, Mystery, Adventure, Psychological",
            status = 2,
            thumbnailUrl = "android.resource://${context.packageName}/${R.drawable.cover_neon_odyssey_1787734059096}",
            favorite = true,
            rating = 4.95f
        )

        dao.insertMangas(listOf(manga1, manga2, manga3, manga4))

        // Seed Chapters
        val chapters = mutableListOf<ChapterEntity>()
        listOf(manga1, manga2, manga3, manga4).forEach { manga ->
            for (chNum in 1..8) {
                chapters.add(
                    ChapterEntity(
                        id = manga.id * 100 + chNum,
                        mangaId = manga.id,
                        url = "${manga.url}/chapter-$chNum",
                        name = when (chNum) {
                            1 -> "Chapter 1: The Awakening of Shadows"
                            2 -> "Chapter 2: The Double Dungeon Trial"
                            3 -> "Chapter 3: Blue Light & The Daily Quest"
                            4 -> "Chapter 4: Instant Dungeon Sovereign"
                            5 -> "Chapter 5: The Gate of Kasaka"
                            6 -> "Chapter 6: Monarch's Authority"
                            7 -> "Chapter 7: Shadow Extraction Protocol"
                            else -> "Chapter $chNum: Domain of the Monarch"
                        },
                        scanlator = "Asura / Lumina Scans",
                        dateUpload = System.currentTimeMillis() - (8 - chNum) * 86400000L,
                        chapterNumber = chNum.toFloat(),
                        sourceOrder = chNum,
                        read = chNum <= 2,
                        lastPageRead = if (chNum == 2) 4 else 0,
                        totalPages = 12,
                        bookmark = chNum == 3,
                        isDownloaded = chNum <= 3
                    )
                )
            }
        }
        dao.insertChapters(chapters)

        // Seed Initial Reading Snaps
        dao.saveSnap(
            ReadingSnapEntity(
                mangaId = 1,
                chapterId = 102,
                title = "Solo Astral Leveling",
                coverUrl = "android.resource://${context.packageName}/${R.drawable.cover_solo_astral_1787734014926}",
                chapterNumber = 2.0f,
                chapterName = "Chapter 2: The Double Dungeon Trial",
                pageIndex = 4,
                scrollOffset = 650,
                progressPercent = 0.42f,
                timestamp = System.currentTimeMillis() - 1800000L
            )
        )

        dao.saveSnap(
            ReadingSnapEntity(
                mangaId = 2,
                chapterId = 201,
                title = "Cyberpunk: Phantom Edge",
                coverUrl = "android.resource://${context.packageName}/${R.drawable.cover_cyber_phantom_1787734029636}",
                chapterNumber = 1.0f,
                chapterName = "Chapter 1: Neon Shadows",
                pageIndex = 8,
                scrollOffset = 1200,
                progressPercent = 0.67f,
                timestamp = System.currentTimeMillis() - 7200000L
            )
        )

        // Seed Achievements
        dao.insertAchievements(
            listOf(
                AchievementEntity("first_snap", "Lumina Snap Pioneer", "Restored active reading state in under 1 second", true, 150, "Speed", 1.0f),
                AchievementEntity("chapter_master", "Shadow Monarch Reader", "Completed 10 sequential chapters with Lumina Flow", true, 250, "Reading", 1.0f),
                AchievementEntity("dna_explorer", "Manga DNA Cartographer", "Inspected the Universe Map & character relationship graph", false, 200, "Intelligence", 0.6f),
                AchievementEntity("offline_vault", "Offline Archive Master", "Downloaded full chapters for offline flight reading", true, 300, "Storage", 1.0f),
                AchievementEntity("marathon_7", "7-Day Lumina Streak", "Maintained an unbroken reading streak for 7 consecutive days", true, 500, "Streak", 1.0f),
                AchievementEntity("speed_demon", "Hyper-Flow Reading (30 PPM)", "Achieved reading pace exceeding 30 pages per minute", false, 400, "Speed", 0.8f)
            )
        )
    }

    private fun loadDefaultExtensions() {
        val sampleExtensions = listOf(
            ExtensionPackage(
                packageName = "com.lumina.extension.mangadex",
                name = "MangaDex (Official Multi-Lang)",
                versionCode = 14,
                versionName = "1.4.12",
                apkUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/apk/mangadex.apk",
                iconUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/icon/mangadex.png",
                signatureHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                lang = "all",
                isNsfw = false,
                isInstalled = true,
                sources = listOf(
                    ExtensionSourceMeta(1001, "MangaDex EN", "en", "https://mangadex.org", "MangaDexEnSource"),
                    ExtensionSourceMeta(1002, "MangaDex JA", "ja", "https://mangadex.org", "MangaDexJaSource")
                )
            ),
            ExtensionPackage(
                packageName = "com.lumina.extension.asurascans",
                name = "Asura Scans (Webtoons)",
                versionCode = 8,
                versionName = "1.2.0",
                apkUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/apk/asura.apk",
                iconUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/icon/asura.png",
                signatureHash = "4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a",
                lang = "en",
                isNsfw = false,
                isInstalled = false,
                sources = listOf(ExtensionSourceMeta(1003, "Asura Scans", "en", "https://asuracomic.net", "AsuraSource"))
            ),
            ExtensionPackage(
                packageName = "com.lumina.extension.flamecomics",
                name = "Flame Comics",
                versionCode = 6,
                versionName = "1.1.5",
                apkUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/apk/flame.apk",
                iconUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/icon/flame.png",
                signatureHash = "ef2d127de37b942baad06145e54b0c619a1f22327b2ebbcfbec78f5564afe39d",
                lang = "en",
                isNsfw = false,
                isInstalled = false,
                sources = listOf(ExtensionSourceMeta(1004, "Flame Comics", "en", "https://flamecomics.xyz", "FlameSource"))
            ),
            ExtensionPackage(
                packageName = "com.lumina.extension.mangakakalot",
                name = "Mangakakalot & Manganato",
                versionCode = 11,
                versionName = "1.3.1",
                apkUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/apk/manganato.apk",
                iconUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/icon/manganato.png",
                signatureHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                lang = "en",
                isNsfw = false,
                isInstalled = true,
                sources = listOf(ExtensionSourceMeta(1005, "Manganato", "en", "https://manganato.com", "ManganatoSource"))
            )
        )
        _availableExtensions.value = sampleExtensions
    }

    private fun generateSamplePages(mangaId: Long, chapterNumber: Int, totalPages: Int): List<String> {
        // High performance local drawable resource URLs or web artwork URLs
        val drawableName = when (mangaId) {
            1L -> R.drawable.cover_solo_astral_1787734014926
            2L -> R.drawable.cover_cyber_phantom_1787734029636
            3L -> R.drawable.cover_shadow_alchem_1787734044487
            else -> R.drawable.cover_neon_odyssey_1787734059096
        }
        val baseUri = "android.resource://${context.packageName}/$drawableName"
        return List(totalPages) { pageIndex ->
            // Return high quality responsive image URI
            "$baseUri#page=$pageIndex"
        }
    }

    fun getMangaDna(mangaId: Long): List<DnaAttribute> {
        return when (mangaId) {
            1L -> listOf(
                DnaAttribute("Action Intensity", 0.96f, LuminaVioletPrimary, "Overwhelming hunter battles & dimensional boss fights"),
                DnaAttribute("Darkness & Aura", 0.88f, LuminaRoseAccent, "Shadow extraction and necromantic monarch lore"),
                DnaAttribute("System Progression", 0.94f, LuminaCyanAccent, "Hyper-addictive RPG level up mechanics"),
                DnaAttribute("Mystery & Lore", 0.78f, LuminaAmberGlow, "Rulers vs Monarchs primordial celestial war"),
                DnaAttribute("Tactical Warfare", 0.82f, LuminaEmerald, "Strategic shadow army positioning")
            )
            2L -> listOf(
                DnaAttribute("Cyberpunk Aesthetics", 0.95f, LuminaCyanAccent, "Neo-Shinjuku rain-slicked neon skyscrapers"),
                DnaAttribute("Fast-Paced Action", 0.90f, LuminaVioletPrimary, "High-frequency blade duels and bullet-time"),
                DnaAttribute("Corporate Intrigue", 0.85f, LuminaAmberGlow, "Mega-corp conspiracies and digitized souls"),
                DnaAttribute("Psychological Tension", 0.80f, LuminaRoseAccent, "Cyber-psychosis and transhumanist dilemmas"),
                DnaAttribute("Stealth & Infiltration", 0.75f, LuminaEmerald, "Optical camouflage and shadow operative ops")
            )
            3L -> listOf(
                DnaAttribute("Arcane Mystery", 0.92f, LuminaAmberGlow, "Transmutation circles and forbidden grimoires"),
                DnaAttribute("Occult Darkness", 0.86f, LuminaRoseAccent, "Forbidden shadow alchemy catacombs"),
                DnaAttribute("Tactical Strategy", 0.84f, LuminaEmerald, "Meticulous compound brewing & elemental clashes"),
                DnaAttribute("Lore Depth", 0.89f, LuminaVioletPrimary, "Forgotten celestial pantheons"),
                DnaAttribute("Character Drama", 0.76f, LuminaCyanAccent, "Betrayal by the Imperial Arcane Council")
            )
            else -> listOf(
                DnaAttribute("Cosmic Sci-Fi", 0.98f, LuminaCyanAccent, "Dyson spheres, wormholes, and relativistic physics"),
                DnaAttribute("Existential Mystery", 0.93f, LuminaVioletPrimary, "First contact with an ancient super-intelligence"),
                DnaAttribute("Visual Wonder", 0.91f, LuminaAmberGlow, "Interstellar nebulas and alien mega-engineering"),
                DnaAttribute("Psychological Depth", 0.84f, LuminaRoseAccent, "Isolation in deep interstellar space"),
                DnaAttribute("Exploration", 0.90f, LuminaEmerald, "Unlocking forgotten star coordinates")
            )
        }
    }

    fun getUniverseNodes(mangaId: Long): List<UniverseNode> {
        return when (mangaId) {
            1L -> listOf(
                UniverseNode("1", "Sung Jin-Woo", "Shadow Monarch (Protagonist)", 300f, 400f, LuminaVioletPrimary, "E-Rank hunter who unlocked the Lumina System and inherited the Throne of Shadows.", "Shadow Legion", "Monarch Class"),
                UniverseNode("2", "Igris", "Blood Red Commander", 140f, 250f, LuminaRoseAccent, "Elite royal knight who wields dual broadswords and commands the vanguard.", "Shadow Legion", "Marshal Rank"),
                UniverseNode("3", "Beru", "Ant King Marshal", 460f, 250f, LuminaCyanAccent, "Fierce combatant with hypersonic flight, venom claws, and profound loyalty.", "Shadow Legion", "Grand Marshal"),
                UniverseNode("4", "Cha Hae-In", "S-Rank Blade Dancer", 160f, 580f, LuminaAmberGlow, "Vice-guild master of Hunters Guild, sensitive to mana scent.", "Hunters Association", "S-Rank Hunter"),
                UniverseNode("5", "Go Gun-Hee", "Association Chairman", 440f, 580f, LuminaEmerald, "Vessel of the Brightest Fragment of Brilliant Light.", "Hunters Association", "Chairman"),
                UniverseNode("6", "Antares", "Monarch of Destruction", 300f, 120f, Color(0xFFEF4444), "Dragon King and the King of Berserk Dragons.", "Monarchs", "Dragon Monarch")
            )
            2L -> listOf(
                UniverseNode("1", "Ren", "Ghost Ninja (Protagonist)", 300f, 400f, LuminaCyanAccent, "Shadow operative with prototype optical camo and dual violet blades.", "Neon Phantoms", "S-Class Operative"),
                UniverseNode("2", "Kira", "Master Netrunner", 150f, 280f, LuminaVioletPrimary, "Legendary decker capable of cracking corp ICE in milliseconds.", "Neon Phantoms", "Cyber Prodigy"),
                UniverseNode("3", "Vance", "Heavy Cyborg Vanguard", 450f, 280f, LuminaAmberGlow, "Ex-military brawler with titanium chassis and heavy railguns.", "Neon Phantoms", "Heavy Armored"),
                UniverseNode("4", "Executive Kuroda", "Zaibatsu CEO", 300f, 150f, LuminaRoseAccent, "Supreme head of Kuroda Cybernetics corporation.", "Kuroda Zaibatsu", "Chairman")
            )
            3L -> listOf(
                UniverseNode("1", "Valen", "Shadow Alchemist", 300f, 400f, LuminaAmberGlow, "Master of forbidden transmutation and amber shadow runes.", "Black Alchemists", "Grand Alchemist"),
                UniverseNode("2", "Lyra", "Arcane Scholar", 160f, 280f, LuminaCyanAccent, "Decipherer of ancient celestial scripts and planetary alignments.", "Scholars", "High Sage"),
                UniverseNode("3", "Grand Inquisitor", "Imperial Enforcer", 440f, 280f, LuminaRoseAccent, "Zealous commander seeking to eradicate all dark alchemy.", "Imperial Inquisition", "Inquisitor")
            )
            else -> listOf(
                UniverseNode("1", "Captain Tyler", "Odyssey Commander", 300f, 400f, LuminaCyanAccent, "Navigator of the CSS Odyssey exploring the Dyson Megastructure.", "Odyssey Crew", "Commander"),
                UniverseNode("2", "Aria", "Sentient AI Core", 160f, 260f, LuminaVioletPrimary, "Quantum computing intelligence calculating relativistic coordinates.", "Odyssey Crew", "Quantum AI"),
                UniverseNode("3", "The Entity", "Stellar Intelligence", 300f, 140f, LuminaAmberGlow, "Consciousness residing within the core of the ancient Dyson sphere.", "Precursors", "Stellar Entity")
            )
        }
    }

    fun getUniverseEdges(mangaId: Long): List<UniverseEdge> {
        return when (mangaId) {
            1L -> listOf(
                UniverseEdge("1", "2", "Shadow Extraction (Commander)", LuminaVioletSecondary),
                UniverseEdge("1", "3", "Shadow Extraction (Grand Marshal)", LuminaCyanAccent),
                UniverseEdge("1", "4", "Allied / Romance Dynamic", LuminaAmberGlow),
                UniverseEdge("1", "5", "Mutual Respect & Protection", LuminaEmerald),
                UniverseEdge("1", "6", "Mortal Nemesis (Primordial War)", Color(0xFFEF4444)),
                UniverseEdge("2", "3", "Vanguard & Shield", LuminaVioletPrimary)
            )
            2L -> listOf(
                UniverseEdge("1", "2", "Tactical Neural Link", LuminaCyanAccent),
                UniverseEdge("1", "3", "Field Combat Unit", LuminaAmberGlow),
                UniverseEdge("1", "4", "Target of Assassination", LuminaRoseAccent),
                UniverseEdge("2", "4", "Corporate Data Theft", LuminaVioletPrimary)
            )
            3L -> listOf(
                UniverseEdge("1", "2", "Ancient Grimoire Partners", LuminaCyanAccent),
                UniverseEdge("1", "3", "Hunted / Exile Rivalry", LuminaRoseAccent)
            )
            else -> listOf(
                UniverseEdge("1", "2", "Direct Neural Interface", LuminaVioletPrimary),
                UniverseEdge("1", "3", "Deciphering Cosmic Broadcast", LuminaAmberGlow)
            )
        }
    }
}

private fun MangaEntity.toDomain(
    dna: List<DnaAttribute>,
    nodes: List<UniverseNode>,
    edges: List<UniverseEdge>
): Manga {
    val genreList = genres.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    return Manga(
        id = id,
        sourceId = sourceId,
        sourceName = sourceName,
        url = url,
        title = title,
        artist = artist ?: "",
        author = author ?: "",
        description = description ?: "",
        genres = genreList,
        status = status,
        thumbnailUrl = thumbnailUrl ?: "",
        favorite = favorite,
        lastUpdate = lastUpdate,
        flags = flags,
        rating = rating,
        totalChapters = 8,
        dnaAttributes = dna,
        universeNodes = nodes,
        universeEdges = edges
    )
}
