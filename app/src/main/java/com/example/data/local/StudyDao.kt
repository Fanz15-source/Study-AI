package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    // --- Study Materials ---
    @Query("SELECT * FROM study_materials ORDER BY id DESC")
    fun getAllMaterials(): Flow<List<StudyMaterial>>

    @Query("SELECT * FROM study_materials WHERE id = :id")
    suspend fun getMaterialById(id: Long): StudyMaterial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: StudyMaterial): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<StudyMaterial>)

    @Update
    suspend fun updateMaterial(material: StudyMaterial)

    @Delete
    suspend fun deleteMaterial(material: StudyMaterial)

    // --- Flashcards ---
    @Query("SELECT * FROM flashcards ORDER BY id ASC")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE materialId = :materialId")
    fun getFlashcardsByMaterial(materialId: Long): Flow<List<Flashcard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<Flashcard>)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    // --- Quiz Questions ---
    @Query("SELECT * FROM quiz_questions ORDER BY id ASC")
    fun getAllQuizQuestions(): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions WHERE materialId = :materialId")
    fun getQuizQuestionsByMaterial(materialId: Long): Flow<List<QuizQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestion(question: QuizQuestion): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestion>)

    // --- Study Notes ---
    @Query("SELECT * FROM study_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<StudyNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNote): Long

    @Update
    suspend fun updateNote(note: StudyNote)

    @Delete
    suspend fun deleteNote(note: StudyNote)

    // --- Study Tasks ---
    @Query("SELECT * FROM study_tasks ORDER BY isCompleted ASC, dueDate ASC")
    fun getAllTasks(): Flow<List<StudyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<StudyTask>)

    @Update
    suspend fun updateTask(task: StudyTask)

    @Delete
    suspend fun deleteTask(task: StudyTask)

    // --- Study Sessions ---
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<StudySession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySession): Long

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // --- Reset All Tables ---
    @Query("DELETE FROM study_materials")
    suspend fun clearMaterials()

    @Query("DELETE FROM flashcards")
    suspend fun clearFlashcards()

    @Query("DELETE FROM quiz_questions")
    suspend fun clearQuizQuestions()

    @Query("DELETE FROM study_notes")
    suspend fun clearNotes()

    @Query("DELETE FROM study_tasks")
    suspend fun clearTasks()

    @Query("DELETE FROM study_sessions")
    suspend fun clearSessions()
}
