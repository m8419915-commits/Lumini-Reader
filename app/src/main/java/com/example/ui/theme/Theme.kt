package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LuminaColorScheme = darkColorScheme(
    primary = LuminaVioletPrimary,
    onPrimary = Color.White,
    primaryContainer = LuminaSurfaceVariant,
    onPrimaryContainer = LuminaVioletSecondary,
    secondary = LuminaCyanAccent,
    onSecondary = Color.Black,
    secondaryContainer = LuminaSurfaceVariant,
    onSecondaryContainer = LuminaCyanAccent,
    tertiary = LuminaAmberGlow,
    onTertiary = Color.Black,
    tertiaryContainer = LuminaSurfaceVariant,
    onTertiaryContainer = LuminaAmberGlow,
    background = LuminaBlack,
    onBackground = TextPrimary,
    surface = LuminaSurface,
    onSurface = TextPrimary,
    surfaceVariant = LuminaSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = LuminaBorder,
    outlineVariant = LuminaBorderSubtle,
    error = LuminaRoseAccent,
    onError = Color.White
)

@Composable
fun LuminaReaderTheme(
    darkTheme: Boolean = true, // Default to true for true AMOLED experience
    content: @Composable () -> Unit
) {
    val colorScheme = LuminaColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LuminaBlack.toArgb()
            window.navigationBarColor = LuminaBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    LuminaReaderTheme(darkTheme = true, content = content)
}
