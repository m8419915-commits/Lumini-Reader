package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LuminaDao {
    @Query("SELECT * FROM mangas ORDER BY title ASC")
    fun observeAllMangas(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM mangas WHERE favorite = 1 ORDER BY title ASC")
    fun observeLibrary(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM mangas WHERE id = :mangaId")
    fun observeMangaById(mangaId: Long): Flow<MangaEntity?>

    @Query("SELECT * FROM mangas WHERE id = :mangaId")
    suspend fun getMangaById(mangaId: Long): MangaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: MangaEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMangas(mangas: List<MangaEntity>): List<Long>

    @Update
    suspend fun updateManga(manga: MangaEntity)

    @Query("UPDATE mangas SET favorite = :favorite WHERE id = :mangaId")
    suspend fun setFavorite(mangaId: Long, favorite: Boolean)

    @Query("SELECT * FROM chapters WHERE mangaId = :mangaId ORDER BY chapterNumber DESC")
    fun observeChapters(mangaId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    suspend fun getChapterById(chapterId: Long): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("UPDATE chapters SET read = :read, lastPageRead = :lastPage WHERE id = :chapterId")
    suspend fun updateChapterReadProgress(chapterId: Long, read: Boolean, lastPage: Int)

    @Query("UPDATE chapters SET bookmark = :bookmarked WHERE id = :chapterId")
    suspend fun setChapterBookmark(chapterId: Long, bookmarked: Boolean)

    @Query("UPDATE chapters SET isDownloaded = :downloaded WHERE id = :chapterId")
    suspend fun setChapterDownloaded(chapterId: Long, downloaded: Boolean)

    // Reading Snaps (1-sec state restore)
    @Query("SELECT * FROM reading_snaps ORDER BY timestamp DESC LIMIT 10")
    fun observeSnaps(): Flow<List<ReadingSnapEntity>>

    @Query("SELECT * FROM reading_snaps WHERE mangaId = :mangaId LIMIT 1")
    suspend fun getSnapByManga(mangaId: Long): ReadingSnapEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSnap(snap: ReadingSnapEntity)

    @Query("DELETE FROM reading_snaps WHERE mangaId = :mangaId")
    suspend fun deleteSnap(mangaId: Long)

    // Achievements
    @Query("SELECT * FROM achievements")
    fun observeAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Query("UPDATE achievements SET unlocked = 1, progress = 1.0 WHERE id = :id")
    suspend fun unlockAchievement(id: String)
}
