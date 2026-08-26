package com.example.ui.explore

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ExtensionPackage
import com.example.domain.model.Manga
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    availableExtensions: List<ExtensionPackage>,
    installedExtensions: Set<String>,
    allMangas: List<Manga>,
    repoUrl: String,
    isLoading: Boolean,
    errorMessage: String?,
    onRepoUrlChange: (String) -> Unit,
    onFetchRepo: () -> Unit,
    onToggleExtension: (ExtensionPackage) -> Unit,
    onOpenManga: (Long) -> Unit,
    onOpenRepositories: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Sources, 1 = Extensions
    var selectedLang by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val languages = listOf("All", "en", "ja", "ko", "zh", "es", "fr")

    val filteredExtensions = remember(availableExtensions, selectedLang, searchQuery) {
        availableExtensions.filter { ext ->
            val matchesLang = selectedLang == "All" || ext.lang.equals(selectedLang, ignoreCase = true) || ext.lang == "all"
            val matchesSearch = searchQuery.isBlank() || ext.name.contains(searchQuery, ignoreCase = true) || ext.packageName.contains(searchQuery, ignoreCase = true)
            matchesLang && matchesSearch
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
                        IconButton(onClick = onOpenRepositories, modifier = Modifier.testTag("open_repo_mgr_btn")) {
                            Icon(Icons.Default.Tune, contentDescription = "Manage Repositories", tint = Color.White)
                        }
                        IconButton(onClick = { /* Search */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                        }
                    }
                }

                // TAB SWITCHER (Sources vs Extensions)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LuminaSurfaceVariant)
                        .padding(4.dp)
                ) {
                    TabButton(
                        text = "Sources",
                        isSelected = selectedTab == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 0 }
                    )
                    TabButton(
                        text = "Extensions",
                        isSelected = selectedTab == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 1 }
                    )
                }
            }
        }
    ) { padding ->
        if (selectedTab == 0) {
            // SOURCES TAB (Matching browse_extensions_screen.png)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "INSTALLED SOURCES",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaCyanAccent,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Source 1: MangaDex
                item {
                    SourceCard(
                        title = "MangaDex",
                        subtitle = "EN • Multi • v1.4.2 [Latest]",
                        iconVector = Icons.Default.Public,
                        iconTint = LuminaVioletSecondary,
                        statusBadge = "ONLINE",
                        onClick = { onOpenManga(1L) }
                    )
                }

                // Source 2: Keiyoushi
                item {
                    SourceCard(
                        title = "Keiyoushi",
                        subtitle = "EN • Repository • v2.0.1 [Latest]",
                        iconVector = Icons.Default.Extension,
                        iconTint = LuminaCyanAccent,
                        statusBadge = "ACTIVE",
                        onClick = { selectedTab = 1 }
                    )
                }

                // Source 3: Local Storage
                item {
                    SourceCard(
                        title = "Local Storage",
                        subtitle = "Device • System (CBZ, CBR, EPUB)",
                        iconVector = Icons.Default.Folder,
                        iconTint = LuminaAmberGlow,
                        statusBadge = "READY",
                        onClick = { /* Browse Local */ }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenRepositories() },
                        shape = RoundedCornerShape(16.dp),
                        color = LuminaSurfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.CloudSync, contentDescription = null, tint = LuminaVioletSecondary)
                                Column {
                                    Text("Repository Manager", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Manage ProtoBuf & Keiyoushi endpoints", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                        }
                    }
                }
            }
        } else {
            // EXTENSIONS TAB
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    // Language filters
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(languages) { lang ->
                            val isSelected = selectedLang == lang
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) LuminaVioletPrimary.copy(alpha = 0.3f) else LuminaSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) LuminaVioletSecondary else LuminaBorder
                                ),
                                modifier = Modifier.clickable { selectedLang = lang }
                            ) {
                                Text(
                                    text = lang.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                if (availableExtensions.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = LuminaSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("No extensions loaded yet", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Sync with Keiyoushi index.pb repository to discover hundreds of sources.", color = TextSecondary, fontSize = 12.sp)
                                Button(
                                    onClick = { onFetchRepo() },
                                    colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary)
                                ) {
                                    Text(if (isLoading) "Syncing..." else "Fetch Extensions Now")
                                }
                            }
                        }
                    }
                } else {
                    items(filteredExtensions, key = { it.packageName }) { pkg ->
                        val isInstalled = installedExtensions.contains(pkg.packageName)
                        ExtensionItemCard(
                            pkg = pkg,
                            isInstalled = isInstalled,
                            onToggle = { onToggleExtension(pkg) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) LuminaVioletPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.White else TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun SourceCard(
    title: String,
    subtitle: String,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    statusBadge: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = LuminaSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f))
                        .border(1.dp, iconTint.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(iconVector, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LuminaBorder.copy(alpha = 0.5f)
            ) {
                Text(
                    text = statusBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyanAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ExtensionItemCard(
    pkg: ExtensionPackage,
    isInstalled: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = LuminaSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = pkg.name, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LuminaVioletPrimary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = pkg.lang.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaVioletSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(text = "v${pkg.versionName} • ${pkg.packageName}", style = MaterialTheme.typography.labelSmall, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInstalled) LuminaRoseAccent.copy(alpha = 0.2f) else LuminaVioletPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                border = if (isInstalled) androidx.compose.foundation.BorderStroke(1.dp, LuminaRoseAccent) else null
            ) {
                Text(
                    text = if (isInstalled) "Uninstall" else "Install",
                    color = if (isInstalled) LuminaRoseAccent else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
