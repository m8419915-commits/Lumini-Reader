package com.example.ui.dna

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Achievement
import com.example.domain.model.DnaAttribute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDnaAchievementsScreen(
    seriesTitle: String,
    dnaAttributes: List<DnaAttribute>,
    achievements: List<Achievement>,
    readingSpeedPpm: Float,
    totalReadingMinutes: Int,
    chaptersReadCount: Int,
    currentStreak: Int,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SERIES GENOME & INTELLIGENCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaCyanAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = seriesTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("dna_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LuminaSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LuminaBlack)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // READING INTELLIGENCE METRICS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "READING INTELLIGENCE ENGINE",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaVioletSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Pace (PPM)",
                            value = "%.1f".format(readingSpeedPpm),
                            subtitle = "Pages / Min",
                            accentColor = LuminaCyanAccent,
                            icon = Icons.Default.Speed
                        )

                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Time Read",
                            value = "${totalReadingMinutes}m",
                            subtitle = "Total Focus",
                            accentColor = LuminaVioletPrimary,
                            icon = Icons.Default.Timer
                        )

                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Streak",
                            value = "$currentStreak d",
                            subtitle = "Active Flame",
                            accentColor = LuminaAmberGlow,
                            icon = Icons.Default.Bolt
                        )
                    }
                }
            }

            // MANGA DNA PROFILE ATTRIBUTE BARS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "NARRATIVE GENOME PROFILE",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaCyanAccent,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dna_attributes_card"),
                        shape = RoundedCornerShape(18.dp),
                        color = LuminaSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            dnaAttributes.forEach { attr ->
                                val animatedProgress by animateFloatAsState(targetValue = attr.intensity, label = "dna_anim")
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            attr.trait,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            "${(attr.intensity * 100).toInt()}%",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = attr.color,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = { animatedProgress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(CircleShape),
                                        color = attr.color,
                                        trackColor = LuminaBlack
                                    )

                                    if (attr.description.isNotEmpty()) {
                                        Text(
                                            attr.description,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // READER ACHIEVEMENTS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "READER ACHIEVEMENTS & MILESTONES",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaAmberGlow,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    val unlockedCount = achievements.count { it.unlocked }
                    Text(
                        text = "$unlockedCount / ${achievements.size} Unlocked",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            items(achievements, key = { it.id }) { ach ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("achievement_item_${ach.id}"),
                    shape = RoundedCornerShape(14.dp),
                    color = if (ach.unlocked) LuminaSurfaceVariant else LuminaSurface.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (ach.unlocked) LuminaAmberGlow.copy(alpha = 0.55f) else LuminaBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (ach.unlocked) LuminaAmberGlow.copy(alpha = 0.2f) else LuminaBorder),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (ach.unlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                contentDescription = ach.title,
                                tint = if (ach.unlocked) LuminaAmberGlow else TextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                ach.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                ach.desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = LuminaBlack
                        ) {
                            Text(
                                text = "+${ach.xp} XP",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (ach.unlocked) LuminaAmberGlow else TextMuted,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = LuminaSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
