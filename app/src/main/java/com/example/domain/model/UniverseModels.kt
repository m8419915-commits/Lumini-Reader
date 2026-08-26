package com.example.domain.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.LuminaCyanAccent
import com.example.ui.theme.LuminaVioletPrimary

data class UniverseNode(
    val id: String,
    val name: String,
    val role: String,
    val x: Float,
    val y: Float,
    val color: Color = LuminaVioletPrimary,
    val bio: String = "",
    val faction: String = "Protagonists",
    val powerLevel: String = "S-Rank"
)

data class UniverseEdge(
    val fromNodeId: String,
    val toNodeId: String,
    val relationLabel: String,
    val color: Color = LuminaCyanAccent
)
