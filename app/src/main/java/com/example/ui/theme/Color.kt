package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Lumina Noir AMOLED Color Palette
val LuminaBlack = Color(0xFF050508)
val LuminaSurface = Color(0xFF0E0E17)
val LuminaSurfaceVariant = Color(0xFF161624)
val LuminaCardBackground = Color(0xFF13131E)
val LuminaBorder = Color(0xFF222238)
val LuminaBorderSubtle = Color(0xFF2D2D4A)

// Lumina Neon Glow Accents
val LuminaVioletPrimary = Color(0xFF8B5CF6)
val LuminaVioletSecondary = Color(0xFFA78BFA)
val LuminaCyanAccent = Color(0xFF06B6D4)
val LuminaAmberGlow = Color(0xFFF59E0B)
val LuminaRoseAccent = Color(0xFFF43F5E)
val LuminaEmerald = Color(0xFF10B981)
val LuminaIndigo = Color(0xFF6366F1)

// Typography & Content Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Gradients & Ambient Effects
val LuminaGlowGradient = Brush.horizontalGradient(
    colors = listOf(LuminaVioletPrimary, LuminaCyanAccent)
)

val LuminaAmberRoseGradient = Brush.horizontalGradient(
    colors = listOf(LuminaAmberGlow, LuminaRoseAccent)
)

val LuminaCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A1A2E), Color(0xFF0E0E17))
)

val LuminaAmbientOverlay = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color(0xFF050508).copy(alpha = 0.85f), Color(0xFF050508))
)
