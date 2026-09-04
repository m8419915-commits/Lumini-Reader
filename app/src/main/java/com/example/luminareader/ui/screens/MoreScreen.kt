package com.example.luminareader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminareader.data.model.ScreenType
import com.example.luminareader.ui.theme.*

data class MoreHubItem(
    val type: ScreenType,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val badge: String? = null
)

@Composable
fun MoreScreen(
    onNavigate: (ScreenType) -> Unit
) {
    val items = listOf(
        MoreHubItem(
            type = ScreenType.UNIVERSE,
            title = "Manga Universe Map",
            subtitle = "Visual character lore graph & faction alliances",
            icon = Icons.Default.Hub,
            accentColor = LuminaCyan,
            badge = "Interactive"
        ),
        MoreHubItem(
            type = ScreenType.DNA,
            title = "Manga DNA & Achievements",
            subtitle = "Reading velocity PPM, genre radar, XP milestones",
            icon = Icons.Default.Fingerprint,
            accentColor = LuminaViolet,
            badge = "Level 14"
        ),
        MoreHubItem(
            type = ScreenType.AI,
            title = "Lumina AI Copilot",
            subtitle = "Lore analyst, chapter recaps & power scaling",
            icon = Icons.Default.AutoAwesome,
            accentColor = LuminaEmerald,
            badge = "Active"
        ),
        MoreHubItem(
            type = ScreenType.FORGE,
            title = "Lumina Forge",
            subtitle = "Custom experience packs, haptics & audio profiles",
            icon = Icons.Default.Tune,
            accentColor = LuminaAmber
        ),
        MoreHubItem(
            type = ScreenType.MUTATION,
            title = "Lumina Mutation",
            subtitle = "Experimental gesture physics & smart zoom hooks",
            icon = Icons.Default.Science,
            accentColor = LuminaRose
        ),
        MoreHubItem(
            type = ScreenType.TIMELINE,
            title = "Narrative Timeline",
            subtitle = "Chronological arc progression & milestone index",
            icon = Icons.Default.Timeline,
            accentColor = LuminaVioletLight
        ),
        MoreHubItem(
            type = ScreenType.BACKUP,
            title = "Backup & Restore",
            subtitle = "Encrypted local JSON snapshots & state recovery",
            icon = Icons.Default.CloudSync,
            accentColor = LuminaCyan
        ),
        MoreHubItem(
            type = ScreenType.SETTINGS,
            title = "Settings & Network",
            subtitle = "AMOLED tuning, DNS-over-HTTPS & security",
            icon = Icons.Default.Settings,
            accentColor = TextSecondary
        )
    )

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "More",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Advanced engines, lore tools & configuration",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
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
                .testTag("more_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items.size) { index ->
                val item = items[index]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, LuminaBorder, RoundedCornerShape(14.dp))
                        .clickable { onNavigate(item.type) },
                    color = LuminaSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(item.accentColor.copy(alpha = 0.15f))
                                .border(1.dp, item.accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.accentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )

                                if (item.badge != null) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = item.accentColor.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = item.badge,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = item.accentColor,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Lumina Reader v2.4.0 (Build 42)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Native Android Edition • Pure AMOLED Noir",
                        style = MaterialTheme.typography.bodySmall,
                        color = LuminaCyan.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
