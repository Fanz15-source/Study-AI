package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_materials")
data class StudyMaterial(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val topic: String,
    val fileType: String = "PDF", // PDF, DOCX, NOTE, SLIDE
    val dateAdded: String,
    val lastStudied: String,
    val progress: Float = 0f, // 0.0 to 1.0
    val content: String,
    val summary: String = "",
    val keyConcepts: String = "",
    val isFavorite: Boolean = false
)

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val materialId: Long = 0,
    val subject: String,
    val question: String,
    val answer: String,
    val isMastered: Boolean = false,
    val reviewCount: Int = 0
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val materialId: Long = 0,
    val subject: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int, // 0 to 3
    val explanation: String
)

@Entity(tableName = "study_notes")
data class StudyNote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val materialId: Long? = null,
    val title: String,
    val content: String,
    val tags: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val dueDate: String,
    val dueTime: String = "19:00",
    val isExam: Boolean = false,
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = true, // User toggleable on/off
    val daysRemaining: Int = 3
)

@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val sessionType: String = "Focus", // Focus, Break, Practice
    val materialTitle: String,
    val timestamp: Long = System.currentTimeMillis(),
    val xpEarned: Int = 100
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex",
    val educationLevel: String = "Universitas",
    val targetSubjects: String = "Biologi, Fisika, Matematika",
    val streakDays: Int = 5,
    val totalXp: Int = 1250,
    val level: Int = 4,
    val dailyGoalMinutes: Int = 45,
    val todayStudiedMinutes: Int = 35,
    val selectedTheme: String = "DARK_FOCUS", // DARK_FOCUS, LIGHT_CALM, CYBER_INDIGO, FOREST_MATCHA, TWILIGHT_AMBER
    val selectedAmbience: String = "CALM_GRADIENT",
    val ambientAnimationsEnabled: Boolean = true,
    val examRemindersMasterToggle: Boolean = true, // Global exam reminder switch
    val soundVolume: Float = 0.5f,
    val unlockedAchievements: String = "FIRST_MATERIAL,FOCUS_STARTER,FIRST_QUIZ"
)

data class CheatSheetItem(
    val id: Long,
    val subject: String,
    val topic: String,
    val title: String,
    val formulaOrRule: String,
    val explanation: String
)

data class SpacedRepetitionItem(
    val id: Long,
    val topic: String,
    val subject: String,
    val stage: String, // "H+1 Review", "H+3 Review", "H+7 Review"
    val memoryRetention: Int, // e.g. 78%
    val isDueToday: Boolean = true
)

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val xpReward: Int,
    val isUnlocked: Boolean
)

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String,
    val category: String,
    val platform: String,
    val externalUrl: String = "",
    val synthMode: String = "RAIN"
)
