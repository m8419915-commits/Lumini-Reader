package com.example.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Manga
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    libraryMangas: List<Manga>,
    allMangas: List<Manga>,
    searchQuery: String,
    selectedGenre: String,
    onSearchChange: (String) -> Unit,
    onGenreSelect: (String) -> Unit,
    onOpenManga: (Long) -> Unit,
    onOpenExplore: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Reading", "Completed", "On Hold")

    val mangasToShow = remember(libraryMangas, allMangas, searchQuery, selectedCategory) {
        val base = if (libraryMangas.isNotEmpty()) libraryMangas else allMangas
        base.filter { manga ->
            val matchesSearch = searchQuery.isBlank() ||
                    manga.title.contains(searchQuery, ignoreCase = true) ||
                    manga.author.contains(searchQuery, ignoreCase = true)
            val matchesCategory = when (selectedCategory) {
                "Reading" -> manga.id % 2 == 1L
                "Completed" -> manga.id % 2 == 0L
                "On Hold" -> manga.id == 4L
                else -> true
            }
            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LuminaSurfaceVariant)
                                .border(1.dp, LuminaVioletPrimary.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = LuminaVioletSecondary,
                                modifier = Modifier.size(22.dp)
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
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(onClick = { /* Filter */ }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.White)
                        }
                        IconButton(onClick = { /* Search */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                        }
                    }
                }

                // Category Filter Pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) LuminaVioletPrimary.copy(alpha = 0.35f) else LuminaSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) LuminaVioletSecondary else LuminaBorder
                            ),
                            modifier = Modifier
                                .clickable { selectedCategory = category }
                                .testTag("cat_tab_$category")
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (mangasToShow.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CollectionsBookmark,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Text("No manga found in this category", color = TextSecondary)
                    Button(
                        onClick = onOpenExplore,
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary)
                    ) {
                        Text("Explore Manga Catalog")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(mangasToShow, key = { it.id }) { manga ->
                    LibraryMangaCard(manga = manga, onClick = { onOpenManga(manga.id) })
                }
            }
        }
    }
}

@Composable
fun LibraryMangaCard(
    manga: Manga,
    onClick: () -> Unit
) {
    // Custom dynamic badges and progress based on ID for rich aesthetic matching screenshot
    val newBadge = when (manga.id) {
        1L -> "3 New"
        3L -> "12 New"
        else -> null
    }

    val subtitle = when (manga.id) {
        1L -> "Ch. 142 • 5 days ago"
        2L -> "Ch. 56 • Read"
        3L -> "Ch. 21 • 2 weeks ago"
        4L -> "Ch. 8 • On Hold"
        else -> "Ch. 12 • Active"
    }

    val progress = when (manga.id) {
        1L -> 0.75f
        2L -> 1.0f
        3L -> 0.40f
        4L -> 0.20f
        else -> 0.50f
    }

    val progressColor = when (manga.id) {
        2L -> LuminaEmerald
        else -> LuminaVioletPrimary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LuminaSurfaceVariant)
            .border(1.dp, LuminaBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("library_card_${manga.id}")
    ) {
        AsyncImage(
            model = manga.thumbnailUrl,
            contentDescription = manga.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f),
                            LuminaBlack.copy(alpha = 0.95f)
                        ),
                        startY = 80f
                    )
                )
        )

        // New Chapters Badge (e.g. "3 New", "12 New")
        if (newBadge != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                color = LuminaVioletPrimary,
                border = androidx.compose.foundation.BorderStroke(0.8.dp, LuminaVioletSecondary)
            ) {
                Text(
                    text = newBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
        }

        // Bottom Details & Progress Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = manga.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Reading Progress Line
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape),
                color = progressColor,
                trackColor = LuminaBorder
            )
        }
    }
}
