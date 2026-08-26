package com.example.ui.reader

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.WindowManager
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.domain.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighPerformanceReaderScreen(
    manga: Manga?,
    chapter: Chapter?,
    chapters: List<Chapter>,
    readerConfig: ReaderConfig,
    initialPage: Int = 0,
    initialScroll: Int = 0,
    onBack: () -> Unit,
    onSwitchChapter: (Long) -> Unit,
    onSaveSnap: (mangaId: Long, chapterId: Long, title: String, coverUrl: String, chapterNumber: Float, chapterName: String, pageIndex: Int, scrollOffset: Int, progressPercent: Float) -> Unit,
    onToggleBookmark: (Long) -> Unit,
    onUpdateReaderConfig: (ReaderConfig) -> Unit
) {
    if (manga == null || chapter == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LuminaBlack),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = LuminaVioletPrimary)
        }
        return
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Keep screen awake if configured
    DisposableEffect(readerConfig.keepScreenOn) {
        val window = (context as? Activity)?.window
        if (readerConfig.keepScreenOn) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    // State & Controls
    var isHudVisible by remember { mutableStateOf(false) }
    var showSettingsModal by remember { mutableStateOf(false) }
    var currentPageIndex by remember { mutableIntStateOf(initialPage.coerceIn(0, (chapter.totalPages - 1).coerceAtLeast(0))) }
    var ambientColor by remember { mutableStateOf(LuminaVioletPrimary) }

    val totalPages = chapter.totalPages.coerceAtLeast(1)
    val webtoonListState = rememberLazyListState(initialFirstVisibleItemIndex = initialPage)
    val horizontalPagerState = rememberPagerState(initialPage = initialPage, pageCount = { totalPages })

    // Auto update current page from scroll
    LaunchedEffect(webtoonListState.firstVisibleItemIndex) {
        if (readerConfig.readerMode == ReaderMode.CONTINUOUS_WEBTOON) {
            currentPageIndex = webtoonListState.firstVisibleItemIndex.coerceIn(0, totalPages - 1)
        }
    }

    LaunchedEffect(horizontalPagerState.currentPage) {
        if (readerConfig.readerMode != ReaderMode.CONTINUOUS_WEBTOON) {
            currentPageIndex = horizontalPagerState.currentPage
        }
    }

    // Extract dynamic ambient color via Palette API (Lumina Flow)
    LaunchedEffect(currentPageIndex, chapter.pageUrls, readerConfig.enableLuminaFlow) {
        if (!readerConfig.enableLuminaFlow) return@LaunchedEffect
        withContext(Dispatchers.IO) {
            try {
                val pageUrl = chapter.pageUrls.getOrNull(currentPageIndex) ?: manga.thumbnailUrl
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(pageUrl)
                    .allowHardware(false)
                    .build()
                val result = (loader.execute(request) as? SuccessResult)?.drawable
                val bitmap = (result as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    val palette = Palette.from(bitmap).generate()
                    val vibrant = palette.getVibrantColor(palette.getDominantColor(LuminaVioletPrimary.toArgb()))
                    withContext(Dispatchers.Main) {
                        ambientColor = Color(vibrant)
                    }
                }
            } catch (e: Exception) {
                // Fallback default
            }
        }
    }

    // Auto-save snapshot on exit or page advance
    fun triggerSnapSave() {
        val progress = ((currentPageIndex + 1).toFloat() / totalPages.toFloat()).coerceIn(0f, 1f)
        onSaveSnap(
            manga.id,
            chapter.id,
            manga.title,
            manga.thumbnailUrl,
            chapter.chapterNumber,
            chapter.name,
            currentPageIndex,
            webtoonListState.firstVisibleItemScrollOffset,
            progress
        )
    }

    DisposableEffect(chapter.id) {
        onDispose {
            triggerSnapSave()
        }
    }

    val backgroundColor = Color(readerConfig.backgroundTint.hex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // LUMINA FLOW: Story-Responsive Ambient Glow Canvas
        if (readerConfig.enableLuminaFlow) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(80.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                ambientColor.copy(alpha = readerConfig.ambientGlowIntensity * 0.45f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // READER SURFACE
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val screenWidth = size.width
                        val tapZone = offset.x / screenWidth

                        if (tapZone in 0.3f..0.7f) {
                            // Center Zone -> Toggle HUD
                            isHudVisible = !isHudVisible
                        } else if (tapZone < 0.3f) {
                            // Left Zone -> Previous page in paged mode
                            if (readerConfig.readerMode == ReaderMode.SINGLE_PAGE_LTR && horizontalPagerState.currentPage > 0) {
                                coroutineScope.launch { horizontalPagerState.animateScrollToPage(horizontalPagerState.currentPage - 1) }
                            } else if (readerConfig.readerMode == ReaderMode.SINGLE_PAGE_RTL && horizontalPagerState.currentPage < totalPages - 1) {
                                coroutineScope.launch { horizontalPagerState.animateScrollToPage(horizontalPagerState.currentPage + 1) }
                            }
                        } else {
                            // Right Zone -> Next page in paged mode
                            if (readerConfig.readerMode == ReaderMode.SINGLE_PAGE_LTR && horizontalPagerState.currentPage < totalPages - 1) {
                                coroutineScope.launch { horizontalPagerState.animateScrollToPage(horizontalPagerState.currentPage + 1) }
                            } else if (readerConfig.readerMode == ReaderMode.SINGLE_PAGE_RTL && horizontalPagerState.currentPage > 0) {
                                coroutineScope.launch { horizontalPagerState.animateScrollToPage(horizontalPagerState.currentPage - 1) }
                            }
                        }
                    }
                }
        ) {
            when (readerConfig.readerMode) {
                ReaderMode.CONTINUOUS_WEBTOON -> {
                    // Continuous Vertical Webtoon with Long-Strip Tiling
                    LazyColumn(
                        state = webtoonListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("webtoon_reader_column"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(chapter.pageUrls, key = { index, url -> "$url-$index" }) { index, pageUrl ->
                            WebtoonPageItem(
                                pageIndex = index,
                                pageUrl = pageUrl,
                                mangaCover = manga.thumbnailUrl
                            )
                        }

                        // Next Chapter Quick Trigger
                        item {
                            val currentIndex = chapters.indexOfFirst { it.id == chapter.id }
                            val nextChapter = chapters.getOrNull(currentIndex - 1) // sorted desc
                            if (nextChapter != null) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { onSwitchChapter(nextChapter.id) },
                                    color = LuminaSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletPrimary)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("CONTINUE TO NEXT CHAPTER", style = MaterialTheme.typography.labelSmall, color = LuminaCyanAccent, fontWeight = FontWeight.Bold)
                                        Text(nextChapter.name, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = LuminaVioletPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                ReaderMode.SINGLE_PAGE_LTR, ReaderMode.SINGLE_PAGE_RTL -> {
                    // Paged mode
                    HorizontalPager(
                        state = horizontalPagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("paged_reader_pager"),
                        reverseLayout = readerConfig.readerMode == ReaderMode.SINGLE_PAGE_RTL
                    ) { pageIdx ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = chapter.pageUrls.getOrNull(pageIdx) ?: manga.thumbnailUrl,
                                contentDescription = "Page ${pageIdx + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(if (readerConfig.cropBorders) 0.dp else 4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                ReaderMode.DUAL_PAGE_SPREAD -> {
                    // Dual page spread
                    val spreadCount = (totalPages + 1) / 2
                    val spreadPagerState = rememberPagerState(pageCount = { spreadCount })

                    HorizontalPager(
                        state = spreadPagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { spreadIdx ->
                        val leftIdx = spreadIdx * 2
                        val rightIdx = leftIdx + 1
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                AsyncImage(
                                    model = chapter.pageUrls.getOrNull(leftIdx) ?: manga.thumbnailUrl,
                                    contentDescription = "Page ${leftIdx + 1}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            if (rightIdx < totalPages) {
                                Box(modifier = Modifier.weight(1f)) {
                                    AsyncImage(
                                        model = chapter.pageUrls.getOrNull(rightIdx) ?: manga.thumbnailUrl,
                                        contentDescription = "Page ${rightIdx + 1}",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // AMBIENT HUD OVERLAYS (Top & Bottom)
        AnimatedVisibility(
            visible = isHudVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = LuminaSurface.copy(alpha = 0.95f),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            triggerSnapSave()
                            onBack()
                        },
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = manga.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = chapter.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaCyanAccent,
                            maxLines = 1
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            triggerSnapSave()
                        }) {
                            Icon(Icons.Default.Bolt, contentDescription = "Save Snap", tint = LuminaAmberGlow)
                        }

                        IconButton(onClick = { onToggleBookmark(chapter.id) }) {
                            Icon(
                                imageVector = if (chapter.bookmark) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (chapter.bookmark) LuminaAmberGlow else Color.White
                            )
                        }

                        IconButton(onClick = { showSettingsModal = !showSettingsModal }) {
                            Icon(Icons.Default.Tune, contentDescription = "Settings", tint = Color.White)
                        }
                    }
                }
            }
        }

        // BOTTOM HUD CONTROLS
        AnimatedVisibility(
            visible = isHudVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = LuminaSurface.copy(alpha = 0.95f),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Page Progress Slider & Label
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Page ${currentPageIndex + 1} / $totalPages",
                            style = MaterialTheme.typography.labelMedium,
                            color = LuminaCyanAccent,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = readerConfig.readerMode.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Slider(
                        value = currentPageIndex.toFloat(),
                        onValueChange = { targetPage ->
                            val page = targetPage.toInt().coerceIn(0, totalPages - 1)
                            currentPageIndex = page
                            coroutineScope.launch {
                                if (readerConfig.readerMode == ReaderMode.CONTINUOUS_WEBTOON) {
                                    webtoonListState.scrollToItem(page)
                                } else {
                                    horizontalPagerState.scrollToPage(page)
                                }
                            }
                        },
                        valueRange = 0f..(totalPages - 1).coerceAtLeast(1).toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = LuminaVioletPrimary,
                            activeTrackColor = LuminaVioletPrimary,
                            inactiveTrackColor = LuminaBorder
                        ),
                        modifier = Modifier.testTag("reader_page_slider")
                    )

                    // Chapter Prev / Next & Mode Quick Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentIndex = chapters.indexOfFirst { it.id == chapter.id }
                        val prevChapter = chapters.getOrNull(currentIndex + 1) // older chapter in desc list
                        val nextChapter = chapters.getOrNull(currentIndex - 1) // newer chapter in desc list

                        OutlinedButton(
                            onClick = { prevChapter?.let { onSwitchChapter(it.id) } },
                            enabled = prevChapter != null,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                        ) {
                            Icon(Icons.Default.NavigateBefore, contentDescription = null)
                            Text("Prev Ch.", style = MaterialTheme.typography.labelSmall)
                        }

                        // Mode Selector Chips
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LuminaSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ReaderMode.entries.forEach { mode ->
                                    val isSelected = readerConfig.readerMode == mode
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) LuminaVioletPrimary else Color.Transparent,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onUpdateReaderConfig(readerConfig.copy(readerMode = mode)) }
                                    ) {
                                        Text(
                                            text = when (mode) {
                                                ReaderMode.CONTINUOUS_WEBTOON -> "Webtoon"
                                                ReaderMode.SINGLE_PAGE_LTR -> "LTR"
                                                ReaderMode.SINGLE_PAGE_RTL -> "RTL"
                                                ReaderMode.DUAL_PAGE_SPREAD -> "Spread"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = { nextChapter?.let { onSwitchChapter(it.id) } },
                            enabled = nextChapter != null,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                        ) {
                            Text("Next Ch.", style = MaterialTheme.typography.labelSmall)
                            Icon(Icons.Default.NavigateNext, contentDescription = null)
                        }
                    }
                }
            }
        }

        // QUICK SETTINGS MODAL (Lumina Flow, AMOLED Tint, Screen Awaking)
        if (showSettingsModal) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("reader_settings_modal"),
                color = LuminaSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "READER ENGINE CONFIG",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showSettingsModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    // Lumina Flow Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Lumina Flow Dynamic Ambient", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("Story-responsive Palette backlight glow", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Switch(
                            checked = readerConfig.enableLuminaFlow,
                            onCheckedChange = { onUpdateReaderConfig(readerConfig.copy(enableLuminaFlow = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuminaVioletPrimary, checkedTrackColor = LuminaVioletSecondary)
                        )
                    }

                    // Ambient Glow Intensity
                    if (readerConfig.enableLuminaFlow) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Ambient Glow Intensity (${(readerConfig.ambientGlowIntensity * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall, color = LuminaCyanAccent)
                            Slider(
                                value = readerConfig.ambientGlowIntensity,
                                onValueChange = { onUpdateReaderConfig(readerConfig.copy(ambientGlowIntensity = it)) },
                                valueRange = 0.2f..1.0f,
                                colors = SliderDefaults.colors(thumbColor = LuminaCyanAccent, activeTrackColor = LuminaCyanAccent)
                            )
                        }
                    }

                    // Background AMOLED Tint
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Canvas Background Tint", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BackgroundTint.entries.forEach { tint ->
                                val isSelected = readerConfig.backgroundTint == tint
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onUpdateReaderConfig(readerConfig.copy(backgroundTint = tint)) },
                                    color = Color(tint.hex),
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) LuminaVioletPrimary else LuminaBorder
                                    )
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = tint.name.take(4),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) LuminaCyanAccent else TextMuted,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Screen On Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Keep Screen Awake", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                        Switch(
                            checked = readerConfig.keepScreenOn,
                            onCheckedChange = { onUpdateReaderConfig(readerConfig.copy(keepScreenOn = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuminaVioletPrimary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WebtoonPageItem(
    pageIndex: Int,
    pageUrl: String,
    mangaCover: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = pageUrl.ifEmpty { mangaCover },
            contentDescription = "Webtoon Strip Page ${pageIndex + 1}",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.FillWidth
        )
    }
}
