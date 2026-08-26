package com.example.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun MoreScreen(
    onNavigate: (Screen) -> Unit
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
                Text(
                    text = "Lumina Ecosystem",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // SECTION 1: INTELLIGENCE & AI
            item {
                Text(
                    text = "INTELLIGENCE & ASSISTANT",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyanAccent,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = LuminaVioletSecondary,
                    title = "Lumina AI Assistant",
                    subtitle = "Semantic manga recommendation, chapter summaries, character lookup",
                    onClick = { onNavigate(Screen.AiAssistant) }
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.BarChart,
                    iconTint = LuminaCyanAccent,
                    title = "Reading Intelligence & Analytics",
                    subtitle = "Reading speed, streaks, genre affinity breakdown, time stats",
                    onClick = { onNavigate(Screen.Analytics) }
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.Biotech,
                    iconTint = LuminaEmerald,
                    title = "Lumina Mutation Engine",
                    subtitle = "Day 30 Evolution 60% • Adaptive learned reading behaviors",
                    onClick = { onNavigate(Screen.Mutation) }
                )
            }

            // SECTION 2: IMMERSION & STORY WORLDS
            item {
                Text(
                    text = "IMMERSION & EXPERIENCES",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaAmberGlow,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.Tune,
                    iconTint = LuminaVioletPrimary,
                    title = "Lumina Forge",
                    subtitle = "Experience Packs • Custom haptics, ambient audio profiles, shaders",
                    onClick = { onNavigate(Screen.Forge) }
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.Public,
                    iconTint = LuminaCyanAccent,
                    title = "Journey Map",
                    subtitle = "Constellation history map & multi-year timeline orbit",
                    onClick = { onNavigate(Screen.JourneyMap) }
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.Timeline,
                    iconTint = LuminaAmberGlow,
                    title = "Timeline Mode (Interactive Guide)",
                    subtitle = "Chronological vs Publication narrative trees & key events",
                    onClick = { onNavigate(Screen.Timeline(1L)) }
                )
            }

            // SECTION 3: SYSTEM & PREFERENCES
            item {
                Text(
                    text = "DATA & SYSTEM",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.CloudSync,
                    iconTint = LuminaCyanAccent,
                    title = "Backup & Cloud Sync",
                    subtitle = "Google Drive, Dropbox, local .lumina exports & restores",
                    onClick = { onNavigate(Screen.BackupSync) }
                )
            }

            item {
                MoreNavCard(
                    icon = Icons.Default.Settings,
                    iconTint = Color.White,
                    title = "Reader Settings",
                    subtitle = "Reading direction, split page spreads, cache & gesture tuning",
                    onClick = { onNavigate(Screen.Settings) }
                )
            }
        }
    }
}

@Composable
fun MoreNavCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("more_card_${title.replace(" ", "_").lowercase()}"),
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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f))
                        .border(1.dp, iconTint.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
        }
    }
}
