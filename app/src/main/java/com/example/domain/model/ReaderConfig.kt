package com.example.domain.model

enum class ReaderMode(val displayName: String) {
    CONTINUOUS_WEBTOON("Webtoon Vertical"),
    SINGLE_PAGE_LTR("Paged (Left to Right)"),
    SINGLE_PAGE_RTL("Paged (Right to Left / Manga)"),
    DUAL_PAGE_SPREAD("Dual Page Spread (Landscape)")
}

enum class BackgroundTint(val displayName: String, val hex: Long) {
    AMOLED_BLACK("AMOLED Pure (#050508)", 0xFF050508),
    DARK_CHARCOAL("Dark Slate (#0E0E17)", 0xFF0E0E17),
    MIDNIGHT_BLUE("Midnight Blue (#0B0D1B)", 0xFF0B0D1B),
    SEPIA_NIGHT("Sepia Warm (#181410)", 0xFF181410)
}

data class ReaderConfig(
    val readerMode: ReaderMode = ReaderMode.CONTINUOUS_WEBTOON,
    val enableLuminaFlow: Boolean = true, // Story-responsive ambient Palette background
    val ambientGlowIntensity: Float = 0.55f,
    val backgroundTint: BackgroundTint = BackgroundTint.AMOLED_BLACK,
    val keepScreenOn: Boolean = true,
    val volumeKeyNavigation: Boolean = true,
    val cropBorders: Boolean = false,
    val invertColors: Boolean = false,
    val splitLongStrips: Boolean = true, // Long-strip bitmap tiling to prevent Canvas OOM
    val wifiOnlyDownload: Boolean = true
)
