package com.example.ui.backup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun BackupSyncScreen(
    syncLibrary: Boolean,
    syncProgress: Boolean,
    syncCategories: Boolean,
    syncSettings: Boolean,
    lastBackupTime: String,
    onToggleSync: (String, Boolean) -> Unit,
    onSyncNow: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("backup_back_btn")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Backup & Sync",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
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
            // AUTO SYNC ACTIVE HERO CARD
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaCyanAccent.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Auto-Sync is Active",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "☁ Last backup: $lastBackupTime",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = onSyncNow,
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sync Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // DATA TO SYNC
            item {
                Text(
                    text = "DATA TO SYNC",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyanAccent,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        SyncToggleRow(
                            icon = Icons.Default.CollectionsBookmark,
                            title = "Library",
                            subtitle = "Titles, metadata, and custom tags",
                            checked = syncLibrary,
                            onCheckedChange = { onToggleSync("library", it) }
                        )

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        SyncToggleRow(
                            icon = Icons.Default.History,
                            title = "Reading Progress",
                            subtitle = "Bookmarks, chapters, and history",
                            checked = syncProgress,
                            onCheckedChange = { onToggleSync("progress", it) }
                        )

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        SyncToggleRow(
                            icon = Icons.Default.Folder,
                            title = "Categories",
                            subtitle = "Custom library organization",
                            checked = syncCategories,
                            onCheckedChange = { onToggleSync("categories", it) }
                        )

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        SyncToggleRow(
                            icon = Icons.Default.Settings,
                            title = "App Settings",
                            subtitle = "Reader preferences and theme",
                            checked = syncSettings,
                            onCheckedChange = { onToggleSync("settings", it) }
                        )
                    }
                }
            }

            // CLOUD STORAGE
            item {
                Text(
                    text = "CLOUD STORAGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaVioletSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Default.Cloud, contentDescription = null, tint = LuminaCyanAccent)
                                Text("Google Drive", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = LuminaEmerald.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, LuminaEmerald)
                            ) {
                                Text(
                                    text = "Connected ✓",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaEmerald,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Default.FolderShared, contentDescription = null, tint = TextSecondary)
                                Text("Dropbox", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = {}) {
                                Text("Link Account", color = LuminaVioletSecondary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // LOCAL STORAGE
            item {
                Text(
                    text = "LOCAL STORAGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Create backup */ },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, tint = LuminaAmberGlow)
                                Column {
                                    Text("Create Local Backup", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Export a .lumina file to your device", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                        }

                        HorizontalDivider(color = LuminaBorder, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Restore backup */ },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Default.FileUpload, contentDescription = null, tint = LuminaCyanAccent)
                                Column {
                                    Text("Restore from File", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Import data from an existing backup", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SyncToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(icon, contentDescription = null, tint = LuminaVioletSecondary, modifier = Modifier.size(20.dp))
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = LuminaVioletPrimary
            )
        )
    }
}
