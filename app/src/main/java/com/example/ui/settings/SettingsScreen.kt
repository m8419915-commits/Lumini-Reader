package com.example.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.BackgroundTint
import com.example.domain.model.ReaderConfig
import com.example.domain.model.ReaderMode
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    readerConfig: ReaderConfig,
    onUpdateReaderConfig: (ReaderConfig) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "LUMINA FORGE & ECOSYSTEM",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyanAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.headlineMedium,
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
                .background(LuminaBlack)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // READER DEFAULTS
            item {
                SettingsSection(title = "READER ENGINE CONFIGURATION") {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Default Reading Orientation", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ReaderMode.entries.forEach { mode ->
                                val isSelected = readerConfig.readerMode == mode
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onUpdateReaderConfig(readerConfig.copy(readerMode = mode)) },
                                    color = if (isSelected) LuminaVioletPrimary else LuminaSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) LuminaVioletPrimary else LuminaBorder)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when (mode) {
                                                ReaderMode.CONTINUOUS_WEBTOON -> "Webtoon"
                                                ReaderMode.SINGLE_PAGE_LTR -> "LTR"
                                                ReaderMode.SINGLE_PAGE_RTL -> "RTL"
                                                ReaderMode.DUAL_PAGE_SPREAD -> "Spread"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Divider(color = LuminaBorder, thickness = 0.5.dp)

                        // Lumina Flow
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Lumina Flow Dynamic Ambient", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Real-time Palette glow backlight", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                            Switch(
                                checked = readerConfig.enableLuminaFlow,
                                onCheckedChange = { onUpdateReaderConfig(readerConfig.copy(enableLuminaFlow = it)) },
                                colors = SwitchDefaults.colors(checkedThumbColor = LuminaVioletPrimary)
                            )
                        }

                        Divider(color = LuminaBorder, thickness = 0.5.dp)

                        // Hardware Acceleration & Screen Awake
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Keep Display Awake During Reading", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            Switch(
                                checked = readerConfig.keepScreenOn,
                                onCheckedChange = { onUpdateReaderConfig(readerConfig.copy(keepScreenOn = it)) },
                                colors = SwitchDefaults.colors(checkedThumbColor = LuminaVioletPrimary)
                            )
                        }
                    }
                }
            }

            // AMOLED BACKGROUND & THEME
            item {
                SettingsSection(title = "AMOLED DISPLAY & CONTRAST") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Reader Canvas AMOLED Tint", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BackgroundTint.entries.forEach { tint ->
                                val isSelected = readerConfig.backgroundTint == tint
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onUpdateReaderConfig(readerConfig.copy(backgroundTint = tint)) },
                                    color = Color(tint.hex),
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) LuminaVioletPrimary else LuminaBorder
                                    )
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = tint.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) LuminaCyanAccent else TextMuted,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // DATA & STORAGE
            item {
                SettingsSection(title = "DATA & OFFLINE VAULT") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Only Download on Unmetered Wi-Fi", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                Text("Save cellular data consumption", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                            Switch(
                                checked = readerConfig.wifiOnlyDownload,
                                onCheckedChange = { onUpdateReaderConfig(readerConfig.copy(wifiOnlyDownload = it)) },
                                colors = SwitchDefaults.colors(checkedThumbColor = LuminaCyanAccent)
                            )
                        }

                        Divider(color = LuminaBorder, thickness = 0.5.dp)

                        Button(
                            onClick = {
                                Toast.makeText(context, "Lumina Reader Cache Cleared", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaSurface),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("clear_cache_button")
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = LuminaRoseAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Clear Image & Reader Cache", color = LuminaRoseAccent)
                        }
                    }
                }
            }

            // SYSTEM ABOUT
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Lumina Reader v1.0.0", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Engineered with Lumina Noir AMOLED UI, Keiyoushi Protobuf Index Parsing, Palette Flow Backlighting, and Room Snap Persistence.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = LuminaCyanAccent,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = LuminaSurfaceVariant,
            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
        ) {
            Box(modifier = Modifier.padding(18.dp)) {
                content()
            }
        }
    }
}
