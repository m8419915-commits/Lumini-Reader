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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.luminareader.data.model.MangaUpdateItem
import com.example.luminareader.ui.theme.*

@Composable
fun UpdatesScreen(
    updates: List<MangaUpdateItem>,
    onMangaClick: (Int) -> Unit,
    onDownloadUpdate: (String) -> Unit
) {
    Scaffold(
        topBar = {
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Updates",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${updates.size} new chapters detected",
                            style = MaterialTheme.typography.bodySmall,
                            color = LuminaCyan
                        )
                    }

                    IconButton(
                        onClick = { /* batch download */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LuminaSurfaceVariant)
                            .testTag("updates_download_all")
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
        },
        containerColor = LuminaBlack
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("updates_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(updates, key = { it.id }) { update ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp))
                        .clickable { onMangaClick(update.mangaId) },
                    color = LuminaSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = update.coverUrl,
                            contentDescription = update.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 50.dp, height = 70.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = update.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = update.chapterDisplay,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LuminaVioletLight,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = update.timeAgo,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        IconButton(
                            onClick = { onDownloadUpdate(update.id) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (update.isDownloaded) Icons.Default.CheckCircle else Icons.Default.DownloadForOffline,
                                contentDescription = "Download",
                                tint = if (update.isDownloaded) LuminaCyan else TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
