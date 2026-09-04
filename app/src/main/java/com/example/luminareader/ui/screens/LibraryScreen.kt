package com.example.luminareader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.luminareader.data.model.*
import com.example.luminareader.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    mangas: List<Manga>,
    categories: List<Category>,
    libraryFilters: LibraryFilters,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onFilterChange: (LibraryFilters) -> Unit,
    onMangaClick: (Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showFilterSheet by remember { mutableStateOf(false) }

    val libraryMangas = mangas.filter { it.inLibrary }

    val filteredMangas = libraryMangas.filter { manga ->
        val matchesCategory = selectedCategory == "All" || manga.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                manga.title.contains(searchQuery, ignoreCase = true) ||
                manga.author.contains(searchQuery, ignoreCase = true) ||
                manga.genre.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesUnread = when (libraryFilters.unread) {
            TriState.INCLUDED -> manga.unreadCount > 0
            TriState.EXCLUDED -> manga.unreadCount == 0
            else -> true
        }

        matchesCategory && matchesSearch && matchesUnread
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LuminaBlack)
                    .statusBarsPadding()
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Library",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row {
                        IconButton(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(LuminaSurfaceVariant)
                                .testTag("library_filter_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = LuminaCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("library_search_input"),
                    placeholder = { Text("Search your titles, authors, genres...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = LuminaSurfaceVariant,
                        unfocusedContainerColor = LuminaSurfaceVariant,
                        focusedBorderColor = LuminaViolet,
                        unfocusedBorderColor = LuminaBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                // Category Chips Row
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOfFirst { it.name.equals(selectedCategory, ignoreCase = true) }.coerceAtLeast(0),
                    containerColor = LuminaBlack,
                    contentColor = LuminaCyan,
                    edgePadding = 16.dp,
                    indicator = {},
                    divider = {}
                ) {
                    categories.forEach { category ->
                        val isSelected = category.name.equals(selectedCategory, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) LuminaCyan else LuminaBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedCategory = category.name },
                            color = if (isSelected) LuminaViolet.copy(alpha = 0.25f) else LuminaSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) LuminaCyan else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = LuminaBlack
    ) { paddingValues ->
        if (filteredMangas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) "No matches found" else "No manga in this category",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "Browse Explore to add titles to your Library",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 110.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("library_grid"),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredMangas, key = { it.id }) { manga ->
                    LibraryGridItem(
                        manga = manga,
                        onClick = { onMangaClick(manga.id) }
                    )
                }
            }
        }

        // Filter Bottom Sheet
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                containerColor = LuminaCard,
                dragHandle = { BottomSheetDefaults.DragHandle(color = LuminaBorder) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Filter & Sort Library",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "UNREAD FILTER", style = MaterialTheme.typography.bodySmall, color = LuminaCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TriStateButton("All", libraryFilters.unread == TriState.NONE) {
                            onFilterChange(libraryFilters.copy(unread = TriState.NONE))
                        }
                        TriStateButton("Unread Only", libraryFilters.unread == TriState.INCLUDED) {
                            onFilterChange(libraryFilters.copy(unread = TriState.INCLUDED))
                        }
                        TriStateButton("Completed Only", libraryFilters.unread == TriState.EXCLUDED) {
                            onFilterChange(libraryFilters.copy(unread = TriState.EXCLUDED))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "SORT BY", style = MaterialTheme.typography.bodySmall, color = LuminaVioletLight, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TriStateButton("A-Z", libraryFilters.sortBy == "alphabetical") {
                            onFilterChange(libraryFilters.copy(sortBy = "alphabetical"))
                        }
                        TriStateButton("Rating", libraryFilters.sortBy == "rating") {
                            onFilterChange(libraryFilters.copy(sortBy = "rating"))
                        }
                        TriStateButton("Chapters", libraryFilters.sortBy == "totalChapters") {
                            onFilterChange(libraryFilters.copy(sortBy = "totalChapters"))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showFilterSheet = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet)
                    ) {
                        Text("Apply Filters")
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun TriStateButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, if (isSelected) LuminaCyan else LuminaBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = if (isSelected) LuminaViolet.copy(alpha = 0.3f) else LuminaSurfaceVariant
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) LuminaCyan else TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun LibraryGridItem(
    manga: Manga,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, LuminaBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = LuminaSurfaceVariant)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
            ) {
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
                            text = "${manga.unreadCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.BottomEnd),
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = LuminaAmber, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "%.1f".format(manga.rating),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontSize = 9.sp
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
                    maxLines = 2,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = manga.source,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
