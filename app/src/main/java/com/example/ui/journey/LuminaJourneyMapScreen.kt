package com.example.ui.journey

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LuminaJourneyMapScreen(
    onBack: () -> Unit
) {
    var viewMode by remember { mutableIntStateOf(0) } // 0 = Year View, 1 = Session View

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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("journey_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Journey Map",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                shape = RoundedCornerShape(20.dp),
                color = LuminaSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, LuminaBorder)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (viewMode == 0) LuminaVioletPrimary else Color.Transparent)
                            .clickable { viewMode = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Year View", color = if (viewMode == 0) Color.White else TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (viewMode == 1) LuminaVioletPrimary else Color.Transparent)
                            .clickable { viewMode = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Session View", color = if (viewMode == 1) Color.White else TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LuminaBlack)
        ) {
            // Constellation Orbit Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw starry orbital constellation links
                val p1 = Offset(w * 0.25f, h * 0.22f)
                val p2 = Offset(w * 0.70f, h * 0.42f)
                val p3 = Offset(w * 0.35f, h * 0.68f)

                // Dashed Orbit path
                drawLine(
                    color = LuminaBorder,
                    start = p1,
                    end = p2,
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )
                drawLine(
                    color = LuminaVioletSecondary.copy(alpha = 0.6f),
                    start = p2,
                    end = p3,
                    strokeWidth = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )

                // Orbital glow rings
                drawCircle(color = LuminaVioletPrimary.copy(alpha = 0.15f), radius = 60f, center = p1)
                drawCircle(color = LuminaCyanAccent.copy(alpha = 0.15f), radius = 70f, center = p2)
                drawCircle(color = LuminaAmberGlow.copy(alpha = 0.25f), radius = 90f, center = p3)
            }

            // Planet Node 1: Naruto
            PlanetNode(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 60.dp, y = 80.dp),
                title = "Naruto",
                chapters = "700 Chaps",
                date = "Jan 2023",
                glowColor = LuminaVioletPrimary
            )

            // Planet Node 2: Bleach
            PlanetNode(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-40).dp, y = (-40).dp),
                title = "Bleach",
                chapters = "364 Chaps",
                date = "Aug 2023",
                glowColor = LuminaCyanAccent
            )

            // Planet Node 3: One Piece (CURRENT ACTIVE GLOW)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 60.dp, y = (-120).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LuminaAmberGlow.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuminaAmberGlow)
                ) {
                    Text(
                        text = "CURRENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaAmberGlow,
                        fontWeight = FontWeight.Black,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                PlanetNode(
                    title = "One Piece",
                    chapters = "1080+ Chaps",
                    date = "Oct 2023 - Present",
                    glowColor = LuminaAmberGlow,
                    isCurrent = true
                )
            }
        }
    }
}

@Composable
fun PlanetNode(
    modifier: Modifier = Modifier,
    title: String,
    chapters: String,
    date: String,
    glowColor: Color,
    isCurrent: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isCurrent) 60.dp else 48.dp)
                .clip(CircleShape)
                .background(LuminaSurfaceVariant)
                .border(2.dp, glowColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoStories,
                contentDescription = null,
                tint = glowColor,
                modifier = Modifier.size(if (isCurrent) 28.dp else 22.dp)
            )
        }

        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(text = "$chapters • $date", color = TextSecondary, fontSize = 10.sp)
    }
}
