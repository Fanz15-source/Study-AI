package com.example.data.repository

import com.example.data.local.DemoData
import com.example.data.local.StudyDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class StudyRepository(private val dao: StudyDao) {

    val allMaterials: Flow<List<StudyMaterial>> = dao.getAllMaterials()
    val allFlashcards: Flow<List<Flashcard>> = dao.getAllFlashcards()
    val allQuizQuestions: Flow<List<QuizQuestion>> = dao.getAllQuizQuestions()
    val allNotes: Flow<List<StudyNote>> = dao.getAllNotes()
    val allTasks: Flow<List<StudyTask>> = dao.getAllTasks()
    val allSessions: Flow<List<StudySession>> = dao.getAllSessions()
    val userProfile: Flow<UserProfile?> = dao.getUserProfile()

    suspend fun getMaterialById(id: Long): StudyMaterial? = dao.getMaterialById(id)

    suspend fun insertMaterial(material: StudyMaterial): Long = dao.insertMaterial(material)

    suspend fun updateMaterial(material: StudyMaterial) = dao.updateMaterial(material)

    suspend fun deleteMaterial(material: StudyMaterial) = dao.deleteMaterial(material)

    suspend fun updateFlashcardMastery(id: Long, isMastered: Boolean) {
        val all = dao.getAllFlashcards().firstOrNull() ?: return
        val target = all.find { it.id == id } ?: return
        dao.updateFlashcard(
            target.copy(
                isMastered = isMastered,
                reviewCount = target.reviewCount + 1
            )
        )
        if (isMastered) {
            addXp(25)
        }
    }

    suspend fun insertFlashcards(cards: List<Flashcard>) = dao.insertFlashcards(cards)

    suspend fun insertQuizQuestions(questions: List<QuizQuestion>) = dao.insertQuizQuestions(questions)

    suspend fun insertNote(note: StudyNote): Long = dao.insertNote(note)

    suspend fun updateNote(note: StudyNote) = dao.updateNote(note)

    suspend fun deleteNote(note: StudyNote) = dao.deleteNote(note)

    suspend fun insertTask(task: StudyTask): Long = dao.insertTask(task)

    suspend fun toggleTask(task: StudyTask) {
        val updated = task.copy(isCompleted = !task.isCompleted)
        dao.updateTask(updated)
        if (updated.isCompleted) {
            addXp(30)
        }
    }

    suspend fun deleteTask(task: StudyTask) = dao.deleteTask(task)

    suspend fun recordStudySession(durationMinutes: Int, sessionType: String, materialTitle: String) {
        val xp = (durationMinutes * 5).coerceAtLeast(20)
        val session = StudySession(
            durationMinutes = durationMinutes,
            sessionType = sessionType,
            materialTitle = materialTitle,
            timestamp = System.currentTimeMillis(),
            xpEarned = xp
        )
        dao.insertSession(session)
        addXp(xp, durationMinutes)
    }

    suspend fun addXp(xpAmount: Int, studyMinutes: Int = 0) {
        val profile = dao.getUserProfileOnce() ?: DemoData.initialProfile
        val newXp = profile.totalXp + xpAmount
        val newLevel = (newXp / 300) + 1
        val newStudied = profile.todayStudiedMinutes + studyMinutes
        dao.insertOrUpdateProfile(
            profile.copy(
                totalXp = newXp,
                level = newLevel,
                todayStudiedMinutes = newStudied
            )
        )
    }

    suspend fun toggleTaskReminder(taskId: Long, enabled: Boolean) {
        val all = dao.getAllTasks().firstOrNull() ?: return
        val target = all.find { it.id == taskId } ?: return
        dao.updateTask(target.copy(reminderEnabled = enabled))
    }

    suspend fun updateExamDetails(taskId: Long, title: String, subject: String, dueDate: String, dueTime: String, daysRemaining: Int, reminderEnabled: Boolean) {
        val all = dao.getAllTasks().firstOrNull() ?: return
        val target = all.find { it.id == taskId }
        if (target != null) {
            dao.updateTask(
                target.copy(
                    title = title,
                    subject = subject,
                    dueDate = dueDate,
                    dueTime = dueTime,
                    daysRemaining = daysRemaining,
                    reminderEnabled = reminderEnabled
                )
            )
        } else {
            dao.insertTask(
                StudyTask(
                    title = title,
                    subject = subject,
                    dueDate = dueDate,
                    dueTime = dueTime,
                    isExam = true,
                    daysRemaining = daysRemaining,
                    reminderEnabled = reminderEnabled
                )
            )
        }
    }

    suspend fun toggleMasterExamReminders(enabled: Boolean) {
        val profile = dao.getUserProfileOnce() ?: DemoData.initialProfile
        dao.insertOrUpdateProfile(profile.copy(examRemindersMasterToggle = enabled))
    }

    suspend fun resetAllDataToDefaults() {
        dao.clearMaterials()
        dao.clearFlashcards()
        dao.clearQuizQuestions()
        dao.clearNotes()
        dao.clearTasks()
        dao.clearSessions()

        // Re-seed clean demo data
        dao.insertMaterials(DemoData.initialMaterials)
        dao.insertFlashcards(DemoData.initialFlashcards)
        dao.insertQuizQuestions(DemoData.initialQuizQuestions)
        dao.insertTasks(DemoData.initialTasks)
        DemoData.initialNotes.forEach { dao.insertNote(it) }
        dao.insertOrUpdateProfile(DemoData.initialProfile)
    }

    suspend fun updateTheme(theme: String) {
        val profile = dao.getUserProfileOnce() ?: DemoData.initialProfile
        dao.insertOrUpdateProfile(profile.copy(selectedTheme = theme))
    }

    suspend fun updateAmbience(ambience: String) {
        val profile = dao.getUserProfileOnce() ?: DemoData.initialProfile
        dao.insertOrUpdateProfile(profile.copy(selectedAmbience = ambience))
    }

    suspend fun toggleAmbientAnimations(enabled: Boolean) {
        val profile = dao.getUserProfileOnce() ?: DemoData.initialProfile
        dao.insertOrUpdateProfile(profile.copy(ambientAnimationsEnabled = enabled))
    }

    suspend fun updateProfileSettings(name: String, educationLevel: String, targetSubjects: String, dailyGoal: Int) {
        val profile = dao.getUserProfileOnce() ?: DemoData.initialProfile
        dao.insertOrUpdateProfile(
            profile.copy(
                name = name,
                educationLevel = educationLevel,
                targetSubjects = targetSubjects,
                dailyGoalMinutes = dailyGoal
            )
        )
    }
}
