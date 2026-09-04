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

@Composable
fun ExploreScreen(
    sources: List<SourceMeta>,
    extensions: List<ExtensionPackage>,
    migrationItems: List<SourceMigrationItem>,
    onToggleExtensionInstall: (String) -> Unit,
    onBrowseSource: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Sources", "Extensions", "Migration")

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = LuminaBlack
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Explore",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Surface(
                            color = LuminaSurfaceVariant,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(LuminaEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Keiyoushi Connected", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    // Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = LuminaBlack,
                        contentColor = LuminaCyan,
                        indicator = {},
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val isSelected = selectedTab == index
                            Tab(
                                selected = isSelected,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        color = if (isSelected) LuminaCyan else TextMuted,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },
        containerColor = LuminaBlack
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("explore_screen")
        ) {
            when (selectedTab) {
                0 -> SourcesTabContent(sources = sources, onBrowseSource = onBrowseSource)
                1 -> ExtensionsTabContent(extensions = extensions, onToggleInstall = onToggleExtensionInstall)
                2 -> MigrationTabContent(migrationItems = migrationItems)
            }
        }
    }
}

@Composable
private fun SourcesTabContent(
    sources: List<SourceMeta>,
    onBrowseSource: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(sources, key = { it.id }) { source ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp))
                    .clickable { onBrowseSource(source.id) },
                color = LuminaSurfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LuminaCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = LuminaCyan, modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = source.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text(
                            text = "${source.lang.uppercase()} • ${source.itemCount} titles available",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    if (source.isPinned) {
                        Icon(Icons.Default.PushPin, contentDescription = "Pinned", tint = LuminaAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Button(
                        onClick = { onBrowseSource(source.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Browse", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExtensionsTabContent(
    extensions: List<ExtensionPackage>,
    onToggleInstall: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(extensions, key = { it.packageName }) { ext ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp)),
                color = LuminaSurfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LuminaCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Extension, contentDescription = null, tint = LuminaVioletLight, modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = ext.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = LuminaCard,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = ext.lang.uppercase(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LuminaCyan,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "v${ext.versionName} • ${ext.repoName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    if (ext.isInstalled) {
                        if (ext.hasUpdate) {
                            Button(
                                onClick = { onToggleInstall(ext.packageName) },
                                colors = ButtonDefaults.buttonColors(containerColor = LuminaAmber),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Update", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            OutlinedButton(
                                onClick = { onToggleInstall(ext.packageName) },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Installed", fontSize = 11.sp, color = LuminaEmerald)
                            }
                        }
                    } else {
                        Button(
                            onClick = { onToggleInstall(ext.packageName) },
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Install", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MigrationTabContent(
    migrationItems: List<SourceMigrationItem>
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = LuminaCard
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = LuminaCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Migrate your titles seamlessly when sources shut down or update domains. Reading progress is preserved 1:1.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        items(migrationItems, key = { it.id }) { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp)),
                color = LuminaSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.mangaCover,
                            contentDescription = item.mangaTitle,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 46.dp, height = 64.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.mangaTitle, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "From: ${item.fromSourceName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "To: ${item.toSourceName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = LuminaCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = LuminaEmerald.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${item.matchScore}% Match",
                                color = LuminaEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* migration */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("1-Tap Migrate (${item.targetChapterCount} Chapters Available)", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
