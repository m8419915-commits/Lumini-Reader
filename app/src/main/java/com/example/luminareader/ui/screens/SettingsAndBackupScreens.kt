package com.example.luminareader.ui.screens

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminareader.ui.theme.*

@Composable
fun BackupSyncScreen(
    onBackupCreated: () -> Unit,
    onRestoreBackup: () -> Unit,
    onBack: () -> Unit
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
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LuminaSurfaceVariant)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Backup & Restore", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Local encrypted JSON snapshots & sync state", style = MaterialTheme.typography.bodySmall, color = LuminaCyan)
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
                .testTag("backup_sync_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, LuminaViolet.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    color = LuminaCard
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "CREATE BACKUP", style = MaterialTheme.typography.bodySmall, color = LuminaCyan, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Generate a full JSON export of your library, reading history, custom categories, and saved Lumina Snaps.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onBackupCreated,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaViolet),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create Encrypted Backup")
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, LuminaBorder, RoundedCornerShape(16.dp)),
                    color = LuminaSurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "RESTORE FROM FILE", style = MaterialTheme.typography.bodySmall, color = LuminaAmber, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Import previously exported Tachiyomi, Mihon, or Lumina backup files to restore entire library configurations.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = onRestoreBackup,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, tint = LuminaCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Restore Backup File", color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var dohEnabled by remember { mutableStateOf(true) }
    var amoledPitchBlack by remember { mutableStateOf(true) }
    var hardwareAcceleration by remember { mutableStateOf(true) }

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
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LuminaSurfaceVariant)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Security, AMOLED display & performance", style = MaterialTheme.typography.bodySmall, color = TextMuted)
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
                .testTag("settings_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(text = "NETWORK & PRIVACY", color = LuminaCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp)),
                    color = LuminaSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("DNS-over-HTTPS (DoH)", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Bypass ISP source blocks using Cloudflare DoH", color = TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = dohEnabled,
                            onCheckedChange = { dohEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyan, checkedTrackColor = LuminaViolet)
                        )
                    }
                }
            }

            item {
                Text(text = "DISPLAY & RENDERING", color = LuminaVioletLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp)),
                    color = LuminaSurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("True Pure AMOLED Noir", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text("Turns off OLED sub-pixels (#000000) for zero battery drain", color = TextMuted, fontSize = 11.sp)
                            }
                            Switch(
                                checked = amoledPitchBlack,
                                onCheckedChange = { amoledPitchBlack = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyan, checkedTrackColor = LuminaViolet)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("GPU Hardware Acceleration", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text("Smooth 120Hz continuous webtoon inertia rendering", color = TextMuted, fontSize = 11.sp)
                            }
                            Switch(
                                checked = hardwareAcceleration,
                                onCheckedChange = { hardwareAcceleration = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyan, checkedTrackColor = LuminaViolet)
                            )
                        }
                    }
                }
            }
        }
    }
}
