package com.example.luminareader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminareader.data.model.ScreenType
import com.example.luminareader.ui.components.LuminaBottomNavBar
import com.example.luminareader.ui.components.SnapSwitcherModal
import com.example.luminareader.ui.screens.*
import com.example.luminareader.ui.theme.LuminaBlack
import com.example.luminareader.ui.theme.LuminaCyan
import com.example.luminareader.ui.theme.LuminaReaderTheme
import com.example.luminareader.ui.theme.LuminaViolet
import com.example.luminareader.ui.viewmodel.LuminaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LuminaReaderTheme {
                val viewModel: LuminaViewModel = viewModel()
                val screenState by viewModel.screenState.collectAsState()
                val isSnapSwitcherOpen by viewModel.isSnapSwitcherOpen.collectAsState()
                val activeToast by viewModel.activeToast.collectAsState()
                val isAiLoading by viewModel.isAiLoading.collectAsState()

                val mangas by viewModel.mangas.collectAsState()
                val chapters by viewModel.chapters.collectAsState()
                val snaps by viewModel.readingSnaps.collectAsState()
                val historyItems by viewModel.historyItems.collectAsState()
                val categories by viewModel.categories.collectAsState()
                val updates by viewModel.updates.collectAsState()
                val extensions by viewModel.extensions.collectAsState()
                val sources by viewModel.sources.collectAsState()
                val migrationItems by viewModel.migrationItems.collectAsState()
                val universeNodes by viewModel.universeNodes.collectAsState()
                val universeEdges by viewModel.universeEdges.collectAsState()
                val dnaAttributes by viewModel.dnaAttributes.collectAsState()
                val achievements by viewModel.achievements.collectAsState()
                val experiencePacks by viewModel.experiencePacks.collectAsState()
                val mutationBehaviors by viewModel.mutationBehaviors.collectAsState()
                val timelineItems by viewModel.timelineItems.collectAsState()
                val readerConfig by viewModel.readerConfig.collectAsState()
                val libraryFilters by viewModel.libraryFilters.collectAsState()
                val aiMessages by viewModel.aiMessages.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()

                // Handle system back navigation
                BackHandler(enabled = viewModel.canGoBack) {
                    viewModel.goBack()
                }

                val showBottomBar = screenState.type in listOf(
                    ScreenType.HOME,
                    ScreenType.LIBRARY,
                    ScreenType.UPDATES,
                    ScreenType.HISTORY,
                    ScreenType.EXPLORE,
                    ScreenType.MORE
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            LuminaBottomNavBar(
                                currentScreen = screenState.type,
                                onNavigate = { viewModel.navigate(it) },
                                updatesCount = updates.size
                            )
                        }
                    },
                    containerColor = LuminaBlack
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp)
                    ) {
                        when (screenState.type) {
                            ScreenType.HOME -> {
                                HomeScreen(
                                    mangas = mangas,
                                    snaps = snaps,
                                    updates = updates,
                                    onNavigate = { type, mId, cId -> viewModel.navigate(type, mId, cId) },
                                    onOpenSnapSwitcher = { viewModel.setSnapSwitcherOpen(true) },
                                    onOpenAi = { viewModel.navigate(ScreenType.AI) }
                                )
                            }
                            ScreenType.LIBRARY -> {
                                LibraryScreen(
                                    mangas = mangas,
                                    categories = categories,
                                    libraryFilters = libraryFilters,
                                    searchQuery = searchQuery,
                                    onSearchChange = { viewModel.setSearchQuery(it) },
                                    onFilterChange = { viewModel.updateLibraryFilters(it) },
                                    onMangaClick = { viewModel.navigate(ScreenType.DETAIL, it) }
                                )
                            }
                            ScreenType.UPDATES -> {
                                UpdatesScreen(
                                    updates = updates,
                                    onMangaClick = { viewModel.navigate(ScreenType.DETAIL, it) },
                                    onDownloadUpdate = { viewModel.showToast("Chapter download queued") }
                                )
                            }
                            ScreenType.HISTORY -> {
                                HistoryScreen(
                                    historyItems = historyItems,
                                    onResume = { mId, cId, page -> viewModel.navigate(ScreenType.READER, mId, cId, page) },
                                    onRemoveItem = { viewModel.removeHistoryItem(it) },
                                    onClearAll = { viewModel.clearHistory() }
                                )
                            }
                            ScreenType.EXPLORE -> {
                                ExploreScreen(
                                    sources = sources,
                                    extensions = extensions,
                                    migrationItems = migrationItems,
                                    onToggleExtensionInstall = { viewModel.toggleExtensionInstall(it) },
                                    onBrowseSource = { viewModel.navigate(ScreenType.LIBRARY) }
                                )
                            }
                            ScreenType.MORE -> {
                                MoreScreen(
                                    onNavigate = { viewModel.navigate(it) }
                                )
                            }
                            ScreenType.DETAIL -> {
                                MangaDetailScreen(
                                    mangaId = screenState.mangaId ?: 1,
                                    mangas = mangas,
                                    chapters = chapters,
                                    onBack = { viewModel.goBack() },
                                    onToggleLibrary = { viewModel.toggleLibrary(it) },
                                    onMarkChapterRead = { cId, read -> viewModel.markChapterRead(cId, read) },
                                    onToggleDownload = { viewModel.toggleChapterDownload(it) },
                                    onDownloadAll = { viewModel.downloadAllForManga(it) },
                                    onReadChapter = { mId, cId -> viewModel.navigate(ScreenType.READER, mId, cId) },
                                    onOpenUniverse = { viewModel.navigate(ScreenType.UNIVERSE, screenState.mangaId) },
                                    onOpenTimeline = { viewModel.navigate(ScreenType.TIMELINE, screenState.mangaId) }
                                )
                            }
                            ScreenType.READER -> {
                                HighPerformanceReaderScreen(
                                    mangaId = screenState.mangaId ?: 1,
                                    chapterId = screenState.chapterId ?: 101,
                                    initialPage = screenState.initialPage,
                                    mangas = mangas,
                                    chapters = chapters,
                                    readerConfig = readerConfig,
                                    onBack = { viewModel.goBack() },
                                    onSaveSnap = { viewModel.saveSnap(it) },
                                    onNextChapter = { viewModel.navigate(ScreenType.READER, screenState.mangaId, it) },
                                    onPrevChapter = { viewModel.navigate(ScreenType.READER, screenState.mangaId, it) },
                                    onUpdateConfig = { viewModel.updateReaderConfig(it) }
                                )
                            }
                            ScreenType.UNIVERSE -> {
                                MangaUniverseMapScreen(
                                    nodes = universeNodes,
                                    edges = universeEdges,
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.DNA -> {
                                MangaDnaScreen(
                                    dnaAttributes = dnaAttributes,
                                    achievements = achievements,
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.AI -> {
                                LuminaAiAssistantScreen(
                                    messages = aiMessages,
                                    isLoading = isAiLoading,
                                    onSendMessage = { viewModel.sendAiMessage(it) },
                                    onSelectRecommendation = { viewModel.showToast("Selected: $it") },
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.FORGE -> {
                                LuminaForgeScreen(
                                    packs = experiencePacks,
                                    onSetActivePack = { viewModel.setActivePack(it) },
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.MUTATION -> {
                                LuminaMutationScreen(
                                    mutations = mutationBehaviors,
                                    onToggleLock = { viewModel.toggleMutationLock(it) },
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.TIMELINE -> {
                                MangaTimelineScreen(
                                    timelineItems = timelineItems,
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.BACKUP -> {
                                BackupSyncScreen(
                                    onBackupCreated = { viewModel.showToast("Encrypted backup saved to local storage") },
                                    onRestoreBackup = { viewModel.showToast("Backup restored successfully") },
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            ScreenType.SETTINGS -> {
                                SettingsScreen(
                                    onBack = { viewModel.goBack() }
                                )
                            }
                            else -> {
                                HomeScreen(
                                    mangas = mangas,
                                    snaps = snaps,
                                    updates = updates,
                                    onNavigate = { type, mId, cId -> viewModel.navigate(type, mId, cId) },
                                    onOpenSnapSwitcher = { viewModel.setSnapSwitcherOpen(true) },
                                    onOpenAi = { viewModel.navigate(ScreenType.AI) }
                                )
                            }
                        }

                        // Floating Snap Switcher Modal
                        SnapSwitcherModal(
                            isOpen = isSnapSwitcherOpen,
                            snaps = snaps,
                            onClose = { viewModel.setSnapSwitcherOpen(false) },
                            onRestoreSnap = { viewModel.restoreSnap(it) },
                            onDeleteSnap = { viewModel.deleteSnap(it) }
                        )

                        // Floating Toast Bar
                        activeToast?.let { message ->
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = if (showBottomBar) 76.dp else 24.dp)
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, LuminaCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    .testTag("app_toast_bar"),
                                color = Color(0xFF18181F),
                                tonalElevation = 8.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(LuminaCyan)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
