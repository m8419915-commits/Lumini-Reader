package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.ai.LuminaAiAssistantScreen
import com.example.ui.analytics.AnalyticsScreen
import com.example.ui.backup.BackupSyncScreen
import com.example.ui.detail.MangaDetailScreen
import com.example.ui.dna.MangaDnaAchievementsScreen
import com.example.ui.explore.ExploreScreen
import com.example.ui.explore.RepositoriesScreen
import com.example.ui.forge.LuminaForgeScreen
import com.example.ui.home.HomeDashboardScreen
import com.example.ui.journey.LuminaJourneyMapScreen
import com.example.ui.library.LibraryScreen
import com.example.ui.more.MoreScreen
import com.example.ui.mutation.LuminaMutationScreen
import com.example.ui.reader.HighPerformanceReaderScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.snap.LuminaSnapSwitcherSheet
import com.example.ui.theme.*
import com.example.ui.timeline.MangaTimelineScreen
import com.example.ui.universe.MangaUniverseMapScreen
import com.example.ui.updates.UpdatesScreen

class MainActivity : ComponentActivity() {
    private val viewModel: LuminaMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuminaReaderTheme {
                LuminaReaderApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LuminaReaderApp(viewModel: LuminaMainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle back button for sub-screens
    BackHandler(enabled = uiState.currentScreen !is Screen.Home) {
        when (uiState.currentScreen) {
            is Screen.Reader -> {
                uiState.selectedManga?.let { viewModel.navigateTo(Screen.MangaDetail(it.id)) }
                    ?: viewModel.navigateTo(Screen.Home)
            }
            is Screen.UniverseMap, is Screen.MangaDna, is Screen.Timeline -> {
                uiState.selectedManga?.let { viewModel.navigateTo(Screen.MangaDetail(it.id)) }
                    ?: viewModel.navigateTo(Screen.Home)
            }
            is Screen.MangaDetail -> viewModel.navigateTo(Screen.Home)
            is Screen.Repositories -> viewModel.navigateTo(Screen.Explore)
            is Screen.AiAssistant, is Screen.JourneyMap, is Screen.Mutation, is Screen.Forge, is Screen.BackupSync, is Screen.Analytics, is Screen.Settings -> {
                viewModel.navigateTo(Screen.More)
            }
            else -> viewModel.navigateTo(Screen.Home)
        }
    }

    val isTopLevelTab = uiState.currentScreen in listOf(
        Screen.Home,
        Screen.Library,
        Screen.Explore,
        Screen.Updates,
        Screen.More
    )

    Scaffold(
        containerColor = LuminaBlack,
        bottomBar = {
            if (isTopLevelTab) {
                NavigationBar(
                    containerColor = LuminaSurface,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .border(androidx.compose.foundation.BorderStroke(0.5.dp, LuminaBorder))
                        .testTag("bottom_nav_bar")
                ) {
                    val tabs = listOf(
                        Triple(Screen.Home, "Home", Icons.Default.Home to Icons.Outlined.Home),
                        Triple(Screen.Library, "Library", Icons.AutoMirrored.Filled.MenuBook to Icons.AutoMirrored.Outlined.MenuBook),
                        Triple(Screen.Explore, "Browse", Icons.Default.Explore to Icons.Outlined.Explore),
                        Triple(Screen.Updates, "Updates", Icons.Default.Download to Icons.Outlined.Download),
                        Triple(Screen.More, "More", Icons.Default.MoreHoriz to Icons.Outlined.MoreHoriz)
                    )

                    tabs.forEach { (screen, title, iconPair) ->
                        val isSelected = uiState.currentScreen == screen
                        val (selectedIcon, unselectedIcon) = iconPair

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(screen) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                    contentDescription = title,
                                    tint = if (isSelected) LuminaVioletSecondary else TextSecondary
                                )
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    color = if (isSelected) LuminaVioletSecondary else TextSecondary
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = LuminaVioletPrimary.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag("nav_tab_${title.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isTopLevelTab) padding else PaddingValues(0.dp))
        ) {
            when (val screen = uiState.currentScreen) {
                is Screen.Home -> {
                    HomeDashboardScreen(
                        currentStreak = uiState.streakDays,
                        snaps = uiState.activeSnaps,
                        allMangas = uiState.allMangas,
                        onOpenSnap = { snap ->
                            viewModel.navigateTo(Screen.Reader(snap.mangaId, snap.chapterId, snap.pageIndex, snap.scrollOffset))
                        },
                        onOpenManga = { mangaId ->
                            viewModel.navigateTo(Screen.MangaDetail(mangaId))
                        },
                        onOpenExplore = {
                            viewModel.navigateTo(Screen.Explore)
                        },
                        onOpenSnapSwitcher = {
                            viewModel.setShowSnapSwitcher(true)
                        },
                        onOpenSearch = {
                            viewModel.navigateTo(Screen.Library)
                        }
                    )
                }

                is Screen.Library -> {
                    LibraryScreen(
                        libraryMangas = uiState.libraryMangas,
                        allMangas = uiState.allMangas,
                        searchQuery = uiState.searchQuery,
                        selectedGenre = uiState.selectedGenreFilter,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onGenreSelect = { viewModel.setGenreFilter(it) },
                        onOpenManga = { mangaId ->
                            viewModel.navigateTo(Screen.MangaDetail(mangaId))
                        },
                        onOpenExplore = {
                            viewModel.navigateTo(Screen.Explore)
                        }
                    )
                }

                is Screen.Explore -> {
                    ExploreScreen(
                        availableExtensions = uiState.availableExtensions,
                        installedExtensions = uiState.installedExtensions,
                        allMangas = uiState.allMangas,
                        repoUrl = uiState.repoUrlInput,
                        isLoading = uiState.isRepoLoading,
                        errorMessage = uiState.repoErrorMessage,
                        onRepoUrlChange = { viewModel.setRepoUrlInput(it) },
                        onFetchRepo = { viewModel.fetchExtensions() },
                        onToggleExtension = { viewModel.toggleExtension(it) },
                        onOpenManga = { mangaId ->
                            viewModel.navigateTo(Screen.MangaDetail(mangaId))
                        },
                        onOpenRepositories = {
                            viewModel.navigateTo(Screen.Repositories)
                        }
                    )
                }

                is Screen.Repositories -> {
                    RepositoriesScreen(
                        repoUrlInput = uiState.repoUrlInput,
                        isLoading = uiState.isRepoLoading,
                        errorMessage = uiState.repoErrorMessage,
                        onRepoUrlChange = { viewModel.setRepoUrlInput(it) },
                        onFetchRepo = { viewModel.fetchExtensions() },
                        onBack = { viewModel.navigateTo(Screen.Explore) }
                    )
                }

                is Screen.Updates -> {
                    UpdatesScreen(
                        updates = uiState.updateItems,
                        onDownloadUpdate = { viewModel.downloadUpdate(it) },
                        onDownloadAll = { viewModel.downloadAllUpdates() },
                        onOpenManga = { mangaId -> viewModel.navigateTo(Screen.MangaDetail(mangaId)) }
                    )
                }

                is Screen.More -> {
                    MoreScreen(
                        onNavigate = { destination -> viewModel.navigateTo(destination) }
                    )
                }

                is Screen.AiAssistant -> {
                    LuminaAiAssistantScreen(
                        messages = uiState.aiMessages,
                        onSendMessage = { viewModel.sendAiMessage(it) },
                        onOpenManga = { mangaId -> viewModel.navigateTo(Screen.MangaDetail(mangaId)) },
                        onBack = { viewModel.navigateTo(Screen.More) }
                    )
                }

                is Screen.Timeline -> {
                    MangaTimelineScreen(
                        mangaTitle = uiState.selectedManga?.title ?: "Timeline",
                        items = uiState.timelineItems,
                        onBack = {
                            uiState.selectedManga?.let { viewModel.navigateTo(Screen.MangaDetail(it.id)) }
                                ?: viewModel.navigateTo(Screen.Home)
                        }
                    )
                }

                is Screen.Forge -> {
                    LuminaForgeScreen(
                        experiencePacks = uiState.experiencePacks,
                        onActivatePack = { viewModel.activateExperiencePack(it) },
                        onBack = { viewModel.navigateTo(Screen.More) }
                    )
                }

                is Screen.JourneyMap -> {
                    LuminaJourneyMapScreen(
                        onBack = { viewModel.navigateTo(Screen.More) }
                    )
                }

                is Screen.Mutation -> {
                    LuminaMutationScreen(
                        behaviors = uiState.mutationBehaviors,
                        onToggleLock = { viewModel.toggleMutationLock(it) },
                        onResetMutation = { viewModel.resetMutation(it) },
                        onBack = { viewModel.navigateTo(Screen.More) }
                    )
                }

                is Screen.BackupSync -> {
                    BackupSyncScreen(
                        syncLibrary = uiState.syncLibrary,
                        syncProgress = uiState.syncProgress,
                        syncCategories = uiState.syncCategories,
                        syncSettings = uiState.syncSettings,
                        lastBackupTime = uiState.lastBackupTime,
                        onToggleSync = { key, value -> viewModel.toggleSyncItem(key, value) },
                        onSyncNow = { viewModel.triggerSyncNow() },
                        onBack = { viewModel.navigateTo(Screen.More) }
                    )
                }

                is Screen.Analytics -> {
                    AnalyticsScreen(
                        readingSpeedPpm = uiState.readingSpeedPpm,
                        totalMinutes = uiState.totalReadingMinutes,
                        chaptersRead = uiState.chaptersReadCount,
                        streakDays = uiState.streakDays,
                        achievements = uiState.achievements
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        readerConfig = uiState.readerConfig,
                        onUpdateReaderConfig = { viewModel.updateReaderConfig(it) }
                    )
                }

                is Screen.MangaDetail -> {
                    MangaDetailScreen(
                        manga = uiState.selectedManga,
                        chapters = uiState.selectedChapters,
                        onBack = { viewModel.navigateTo(Screen.Home) },
                        onOpenChapter = { chapterId ->
                            uiState.selectedManga?.let {
                                viewModel.navigateTo(Screen.Reader(it.id, chapterId))
                            }
                        },
                        onToggleFavorite = { mangaId ->
                            viewModel.toggleFavorite(mangaId)
                        },
                        onToggleBookmark = { chapterId ->
                            viewModel.toggleBookmark(chapterId)
                        },
                        onDownloadChapter = { chapterId ->
                            viewModel.downloadChapter(chapterId)
                        },
                        onDownloadAll = { mangaId ->
                            viewModel.downloadAllChapters(mangaId)
                        },
                        onOpenUniverseMap = { mangaId ->
                            viewModel.navigateTo(Screen.UniverseMap(mangaId))
                        },
                        onOpenMangaDna = { mangaId ->
                            viewModel.navigateTo(Screen.MangaDna(mangaId))
                        },
                        onOpenTimeline = { mangaId ->
                            viewModel.navigateTo(Screen.Timeline(mangaId))
                        }
                    )
                }

                is Screen.UniverseMap -> {
                    MangaUniverseMapScreen(
                        mangaTitle = uiState.selectedManga?.title ?: "Universe",
                        nodes = uiState.selectedManga?.universeNodes ?: emptyList(),
                        edges = uiState.selectedManga?.universeEdges ?: emptyList(),
                        onBack = {
                            uiState.selectedManga?.let { viewModel.navigateTo(Screen.MangaDetail(it.id)) }
                                ?: viewModel.navigateTo(Screen.Home)
                        }
                    )
                }

                is Screen.MangaDna -> {
                    MangaDnaAchievementsScreen(
                        seriesTitle = uiState.selectedManga?.title ?: "DNA Analysis",
                        dnaAttributes = uiState.selectedManga?.dnaAttributes ?: emptyList(),
                        achievements = uiState.achievements,
                        readingSpeedPpm = uiState.readingSpeedPpm,
                        totalReadingMinutes = uiState.totalReadingMinutes,
                        chaptersReadCount = uiState.chaptersReadCount,
                        currentStreak = uiState.streakDays,
                        onBack = {
                            uiState.selectedManga?.let { viewModel.navigateTo(Screen.MangaDetail(it.id)) }
                                ?: viewModel.navigateTo(Screen.Home)
                        }
                    )
                }

                is Screen.Reader -> {
                    HighPerformanceReaderScreen(
                        manga = uiState.selectedManga,
                        chapter = uiState.currentChapter,
                        chapters = uiState.selectedChapters,
                        readerConfig = uiState.readerConfig,
                        initialPage = screen.initialPage,
                        initialScroll = screen.initialScroll,
                        onBack = {
                            uiState.selectedManga?.let { viewModel.navigateTo(Screen.MangaDetail(it.id)) }
                                ?: viewModel.navigateTo(Screen.Home)
                        },
                        onSwitchChapter = { newChapterId ->
                            uiState.selectedManga?.let {
                                viewModel.navigateTo(Screen.Reader(it.id, newChapterId))
                            }
                        },
                        onSaveSnap = { mId, cId, title, cover, chNum, chName, pIdx, sOff, prog ->
                            viewModel.saveSnap(mId, cId, title, cover, chNum, chName, pIdx, sOff, prog)
                        },
                        onToggleBookmark = { chapterId ->
                            viewModel.toggleBookmark(chapterId)
                        },
                        onUpdateReaderConfig = { newConfig ->
                            viewModel.updateReaderConfig(newConfig)
                        }
                    )
                }
            }

            // LUMINA SNAP MODAL / SHEET OVERLAY
            if (uiState.showSnapSwitcher) {
                LuminaSnapSwitcherSheet(
                    activeSnaps = uiState.activeSnaps,
                    onSelectSnap = { snap ->
                        viewModel.setShowSnapSwitcher(false)
                        viewModel.navigateTo(
                            Screen.Reader(snap.mangaId, snap.chapterId, snap.pageIndex, snap.scrollOffset)
                        )
                    },
                    onCloseSnap = { mangaId ->
                        viewModel.deleteSnap(mangaId)
                    },
                    onDismiss = {
                        viewModel.setShowSnapSwitcher(false)
                    }
                )
            }
        }
    }
}
