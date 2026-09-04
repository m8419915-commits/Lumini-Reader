package com.example.luminareader.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LuminaViolet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2E1A47),
    onPrimaryContainer = LuminaVioletLight,
    secondary = LuminaCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF003840),
    onSecondaryContainer = LuminaCyan,
    tertiary = LuminaVioletLight,
    background = LuminaBlack,
    onBackground = TextPrimary,
    surface = LuminaSurface,
    onSurface = TextPrimary,
    surfaceVariant = LuminaSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = LuminaBorder
)

@Composable
fun LuminaReaderTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
