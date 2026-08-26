package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Manga
import com.example.domain.model.ReadingSnap
import com.example.ui.theme.*

@Composable
fun HomeDashboardScreen(
    currentStreak: Int,
    snaps: List<ReadingSnap>,
    allMangas: List<Manga>,
    onOpenSnap: (ReadingSnap) -> Unit,
    onOpenManga: (Long) -> Unit,
    onOpenExplore: () -> Unit,
    onOpenSnapSwitcher: () -> Unit,
    onOpenSearch: () -> Unit = {}
) {
    val featuredManga = allMangas.firstOrNull() ?: Manga(
        id = 1L,
        sourceId = 101L,
        url = "/manga/neon-genesis-requiem",
        title = "Neon Genesis: Requiem",
        author = "Kaito & Ren",
        description = "The sprawling metropolis holds secrets deeper than the neon lights reach. Kaito must navigate forbidden ocular resonance tech.",
        genres = listOf("Cyberpunk", "Action", "Psychological"),
        thumbnailUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80"
    )

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LuminaSurfaceVariant)
                            .border(1.dp, LuminaVioletPrimary.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = LuminaVioletSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Lumina",
                        style = MaterialTheme.typography.titleLarge,
                        color = LuminaVioletSecondary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Reading Streak Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = LuminaSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaAmberGlow.copy(alpha = 0.5f)),
                        modifier = Modifier.testTag("streak_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Reading Streak",
                                tint = LuminaAmberGlow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "$currentStreak Days",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onOpenSearch,
                        modifier = Modifier.testTag("home_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // HERO FEATURED BANNER (Matching Screenshot: Neon Genesis Requiem)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(LuminaSurfaceVariant)
                        .border(1.dp, LuminaBorder, RoundedCornerShape(24.dp))
                ) {
                    AsyncImage(
                        model = featuredManga.thumbnailUrl,
                        contentDescription = featuredManga.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Cinematic Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.6f),
                                        LuminaBlack.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Badge Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LuminaVioletPrimary.copy(alpha = 0.25f),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, LuminaVioletSecondary)
                            ) {
                                Text(
                                    text = "Continue Reading",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaCyanAccent,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Chapter 42",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = featuredManga.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = featuredManga.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Action Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onOpenManga(featuredManga.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("hero_read_now_btn")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Read Now", fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            FilledTonalButton(
                                onClick = { onOpenManga(featuredManga.id) },
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = LuminaSurface.copy(alpha = 0.8f)),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Details", color = Color.White)
                            }
                        }
                    }
                }
            }

            // LUMINA SNAP 1-SEC STATE RESTORATION CAROUSEL
            if (snaps.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = LuminaCyanAccent, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "LUMINA SNAP (1-SEC RESUME)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaCyanAccent,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = "Switcher",
                                style = MaterialTheme.typography.labelSmall,
                                color = LuminaVioletSecondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onOpenSnapSwitcher() }
                            )
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(snaps, key = { it.mangaId }) { snap ->
                                SnapResumeCard(snap = snap, onOpenSnap = { onOpenSnap(snap) })
                            }
                        }
                    }
                }
            }

            // RECENTLY ADDED (Matching screenshot)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recently Added",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onOpenExplore() }
                        ) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelSmall,
                                color = LuminaVioletSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = LuminaVioletSecondary, modifier = Modifier.size(16.dp))
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(allMangas, key = { it.id }) { manga ->
                            RecentlyAddedMangaCard(manga = manga, onClick = { onOpenManga(manga.id) })
                        }
                    }
                }
            }

            // CONTINUE READING ROW
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Continue Reading",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(allMangas.take(4), key = { it.id }) { manga ->
                            ContinueReadingCard(
                                manga = manga,
                                progress = 0.65f,
                                onClick = { onOpenManga(manga.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentlyAddedMangaCard(
    manga: Manga,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(135.dp)
            .height(190.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LuminaSurfaceVariant)
            .border(1.dp, LuminaBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("recent_manga_${manga.id}")
    ) {
        AsyncImage(
            model = manga.thumbnailUrl,
            contentDescription = manga.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient & Badge
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        ),
                        startY = 100f
                    )
                )
        )

        // NEW Pill
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            color = LuminaBlack.copy(alpha = 0.7f),
            border = androidx.compose.foundation.BorderStroke(0.8.dp, LuminaCyanAccent)
        ) {
            Text(
                text = "NEW",
                style = MaterialTheme.typography.labelSmall,
                color = LuminaCyanAccent,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = manga.title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = manga.genres.firstOrNull() ?: "Manga",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun SnapResumeCard(
    snap: ReadingSnap,
    onOpenSnap: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(220.dp)
            .height(105.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenSnap() },
        color = LuminaSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletPrimary.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = snap.coverUrl,
                contentDescription = snap.title,
                modifier = Modifier
                    .width(55.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = snap.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Ch. ${snap.chapterNumber} • P. ${snap.pageIndex + 1}",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyanAccent
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { snap.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(CircleShape),
                    color = LuminaVioletPrimary,
                    trackColor = LuminaBorder
                )
            }
        }
    }
}

@Composable
fun ContinueReadingCard(
    manga: Manga,
    progress: Float,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(135.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(135.dp)
                .height(185.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LuminaSurfaceVariant)
                .border(1.dp, LuminaBorder, RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = manga.thumbnailUrl,
                contentDescription = manga.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.BottomCenter),
                color = LuminaCyanAccent,
                trackColor = Color.Black.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = manga.title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
