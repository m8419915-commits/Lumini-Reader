package com.example.ui.updates

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.domain.model.MangaUpdateItem
import com.example.ui.theme.*

@Composable
fun UpdatesScreen(
    updates: List<MangaUpdateItem>,
    onDownloadUpdate: (String) -> Unit,
    onDownloadAll: () -> Unit,
    onOpenManga: (Long) -> Unit
) {
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

                IconButton(onClick = { /* Search */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pill notification
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LuminaVioletPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletSecondary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = LuminaVioletSecondary, modifier = Modifier.size(18.dp))
                        Text(
                            text = "${updates.count { !it.isDownloaded }} new chapters available",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Updates Headline & Download All
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Updates",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onDownloadAll,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LuminaSurfaceVariant)
                            .border(1.dp, LuminaBorder, CircleShape)
                            .testTag("download_all_updates_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DownloadForOffline,
                            contentDescription = "Download All",
                            tint = LuminaCyanAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Updates List
            items(updates, key = { it.id }) { item ->
                UpdateCard(
                    item = item,
                    onDownload = { onDownloadUpdate(item.id) },
                    onClick = { onOpenManga(item.mangaId) }
                )
            }
        }
    }
}

@Composable
fun UpdateCard(
    item: MangaUpdateItem,
    onDownload: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = LuminaSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.coverUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .width(60.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${item.chapterDisplay} • ${item.timeAgo}",
                    style = MaterialTheme.typography.labelMedium,
                    color = LuminaVioletSecondary
                )
            }

            IconButton(
                onClick = onDownload,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (item.isDownloaded) LuminaEmerald.copy(alpha = 0.2f) else LuminaBorder.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = if (item.isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                    contentDescription = if (item.isDownloaded) "Downloaded" else "Download",
                    tint = if (item.isDownloaded) LuminaEmerald else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
