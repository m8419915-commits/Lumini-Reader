package com.example.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Chapter
import com.example.domain.model.Manga
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    manga: Manga?,
    chapters: List<Chapter>,
    onBack: () -> Unit,
    onOpenChapter: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onToggleBookmark: (Long) -> Unit,
    onDownloadChapter: (Long) -> Unit,
    onDownloadAll: (Long) -> Unit,
    onOpenUniverseMap: (Long) -> Unit,
    onOpenMangaDna: (Long) -> Unit,
    onOpenTimeline: (Long) -> Unit = {}
) {
    if (manga == null) {
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

    var isDescExpanded by remember { mutableStateOf(false) }
    var chapterSearch by remember { mutableStateOf("") }
    var sortDescending by remember { mutableStateOf(true) }

    val filteredChapters = remember(chapters, chapterSearch, sortDescending) {
        val filtered = if (chapterSearch.isBlank()) {
            chapters
        } else {
            chapters.filter { it.name.contains(chapterSearch, ignoreCase = true) || it.chapterNumber.toString().contains(chapterSearch) }
        }
        if (sortDescending) filtered.sortedByDescending { it.chapterNumber } else filtered.sortedBy { it.chapterNumber }
    }

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.55f))
                            .testTag("detail_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onToggleFavorite(manga.id) },
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.55f))
                            .testTag("detail_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (manga.favorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (manga.favorite) LuminaRoseAccent else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LuminaBlack),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // HERO BANNER & POSTER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    AsyncImage(
                        model = manga.thumbnailUrl,
                        contentDescription = manga.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LuminaAmbientOverlay)
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        AsyncImage(
                            model = manga.thumbnailUrl,
                            contentDescription = manga.title,
                            modifier = Modifier
                                .width(110.dp)
                                .height(155.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, LuminaVioletPrimary, RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = LuminaSurfaceVariant
                            ) {
                                Text(
                                    text = manga.sourceName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaCyanAccent,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = manga.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "${manga.author} • ${manga.artist}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 1
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = LuminaAmberGlow, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "%.2f Rating".format(manga.rating),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "• Ongoing",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaEmerald
                                )
                            }
                        }
                    }
                }
            }

            // PRIMARY ACTION ROW
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val firstCh = chapters.firstOrNull()?.id ?: 101L
                            onOpenChapter(firstCh)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("start_reading_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Reading", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    OutlinedButton(
                        onClick = { onOpenUniverseMap(manga.id) },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("open_universe_map_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LuminaCyanAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaCyanAccent.copy(alpha = 0.6f))
                    ) {
                        Icon(Icons.Default.Hub, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Universe", fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = { onOpenMangaDna(manga.id) },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("open_manga_dna_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LuminaAmberGlow),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaAmberGlow.copy(alpha = 0.6f))
                    ) {
                        Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DNA", fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = { onOpenTimeline(manga.id) },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("open_timeline_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LuminaRoseAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaRoseAccent.copy(alpha = 0.6f))
                    ) {
                        Icon(Icons.Default.Timeline, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // SYNOPSIS & GENRES
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SYNOPSIS",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = manga.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        maxLines = if (isDescExpanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { isDescExpanded = !isDescExpanded }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        manga.genres.forEach { genre ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = LuminaSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                            ) {
                                Text(
                                    text = genre,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaVioletSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // CHAPTER LIST HEADER & SEARCH/SORT
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHAPTERS (${chapters.size})",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(onClick = { onDownloadAll(manga.id) }) {
                                Icon(Icons.Outlined.Download, contentDescription = "Download All", tint = LuminaCyanAccent)
                            }
                            IconButton(onClick = { sortDescending = !sortDescending }) {
                                Icon(Icons.Default.Sort, contentDescription = "Sort", tint = Color.White)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = chapterSearch,
                        onValueChange = { chapterSearch = it },
                        placeholder = { Text("Filter chapters...", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("chapter_search_input"),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                        trailingIcon = {
                            if (chapterSearch.isNotEmpty()) {
                                IconButton(onClick = { chapterSearch = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LuminaSurfaceVariant,
                            unfocusedContainerColor = LuminaSurfaceVariant,
                            focusedBorderColor = LuminaVioletPrimary,
                            unfocusedBorderColor = LuminaBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // CHAPTER ITEMS
            items(filteredChapters, key = { it.id }) { chapter ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenChapter(chapter.id) }
                        .testTag("chapter_item_${chapter.id}"),
                    color = if (chapter.read) LuminaSurface.copy(alpha = 0.6f) else LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (chapter.bookmark) LuminaAmberGlow.copy(alpha = 0.5f) else LuminaBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = chapter.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (chapter.read) TextMuted else Color.White,
                                fontWeight = if (chapter.read) FontWeight.Normal else FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${chapter.scanlator} • ${chapter.totalPages} Pages",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (chapter.isDownloaded) LuminaEmerald else TextSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(onClick = { onToggleBookmark(chapter.id) }) {
                                Icon(
                                    imageVector = if (chapter.bookmark) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (chapter.bookmark) LuminaAmberGlow else TextMuted
                                )
                            }

                            IconButton(onClick = { onDownloadChapter(chapter.id) }) {
                                Icon(
                                    imageVector = if (chapter.isDownloaded) Icons.Default.CheckCircle else Icons.Outlined.Download,
                                    contentDescription = "Download",
                                    tint = if (chapter.isDownloaded) LuminaEmerald else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
