package com.example.ui.universe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.UniverseEdge
import com.example.domain.model.UniverseNode
import com.example.ui.theme.*
import kotlin.math.pow
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaUniverseMapScreen(
    mangaTitle: String,
    nodes: List<UniverseNode>,
    edges: List<UniverseEdge>,
    onBack: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var selectedNode by remember { mutableStateOf<UniverseNode?>(nodes.firstOrNull()) }

    Scaffold(
        containerColor = LuminaBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "UNIVERSE MAP",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaCyanAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = mangaTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("universe_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scale = 1f
                        offset = Offset.Zero
                    }) {
                        Icon(Icons.Default.CenterFocusStrong, contentDescription = "Reset Zoom", tint = LuminaCyanAccent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LuminaSurface)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LuminaBlack)
                .pointerInput(nodes) {
                    detectTapGestures { tapOffset ->
                        // Calculate transformed tap position
                        val transformedX = (tapOffset.x - offset.x) / scale
                        val transformedY = (tapOffset.y - offset.y) / scale

                        // Find closest node within touch radius
                        val clicked = nodes.find { node ->
                            val dist = sqrt((node.x - transformedX).pow(2) + (node.y - transformedY).pow(2))
                            dist <= 48f
                        }
                        if (clicked != null) {
                            selectedNode = clicked
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.5f, 3.5f)
                        offset += pan
                    }
                }
        ) {
            // Background cosmic starfield effect
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            ) {
                // Draw relationship connections
                edges.forEach { edge ->
                    val from = nodes.find { it.id == edge.fromNodeId }
                    val to = nodes.find { it.id == edge.toNodeId }
                    if (from != null && to != null) {
                        val isHighlighted = selectedNode?.id == from.id || selectedNode?.id == to.id
                        drawLine(
                            color = if (isHighlighted) edge.color else LuminaBorder,
                            start = Offset(from.x, from.y),
                            end = Offset(to.x, to.y),
                            strokeWidth = if (isHighlighted) 4f else 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                        )
                    }
                }

                // Draw character/entity nodes
                nodes.forEach { node ->
                    val isSelected = selectedNode?.id == node.id

                    // Outer Halo
                    drawCircle(
                        color = node.color.copy(alpha = if (isSelected) 0.45f else 0.18f),
                        radius = if (isSelected) 56f else 40f,
                        center = Offset(node.x, node.y)
                    )

                    // Core Node
                    drawCircle(
                        color = node.color,
                        radius = if (isSelected) 30f else 22f,
                        center = Offset(node.x, node.y)
                    )
                }
            }

            // Map Quick Help Tip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LuminaSurfaceVariant.copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.TouchApp, contentDescription = null, tint = LuminaCyanAccent, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Pinch to zoom • Tap nodes to inspect lore",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            // Node Inspector Overlay Panel
            AnimatedVisibility(
                visible = selectedNode != null,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                selectedNode?.let { node ->
                    val connectedEdges = edges.filter { it.fromNodeId == node.id || it.toNodeId == node.id }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .testTag("node_inspector_panel"),
                        shape = RoundedCornerShape(20.dp),
                        color = LuminaSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, node.color.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(node.color)
                                    )
                                    Column {
                                        Text(
                                            node.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            node.role,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = node.color
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = LuminaBlack
                                ) {
                                    Text(
                                        text = node.powerLevel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LuminaAmberGlow,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (node.bio.isNotEmpty()) {
                                Text(
                                    text = node.bio,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            // Faction & Connections
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Faction: ${node.faction}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "${connectedEdges.size} Lore Link(s)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaCyanAccent
                                )
                            }

                            if (connectedEdges.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    connectedEdges.forEach { edge ->
                                        val otherNodeId = if (edge.fromNodeId == node.id) edge.toNodeId else edge.fromNodeId
                                        val otherNode = nodes.find { it.id == otherNodeId }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = LuminaBlack.copy(alpha = 0.6f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "→ ${otherNode?.name ?: "Unknown"}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = edge.relationLabel,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = edge.color
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
