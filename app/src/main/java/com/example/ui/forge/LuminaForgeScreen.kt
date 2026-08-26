package com.example.ui.forge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.domain.model.ExperiencePack
import com.example.ui.theme.*

@Composable
fun LuminaForgeScreen(
    experiencePacks: List<ExperiencePack>,
    onActivatePack: (String) -> Unit,
    onBack: () -> Unit
) {
    var hapticLevel by remember { mutableFloatStateOf(0.85f) }

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("forge_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Lumina Forge",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
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
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "EXPERIENCE PACKS",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaCyanAccent,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Forge Studio",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Craft and manage immersive reading environments. Fine-tune haptics, soundscapes, and visual presentation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            // + NEW PACK BUTTON
            item {
                Button(
                    onClick = { /* Create pack */ },
                    colors = ButtonDefaults.buttonColors(containerColor = LuminaVioletPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forge_new_pack_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Experience Pack", fontWeight = FontWeight.Bold)
                }
            }

            // EXPERIENCE PACK 1: MY BLEACH EXPERIENCE (ACTIVE)
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletSecondary),
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "My Bleach Experience",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = LuminaVioletPrimary.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Shonen",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LuminaVioletSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = LuminaEmerald.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, LuminaEmerald)
                                ) {
                                    Text(
                                        text = "Active",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LuminaEmerald,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Row {
                                IconButton(onClick = {}) { Icon(Icons.Default.Share, contentDescription = "Share", tint = TextSecondary, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = {}) { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondary, modifier = Modifier.size(18.dp)) }
                            }
                        }

                        // Feature 1: Reading Direction
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.AutoStories, contentDescription = null, tint = LuminaCyanAccent, modifier = Modifier.size(18.dp))
                                Text("Reading Direction", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Text("Right to Left ✓", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        // Feature 2: Haptic Intensity
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Vibration, contentDescription = null, tint = LuminaVioletSecondary, modifier = Modifier.size(18.dp))
                                    Text("Haptic Intensity", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                                Text("${(hapticLevel * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = LuminaVioletSecondary, fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = hapticLevel,
                                onValueChange = { hapticLevel = it },
                                colors = SliderDefaults.colors(
                                    thumbColor = LuminaVioletSecondary,
                                    activeTrackColor = LuminaVioletPrimary,
                                    inactiveTrackColor = LuminaBorder
                                )
                            )
                        }

                        // Feature 3: Background
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Palette, contentDescription = null, tint = LuminaAmberGlow, modifier = Modifier.size(18.dp))
                                Text("Background", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Text("Soul Society Theme (Edit)", style = MaterialTheme.typography.labelSmall, color = LuminaAmberGlow, fontWeight = FontWeight.Bold)
                        }

                        // Feature 4: Audio Profile
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = LuminaRoseAccent, modifier = Modifier.size(18.dp))
                                Text("Audio Profile", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Text("Action / Heavy ılılı", style = MaterialTheme.typography.labelSmall, color = LuminaRoseAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // EXPERIENCE PACK 2: MIDNIGHT READING (DRAFT)
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Midnight Reading", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = LuminaBorder.copy(alpha = 0.5f)
                                ) {
                                    Text("Quiet", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = LuminaAmberGlow.copy(alpha = 0.15f)
                                ) {
                                    Text("Draft", style = MaterialTheme.typography.labelSmall, color = LuminaAmberGlow, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("AMOLED Pitch Black • Subtle Lo-Fi Rain", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }

                        IconButton(onClick = { onActivatePack("midnight_reading") }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Activate", tint = LuminaVioletSecondary)
                        }
                    }
                }
            }

            // SHARE YOUR FORGE CALLOUT
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LuminaVioletPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletSecondary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Share Your Forge", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Export your custom experience packs to the community gallery for others to enjoy.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        OutlinedButton(
                            onClick = { /* Publish */ },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LuminaVioletSecondary)
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = LuminaVioletSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Publish Pack", color = LuminaVioletSecondary)
                        }
                    }
                }
            }
        }
    }
}
