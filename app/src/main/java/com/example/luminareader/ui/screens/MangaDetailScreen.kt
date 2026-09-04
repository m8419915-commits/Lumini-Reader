package com.example.luminareader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun MangaDetailScreen(
    mangaId: Int,
    mangas: List<Manga>,
    chapters: List<Chapter>,
    onBack: () -> Unit,
    onToggleLibrary: (Int) -> Unit,
    onMarkChapterRead: (Int, Boolean) -> Unit,
    onToggleDownload: (Int) -> Unit,
    onDownloadAll: (Int) -> Unit,
    onReadChapter: (Int, Int) -> Unit,
    onOpenUniverse: () -> Unit,
    onOpenTimeline: () -> Unit
) {
    val manga = mangas.find { it.id == mangaId } ?: mangas.first()
    val mangaChapters = chapters.filter { it.mangaId == mangaId }.sortedByDescending { it.chapterNumber }
    val firstUnread = mangaChapters.lastOrNull { !it.isRead } ?: mangaChapters.firstOrNull()

    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = LuminaBlack
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("manga_detail_screen"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header with Banner & Overlay
            item {
                Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                    AsyncImage(
                        model = manga.bannerUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        LuminaBlack.copy(alpha = 0.7f),
                                        LuminaBlack
                                    )
                                )
                            )
                    )

                    // Top Bar Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .testTag("detail_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { onToggleLibrary(manga.id) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .testTag("detail_library_toggle")
                            ) {
                                Icon(
                                    imageVector = if (manga.inLibrary) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                                    contentDescription = "Library",
                                    tint = if (manga.inLibrary) LuminaCyan else Color.White
                                )
                            }
                        }
                    }

                    // Poster and Metadata Details
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .align(Alignment.BottomStart),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        AsyncImage(
                            model = manga.thumbnailUrl,
                            contentDescription = manga.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 100.dp, height = 145.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, LuminaBorder, RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = manga.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${manga.author} • ${manga.artist}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = if (manga.status == "Completed") LuminaEmerald.copy(alpha = 0.2f) else LuminaCyan.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = manga.status,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (manga.status == "Completed") LuminaEmerald else LuminaCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = LuminaAmber.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = LuminaAmber, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "%.1f".format(manga.rating),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = LuminaAmber,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Text(
                                    text = manga.source,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons Row
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (firstUnread != null) {
                                onReadChapter(manga.id, firstUnread.id)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("detail_read_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (firstUnread != null) "Ch. ${firstUnread.chapterNumber.toInt()}" else "Start Reading",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = { onToggleLibrary(manga.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (manga.inLibrary) LuminaCyan else LuminaBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (manga.inLibrary) LuminaCyan.copy(alpha = 0.1f) else LuminaSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = if (manga.inLibrary) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (manga.inLibrary) LuminaCyan else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (manga.inLibrary) "In Library" else "Add to Library",
                            color = if (manga.inLibrary) LuminaCyan else Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Genre Chips
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    manga.genre.forEach { genre ->
                        Surface(
                            color = LuminaCard,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Description
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = manga.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = if (isDescriptionExpanded) 20 else 3,
                        lineHeight = 20.sp,
                        modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded }
                    )
                    Text(
                        text = if (isDescriptionExpanded) "Show less" else "Read more",
                        style = MaterialTheme.typography.bodySmall,
                        color = LuminaCyan,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                    )
                }
            }

            // Quick Portals: Universe Map & Narrative Timeline
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, LuminaViolet.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable { onOpenUniverse() },
                        color = LuminaCard
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Hub, contentDescription = null, tint = LuminaCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Universe Map", style = MaterialTheme.typography.titleMedium, fontSize = 13.sp, color = TextPrimary)
                                Text("Lore Graph", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, LuminaBorder, RoundedCornerShape(12.dp))
                            .clickable { onOpenTimeline() },
                        color = LuminaCard
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = LuminaAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Timeline", style = MaterialTheme.typography.titleMedium, fontSize = 13.sp, color = TextPrimary)
                                Text("Story Arcs", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }

            // Trackers (Mihon/Tachiyomi parity)
            if (manga.trackers.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "TRACKERS",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            manga.trackers.forEach { tracker ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = LuminaSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(tracker.color).copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(tracker.color))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${tracker.serviceName} • ${tracker.status.name} (${tracker.score})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Chapters Header with Download All
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${mangaChapters.size} CHAPTERS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )

                    IconButton(
                        onClick = { onDownloadAll(manga.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download All",
                            tint = LuminaCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Chapters List
            items(mangaChapters, key = { it.id }) { chapter ->
                ChapterRowItem(
                    chapter = chapter,
                    onRead = { onReadChapter(manga.id, chapter.id) },
                    onToggleRead = { onMarkChapterRead(chapter.id, !chapter.isRead) },
                    onToggleDownload = { onToggleDownload(chapter.id) }
                )
            }
        }
    }
}

@Composable
private fun ChapterRowItem(
    chapter: Chapter,
    onRead: () -> Unit,
    onToggleRead: () -> Unit,
    onToggleDownload: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onRead() },
        color = if (chapter.isRead) LuminaBlack else LuminaSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (chapter.isRead) TextMuted else TextPrimary,
                    fontWeight = if (chapter.isRead) FontWeight.Normal else FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${chapter.dateUpload} • ${chapter.scanlator} • ${chapter.pageCount} pages",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = onToggleDownload,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (chapter.isDownloaded) Icons.Default.CheckCircle else Icons.Default.DownloadForOffline,
                    contentDescription = "Download",
                    tint = if (chapter.isDownloaded) LuminaCyan else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onToggleRead,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (chapter.isRead) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Mark Read",
                    tint = if (chapter.isRead) LuminaVioletLight else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
