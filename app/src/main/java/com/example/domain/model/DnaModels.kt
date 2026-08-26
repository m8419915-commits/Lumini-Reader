package com.example.domain.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.LuminaVioletPrimary

data class DnaAttribute(
    val trait: String,
    val intensity: Float, // 0.0 to 1.0
    val color: Color = LuminaVioletPrimary,
    val description: String = ""
)

data class Achievement(
    val id: String,
    val title: String,
    val desc: String,
    val unlocked: Boolean = false,
    val xp: Int = 100,
    val category: String = "Reading",
    val progress: Float = 1.0f
)
