package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Lumina Reader", appName)
    }

    @Test
    fun `test reading snap creation and progress`() {
        val snap = ReadingSnap(
            mangaId = 1L,
            chapterId = 101L,
            title = "Solo Leveling",
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477",
            chapterNumber = 179f,
            chapterName = "Chapter 179: Epilogue",
            pageIndex = 12,
            scrollOffset = 340,
            progressPercent = 0.85f
        )
        assertEquals(1L, snap.mangaId)
        assertEquals(12, snap.pageIndex)
        assertEquals(0.85f, snap.progressPercent)
        assertTrue(snap.timestamp > 0)
    }

    @Test
    fun `test default reader configuration values`() {
        val config = ReaderConfig(
            readerMode = ReaderMode.CONTINUOUS_WEBTOON,
            readingDirection = ReadingDirection.TOP_TO_BOTTOM,
            backgroundColor = ReaderBackgroundColor.AMOLED_BLACK,
            ambientFlowEnabled = true,
            tilingEnabled = true,
            hardwareAcceleration = true
        )
        assertEquals(ReaderMode.CONTINUOUS_WEBTOON, config.readerMode)
        assertTrue(config.ambientFlowEnabled)
        assertTrue(config.tilingEnabled)
    }

    @Test
    fun `test experience pack structure`() {
        val pack = ExperiencePack(
            id = "bleach_pack",
            title = "My Bleach Experience",
            categoryTag = "Shonen",
            isActive = true,
            readingDirection = "Right to Left",
            hapticIntensity = 0.85f,
            backgroundTheme = "Soul Society Theme",
            audioProfile = "Action / Heavy"
        )
        assertEquals("Shonen", pack.categoryTag)
        assertTrue(pack.isActive)
        assertEquals(0.85f, pack.hapticIntensity)
    }
}
