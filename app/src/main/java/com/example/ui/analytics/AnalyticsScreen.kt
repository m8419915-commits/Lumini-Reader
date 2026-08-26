package com.example.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Achievement
import com.example.ui.dna.MetricCard
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    readingSpeedPpm: Float,
    totalMinutes: Int,
    chaptersRead: Int,
    streakDays: Int,
    achievements: List<Achievement>
) {
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
                    text = "LUMINA TELEMETRY & STATS",
                    style = MaterialTheme.typography.labelSmall,
                    color = LuminaCyanAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Reading Intelligence",
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
            // METRIC CARDS ROW
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Speed",
                        value = "%.1f".format(readingSpeedPpm),
                        subtitle = "PPM",
                        accentColor = LuminaCyanAccent,
                        icon = Icons.Default.Speed
                    )

                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Chapters",
                        value = "$chaptersRead",
                        subtitle = "Completed",
                        accentColor = LuminaVioletPrimary,
                        icon = Icons.Default.MenuBook
                    )

                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Streak",
                        value = "$streakDays",
                        subtitle = "Days Flame",
                        accentColor = LuminaAmberGlow,
                        icon = Icons.Default.Bolt
                    )
                }
            }

            // WEEKLY READING HEATMAP
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("weekly_heatmap_card"),
                    shape = RoundedCornerShape(18.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "WEEKLY READING CADENCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaVioletSecondary,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            val hours = listOf(1.2f, 2.5f, 1.8f, 3.1f, 2.4f, 4.0f, 3.5f)

                            days.zip(hours).forEach { (day, h) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(28.dp)
                                            .height((h * 24).dp)
                                            .background(
                                                if (h > 2.5f) LuminaVioletPrimary else LuminaCyanAccent,
                                                RoundedCornerShape(6.dp)
                                            )
                                    )
                                    Text(day, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // TOTAL TIME READ
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = LuminaSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = LuminaAmberGlow,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = "Total Focus Reading Time",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${totalMinutes / 60} hours and ${totalMinutes % 60} minutes recorded in Lumina Reader",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
