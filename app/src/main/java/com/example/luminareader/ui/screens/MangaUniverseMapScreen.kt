package com.example.luminareader.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.luminareader.data.model.UniverseEdge
import com.example.luminareader.data.model.UniverseNode
import com.example.luminareader.ui.theme.*

@Composable
fun MangaUniverseMapScreen(
    nodes: List<UniverseNode>,
    edges: List<UniverseEdge>,
    onBack: () -> Unit
) {
    var selectedNode by remember { mutableStateOf<UniverseNode?>(null) }

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
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Manga Universe Map",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Bleach: Thousand-Year Blood War • Lore Graph",
                            style = MaterialTheme.typography.bodySmall,
                            color = LuminaCyan
                        )
                    }
                }
            }
        },
        containerColor = LuminaBlack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("universe_map_screen")
        ) {
            // Interactive 2D Graph Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(LuminaSurface)
                    .border(1.dp, LuminaBorder)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw connecting edges
                    edges.forEach { edge ->
                        val from = nodes.find { it.id == edge.fromNodeId }
                        val to = nodes.find { it.id == edge.toNodeId }
                        if (from != null && to != null) {
                            val start = Offset(from.x * w, from.y * h)
                            val end = Offset(to.x * w, to.y * h)
                            drawLine(
                                color = Color(edge.color).copy(alpha = 0.4f),
                                start = start,
                                end = end,
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
                }

                // Overlay interactive nodes
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val boxWidth = maxWidth
                    val boxHeight = maxHeight

                    nodes.forEach { node ->
                        val nodeColor = Color(node.color)
                        val isSelected = selectedNode?.id == node.id

                        Box(
                            modifier = Modifier
                                .offset(
                                    x = (boxWidth * node.x) - 24.dp,
                                    y = (boxHeight * node.y) - 24.dp
                                )
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(nodeColor.copy(alpha = 0.2f))
                                .border(
                                    if (isSelected) 2.5.dp else 1.5.dp,
                                    if (isSelected) Color.White else nodeColor,
                                    CircleShape
                                )
                                .clickable { selectedNode = node },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = node.name.split(" ").firstOrNull()?.take(2) ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Relationship & Node List
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "CHARACTER NODES (${nodes.size})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                items(nodes, key = { it.id }) { node ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, LuminaBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedNode = node },
                        color = LuminaSurfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color(node.color))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = node.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${node.role} • ${node.faction}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            Surface(
                                color = Color(node.color).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = node.powerLevel,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(node.color),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "CANON CONNECTIONS (${edges.size})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                items(edges, key = { "${it.fromNodeId}_${it.toNodeId}" }) { edge ->
                    val from = nodes.find { it.id == edge.fromNodeId }?.name ?: "Unknown"
                    val to = nodes.find { it.id == edge.toNodeId }?.name ?: "Unknown"

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        color = LuminaCard
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = from, color = LuminaCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "↔", color = TextMuted, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = to, color = LuminaVioletLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = edge.relationLabel, color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Node Inspection Dialog
        selectedNode?.let { node ->
            Dialog(onDismissRequest = { selectedNode = null }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color(node.color).copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
                    color = LuminaCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = node.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = node.role,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(node.color),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { selectedNode = null },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = "FACTION", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = node.faction, color = TextPrimary, fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "POWER LEVEL", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = node.powerLevel, color = LuminaAmber, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "LORE SYNPOSIS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = node.bio, color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { selectedNode = null },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(node.color))
                        ) {
                            Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
