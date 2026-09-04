package com.example.luminareader.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.luminareader.data.model.*
import com.example.luminareader.ui.theme.*

@Composable
fun HomeScreen(
    mangas: List<Manga>,
    snaps: List<ReadingSnap>,
    updates: List<MangaUpdateItem>,
    onNavigate: (ScreenType, Int?, Int?) -> Unit,
    onOpenSnapSwitcher: () -> Unit,
    onOpenAi: () -> Unit
) {
    val libraryMangas = mangas.filter { it.inLibrary }
    val latestSnap = snaps.firstOrNull()

    Scaffold(
        topBar = {
            HomeTopBar(
                onOpenSnapSwitcher = onOpenSnapSwitcher,
                onOpenAi = onOpenAi,
                onOpenSearch = { onNavigate(ScreenType.LIBRARY, null, null) }
            )
        },
        containerColor = LuminaBlack
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("home_screen"),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Hero Continue Reading Card
            if (latestSnap != null) {
                item {
                    HeroReadingCard(
                        snap = latestSnap,
                        onResume = { onNavigate(ScreenType.READER, latestSnap.mangaId, latestSnap.chapterId) }
                    )
                }
            }

            // Telemetry & Stats Row
            item {
                TelemetryStatsRow(
                    speedPpm = 3.2f,
                    streakDays = 5,
                    totalMinutes = 480
                )
            }

            // Quick Portals Row
            item {
                QuickPortalsRow(
                    onUniverse = { onNavigate(ScreenType.UNIVERSE, null, null) },
                    onDna = { onNavigate(ScreenType.DNA, null, null) },
                    onForge = { onNavigate(ScreenType.FORGE, null, null) },
                    onAi = onOpenAi
                )
            }

            // Recent Updates Section
            item {
                SectionHeader(
                    title = "Recent Chapter Drops",
                    subtitle = "Real-time extension sync",
                    onSeeAll = { onNavigate(ScreenType.UPDATES, null, null) }
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(updates, key = { it.id }) { update ->
                        UpdatePreviewCard(
                            update = update,
                            onClick = { onNavigate(ScreenType.DETAIL, update.mangaId, null) }
                        )
                    }
                }
            }

            // In Your Library Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Jump Back In",
                    subtitle = "Active in your collection",
                    onSeeAll = { onNavigate(ScreenType.LIBRARY, null, null) }
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(libraryMangas, key = { it.id }) { manga ->
                        MangaPosterCard(
                            manga = manga,
                            onClick = { onNavigate(ScreenType.DETAIL, manga.id, null) }
                        )
                    }
                }
            }

            // Recommended Picks Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Curated Discoveries",
                    subtitle = "Matches your Manga DNA",
                    onSeeAll = null
                )
            }

            items(mangas.take(3), key = { "rec_${it.id}" }) { manga ->
                RecommendedMangaRowItem(
                    manga = manga,
                    onClick = { onNavigate(ScreenType.DETAIL, manga.id, null) }
                )
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    onOpenSnapSwitcher: () -> Unit,
    onOpenAi: () -> Unit,
    onOpenSearch: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = LuminaBlack
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(LuminaCyan)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LUMINA",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = TextPrimary
                )
                Text(
                    text = " NOIR",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 2.sp,
                    color = LuminaVioletLight
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenSearch,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(LuminaSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onOpenSnapSwitcher,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(LuminaViolet.copy(alpha = 0.3f), LuminaCyan.copy(alpha = 0.3f)))
                        )
                        .border(1.dp, LuminaViolet, CircleShape)
                        .testTag("home_snap_switcher_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Lumina Snap",
                        tint = LuminaCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onOpenAi,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(LuminaSurfaceVariant)
                        .border(1.dp, LuminaBorder, CircleShape)
                        .testTag("home_ai_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Lumina AI",
                        tint = LuminaVioletLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroReadingCard(
    snap: ReadingSnap,
    onResume: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, LuminaViolet.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .clickable { onResume() }
            .testTag("hero_reading_card"),
        colors = CardDefaults.cardColors(containerColor = LuminaCard)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = snap.coverUrl,
                contentDescription = snap.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                LuminaCard.copy(alpha = 0.8f),
                                LuminaCard
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = LuminaViolet.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "CONTINUE READING",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${snap.progressPercent}% Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = LuminaCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = snap.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    maxLines = 1
                )

                Text(
                    text = "Chapter ${snap.chapterNumber.toInt()} • Page ${snap.pageIndex + 1} of ${snap.totalPages}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { snap.progressPercent / 100f },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = LuminaCyan,
                        trackColor = LuminaBorder
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onResume,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuminaViolet,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Resume", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryStatsRow(
    speedPpm: Float,
    streakDays: Int,
    totalMinutes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatBadge(
            label = "VELOCITY",
            value = "$speedPpm PPM",
            icon = Icons.Default.Speed,
            color = LuminaCyan,
            modifier = Modifier.weight(1f)
        )
        StatBadge(
            label = "STREAK",
            value = "$streakDays Days",
            icon = Icons.Default.LocalFireDepartment,
            color = LuminaAmber,
            modifier = Modifier.weight(1f)
        )
        StatBadge(
            label = "IMMERSION",
            value = "${totalMinutes / 60}h ${totalMinutes % 60}m",
            icon = Icons.Default.AccessTime,
            color = LuminaVioletLight,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatBadge(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp)),
        color = LuminaSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 9.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuickPortalsRow(
    onUniverse: () -> Unit,
    onDna: () -> Unit,
    onForge: () -> Unit,
    onAi: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PortalPill("Universe", Icons.Default.Hub, LuminaCyan, Modifier.weight(1f), onUniverse)
        PortalPill("DNA", Icons.Default.Fingerprint, LuminaViolet, Modifier.weight(1f), onDna)
        PortalPill("Forge", Icons.Default.Tune, LuminaAmber, Modifier.weight(1f), onForge)
        PortalPill("Copilot", Icons.Default.AutoAwesome, LuminaEmerald, Modifier.weight(1f), onAi)
    }
}

@Composable
private fun PortalPill(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = LuminaCard
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAll: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }

        if (onSeeAll != null) {
            TextButton(
                onClick = onSeeAll,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyan
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = LuminaCyan,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun UpdatePreviewCard(
    update: MangaUpdateItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LuminaCard)
    ) {
        Column {
            Box(modifier = Modifier.height(100.dp)) {
                AsyncImage(
                    model = update.coverUrl,
                    contentDescription = update.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopEnd),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = update.timeAgo,
                        style = MaterialTheme.typography.bodySmall,
                        color = LuminaCyan,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = update.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = update.chapterDisplay,
                    style = MaterialTheme.typography.bodySmall,
                    color = LuminaVioletLight,
                    maxLines = 1,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun MangaPosterCard(
    manga: Manga,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, LuminaBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LuminaSurfaceVariant)
    ) {
        Column {
            Box(modifier = Modifier.height(150.dp)) {
                AsyncImage(
                    model = manga.thumbnailUrl,
                    contentDescription = manga.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (manga.unreadCount > 0) {
                    Surface(
                        modifier = Modifier
                            .padding(6.dp)
                            .align(Alignment.TopStart),
                        color = LuminaViolet,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${manga.unreadCount} unread",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${manga.totalChapters} chapters",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun RecommendedMangaRowItem(
    manga: Manga,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = LuminaCard
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = manga.thumbnailUrl,
                contentDescription = manga.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 54.dp, height = 76.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${manga.author} • ${manga.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    manga.genre.take(2).forEach { genre ->
                        Surface(
                            color = LuminaSurfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = LuminaCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = LuminaAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "%.1f".format(manga.rating),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
