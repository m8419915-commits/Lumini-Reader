package com.example.luminareader.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.luminareader.data.model.*
import com.example.luminareader.ui.theme.*

@Composable
fun HighPerformanceReaderScreen(
    mangaId: Int,
    chapterId: Int,
    initialPage: Int = 0,
    mangas: List<Manga>,
    chapters: List<Chapter>,
    readerConfig: ReaderConfig,
    onBack: () -> Unit,
    onSaveSnap: (ReadingSnap) -> Unit,
    onNextChapter: (Int) -> Unit,
    onPrevChapter: (Int) -> Unit,
    onUpdateConfig: (ReaderConfig) -> Unit
) {
    val manga = mangas.find { it.id == mangaId } ?: mangas.first()
    val allChapters = chapters.filter { it.mangaId == mangaId }.sortedBy { it.chapterNumber }
    val currentChapterIndex = allChapters.indexOfFirst { it.id == chapterId }.coerceAtLeast(0)
    val currentChapter = allChapters.getOrNull(currentChapterIndex) ?: allChapters.first()

    var showControls by remember { mutableStateOf(true) }
    var showSettingsSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialPage)
    val currentPageIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    val totalPages = currentChapter.pages.size.coerceAtLeast(1)
    val progressPercent = ((currentPageIndex + 1).toFloat() / totalPages * 100).toInt().coerceIn(0, 100)

    val bgColor = when (readerConfig.backgroundTint) {
        BackgroundTint.PITCH_BLACK -> Color(0xFF000000)
        BackgroundTint.CHARCOAL -> Color(0xFF0D0D11)
        BackgroundTint.SEPIA -> Color(0xFF1A140B)
        BackgroundTint.DEEP_SLATE -> Color(0xFF0B1118)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .testTag("high_performance_reader")
    ) {
        // Continuous Webtoon Page List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showControls = !showControls
                }
        ) {
            itemsIndexed(currentChapter.pages) { index, pageUrl ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pageUrl,
                        contentDescription = "Page ${index + 1}",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp)
                    )

                    // Page indicator on bottom right of each page
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${index + 1}/$totalPages",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Chapter End Transition Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "End of Chapter ${currentChapter.chapterNumber.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (currentChapterIndex < allChapters.size - 1) {
                        val nextCh = allChapters[currentChapterIndex + 1]
                        Button(
                            onClick = { onNextChapter(nextCh.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet)
                        ) {
                            Text("Next Chapter: Ch. ${nextCh.chapterNumber.toInt()}")
                        }
                    } else {
                        Text("You are on the latest available chapter!", color = TextMuted, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Lumina Flow Glow Border (Ambient light)
        if (readerConfig.enableLuminaFlow) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        2.dp,
                        Brush.verticalGradient(
                            listOf(
                                LuminaCyan.copy(alpha = 0.2f),
                                Color.Transparent,
                                LuminaViolet.copy(alpha = 0.2f)
                            )
                        )
                    )
            )
        }

        // Animated Top HUD Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = Color.Black.copy(alpha = 0.88f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LuminaSurfaceVariant)
                            .testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = manga.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            maxLines = 1,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${currentChapter.title} • Page ${currentPageIndex + 1}/$totalPages",
                            style = MaterialTheme.typography.bodySmall,
                            color = LuminaCyan,
                            fontSize = 11.sp
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                onSaveSnap(
                                    ReadingSnap(
                                        mangaId = manga.id,
                                        title = manga.title,
                                        coverUrl = manga.thumbnailUrl,
                                        chapterId = currentChapter.id,
                                        chapterNumber = currentChapter.chapterNumber,
                                        pageIndex = currentPageIndex,
                                        totalPages = totalPages,
                                        progressPercent = progressPercent
                                    )
                                )
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(LuminaSurfaceVariant)
                                .testTag("reader_save_snap_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Save Snap",
                                tint = LuminaCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = { showSettingsSheet = true },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(LuminaSurfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Settings",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Animated Bottom HUD Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = Color.Black.copy(alpha = 0.88f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Page Scrub / Progress Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${currentPageIndex + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaCyan,
                            fontWeight = FontWeight.Bold
                        )

                        Slider(
                            value = (currentPageIndex + 1).toFloat(),
                            onValueChange = { /* scrubbing handled via smooth scroll */ },
                            valueRange = 1f..totalPages.toFloat(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = LuminaCyan,
                                activeTrackColor = LuminaCyan,
                                inactiveTrackColor = LuminaBorder
                            )
                        )

                        Text(
                            text = "$totalPages",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    // Navigation Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                if (currentChapterIndex > 0) {
                                    onPrevChapter(allChapters[currentChapterIndex - 1].id)
                                }
                            },
                            enabled = currentChapterIndex > 0
                        ) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Prev Chapter", fontSize = 12.sp)
                        }

                        // Background Tint Selector Buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            BackgroundTint.values().forEach { tint ->
                                val tintColor = when (tint) {
                                    BackgroundTint.PITCH_BLACK -> Color.Black
                                    BackgroundTint.CHARCOAL -> Color(0xFF1E1E24)
                                    BackgroundTint.SEPIA -> Color(0xFF382B17)
                                    BackgroundTint.DEEP_SLATE -> Color(0xFF152238)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(tintColor)
                                        .border(
                                            1.5.dp,
                                            if (readerConfig.backgroundTint == tint) LuminaCyan else LuminaBorder,
                                            CircleShape
                                        )
                                        .clickable { onUpdateConfig(readerConfig.copy(backgroundTint = tint)) }
                                )
                            }
                        }

                        TextButton(
                            onClick = {
                                if (currentChapterIndex < allChapters.size - 1) {
                                    onNextChapter(allChapters[currentChapterIndex + 1].id)
                                }
                            },
                            enabled = currentChapterIndex < allChapters.size - 1
                        ) {
                            Text("Next Chapter", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.SkipNext, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Reader Configuration Dialog / Bottom Sheet
        if (showSettingsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSettingsSheet = false },
                containerColor = LuminaCard,
                dragHandle = { BottomSheetDefaults.DragHandle(color = LuminaBorder) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Reader Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Lumina Flow Ambient Glow", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Bezel edge illumination", color = TextMuted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = readerConfig.enableLuminaFlow,
                            onCheckedChange = { onUpdateConfig(readerConfig.copy(enableLuminaFlow = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyan, checkedTrackColor = LuminaViolet)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Keep Screen Awake", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Prevent device sleep while reading", color = TextMuted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = readerConfig.keepScreenOn,
                            onCheckedChange = { onUpdateConfig(readerConfig.copy(keepScreenOn = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyan, checkedTrackColor = LuminaViolet)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Crop White Borders", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Maximize image footprint on AMOLED", color = TextMuted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = readerConfig.cropWhiteBorders,
                            onCheckedChange = { onUpdateConfig(readerConfig.copy(cropWhiteBorders = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyan, checkedTrackColor = LuminaViolet)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
