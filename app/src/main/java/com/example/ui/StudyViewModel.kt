package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiService
import com.example.data.audio.AmbienceAudioPlayer
import com.example.data.local.AppDatabase
import com.example.data.local.DemoData
import com.example.data.model.*
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = StudyRepository(database.studyDao())
    private val geminiService = GeminiService()
    val audioPlayer = AmbienceAudioPlayer()

    val materials: StateFlow<List<StudyMaterial>> = repository.allMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashcards: StateFlow<List<Flashcard>> = repository.allFlashcards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizQuestions: StateFlow<List<QuizQuestion>> = repository.allQuizQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<StudyNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<StudyTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<StudySession>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .map { it ?: DemoData.initialProfile }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DemoData.initialProfile)

    private val _selectedMaterial = MutableStateFlow<StudyMaterial?>(null)
    val selectedMaterial: StateFlow<StudyMaterial?> = _selectedMaterial.asStateFlow()

    // Focus Room & Timer State
    private val _timerRemainingSeconds = MutableStateFlow(25 * 60)
    val timerRemainingSeconds: StateFlow<Int> = _timerRemainingSeconds.asStateFlow()

    private val _timerTotalSeconds = MutableStateFlow(25 * 60)
    val timerTotalSeconds: StateFlow<Int> = _timerTotalSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _isBreakMode = MutableStateFlow(false)
    val isBreakMode: StateFlow<Boolean> = _isBreakMode.asStateFlow()

    private val _focusPreset = MutableStateFlow("25_5") // 25_5, 45_10, 50_10, CUSTOM
    val focusPreset: StateFlow<String> = _focusPreset.asStateFlow()

    private val _showSessionSummary = MutableStateFlow(false)
    val showSessionSummary: StateFlow<Boolean> = _showSessionSummary.asStateFlow()

    private val _lastCompletedMinutes = MutableStateFlow(25)
    val lastCompletedMinutes: StateFlow<Int> = _lastCompletedMinutes.asStateFlow()

    private var timerJob: Job? = null
    private var timerEndEpoch: Long = 0L

    // Audio & Music State
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

    private val _currentTrack = MutableStateFlow(DemoData.musicTracks.first())
    val currentTrack: StateFlow<MusicTrack> = _currentTrack.asStateFlow()

    private val _audioVolume = MutableStateFlow(0.5f)
    val audioVolume: StateFlow<Float> = _audioVolume.asStateFlow()

    // AI Workspace & Tutor State
    private val _aiResponseText = MutableStateFlow("")
    val aiResponseText: StateFlow<String> = _aiResponseText.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            Pair(false, "Halo! Saya AI Study Tutor. Ada topik atau bagian materi yang ingin kamu diskusikan atau tanyakan?"),
            Pair(true, "Bisa jelaskan perbedaan DNA polimerase I dan III dengan mudah?"),
            Pair(false, "Tentu! DNA Polimerase III adalah pekerja utama yang membangun untai DNA baru dengan cepat. Sedangkan DNA Polimerase I adalah tim pemeriksa/pembersih yang membuang primer RNA dan menggantinya dengan DNA.")
        )
    )
    val chatMessages: StateFlow<List<Pair<Boolean, String>>> = _chatMessages.asStateFlow()

    // Formula Cheat Sheet & Spaced Repetition State
    val cheatSheets: StateFlow<List<CheatSheetItem>> = MutableStateFlow(DemoData.initialCheatSheets).asStateFlow()
    private val _spacedRepetitions = MutableStateFlow(DemoData.initialSpacedRepetition)
    val spacedRepetitions: StateFlow<List<SpacedRepetitionItem>> = _spacedRepetitions.asStateFlow()

    // Confetti celebration trigger
    private val _showConfetti = MutableStateFlow(false)
    val showConfetti: StateFlow<Boolean> = _showConfetti.asStateFlow()

    init {
        // Automatically select first material once materials load
        viewModelScope.launch {
            materials.collect { list ->
                if (_selectedMaterial.value == null && list.isNotEmpty()) {
                    _selectedMaterial.value = list.first()
                }
            }
        }
    }

    fun selectMaterial(material: StudyMaterial) {
        _selectedMaterial.value = material
    }

    // --- AI Assistance Actions ---
    fun askAiWorkspace(actionType: String, customPrompt: String = "") {
        val currentMat = _selectedMaterial.value
        val materialContent = currentMat?.content.orEmpty()
        val prompt = if (customPrompt.isNotBlank()) customPrompt else "Tolong bantu dengan fitur $actionType untuk materi ini."

        _isAiLoading.value = true
        _aiResponseText.value = ""

        viewModelScope.launch {
            val result = geminiService.generateStudyAid(
                prompt = prompt,
                contextMaterial = materialContent,
                actionType = actionType
            )
            _aiResponseText.value = result
            _isAiLoading.value = false
            repository.addXp(15)
        }
    }

    fun sendTutorChat(userMessage: String) {
        if (userMessage.isBlank()) return
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(Pair(true, userMessage))
        _chatMessages.value = currentList
        _isAiLoading.value = true

        viewModelScope.launch {
            val reply = geminiService.generateStudyAid(
                prompt = userMessage,
                contextMaterial = _selectedMaterial.value?.content.orEmpty(),
                actionType = "CHAT"
            )
            val updated = _chatMessages.value.toMutableList()
            updated.add(Pair(false, reply))
            _chatMessages.value = updated
            _isAiLoading.value = false
            repository.addXp(10)
        }
    }

    // --- Focus Room Pomodoro Timer ---
    fun setFocusPreset(preset: String) {
        _focusPreset.value = preset
        val focusMinutes = when (preset) {
            "25_5" -> 25
            "45_10" -> 45
            "50_10" -> 50
            else -> 30
        }
        val seconds = focusMinutes * 60
        _timerTotalSeconds.value = seconds
        _timerRemainingSeconds.value = seconds
        _isBreakMode.value = false
        stopTimer()
    }

    fun startTimer() {
        if (_isTimerRunning.value) return
        _isTimerRunning.value = true
        timerEndEpoch = SystemClock.elapsedRealtime() + (_timerRemainingSeconds.value * 1000L)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _isTimerRunning.value) {
                val remainingMs = timerEndEpoch - SystemClock.elapsedRealtime()
                val remSeconds = (remainingMs / 1000L).toInt().coerceAtLeast(0)
                _timerRemainingSeconds.value = remSeconds

                if (remSeconds <= 0) {
                    onTimerFinished()
                    break
                }
                delay(500)
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun stopTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        _timerRemainingSeconds.value = _timerTotalSeconds.value
    }

    private fun onTimerFinished() {
        _isTimerRunning.value = false
        timerJob?.cancel()

        if (!_isBreakMode.value) {
            // Focus session finished
            val durationMin = _timerTotalSeconds.value / 60
            _lastCompletedMinutes.value = durationMin
            _showSessionSummary.value = true
            triggerConfetti()

            val matTitle = _selectedMaterial.value?.title ?: "Sesi Belajar Mandiri"
            viewModelScope.launch {
                repository.recordStudySession(durationMin, "Focus", matTitle)
            }
        } else {
            // Break finished -> switch to focus
            setFocusPreset(_focusPreset.value)
        }
    }

    fun startBreak(minutes: Int = 5) {
        _showSessionSummary.value = false
        _isBreakMode.value = true
        val breakSec = minutes * 60
        _timerTotalSeconds.value = breakSec
        _timerRemainingSeconds.value = breakSec
        startTimer()
    }

    fun dismissSessionSummary() {
        _showSessionSummary.value = false
        _timerRemainingSeconds.value = _timerTotalSeconds.value
    }

    // --- Audio Ambience & Music ---
    fun togglePlayAudio() {
        if (_isAudioPlaying.value) {
            audioPlayer.stop()
            _isAudioPlaying.value = false
        } else {
            val track = _currentTrack.value
            if (track.platform == "BUILT_IN") {
                audioPlayer.start(track.synthMode, _audioVolume.value)
                _isAudioPlaying.value = true
            }
        }
    }

    fun selectTrack(track: MusicTrack, context: Context) {
        _currentTrack.value = track
        if (track.platform == "BUILT_IN") {
            audioPlayer.start(track.synthMode, _audioVolume.value)
            _isAudioPlaying.value = true
        } else {
            audioPlayer.stop()
            _isAudioPlaying.value = false
            // Launch external service (Spotify or YouTube Music)
            openExternalMusic(track, context)
        }
    }

    fun setVolume(vol: Float) {
        _audioVolume.value = vol
        audioPlayer.setVolume(vol)
    }

    private fun openExternalMusic(track: MusicTrack, context: Context) {
        try {
            val uri = Uri.parse(track.externalUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback: search on web
            val fallbackUri = Uri.parse("https://google.com/search?q=" + Uri.encode(track.title))
            val intent = Intent(Intent.ACTION_VIEW, fallbackUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    // --- Gamification & Flashcards ---
    fun markFlashcard(id: Long, isMastered: Boolean) {
        viewModelScope.launch {
            repository.updateFlashcardMastery(id, isMastered)
            if (isMastered) triggerConfetti()
        }
    }

    fun toggleTask(task: StudyTask) {
        viewModelScope.launch {
            repository.toggleTask(task)
        }
    }

    fun addNewMaterial(
        title: String,
        subject: String,
        topic: String,
        content: String,
        fileType: String = "PDF"
    ) {
        viewModelScope.launch {
            val newMat = StudyMaterial(
                title = title,
                subject = subject,
                topic = topic,
                fileType = fileType,
                dateAdded = "Baru saja",
                lastStudied = "Hari ini",
                progress = 0.1f,
                content = content,
                summary = "Materi $title berhasil diunggah dan siap dipelajari."
            )
            val id = repository.insertMaterial(newMat)
            repository.addXp(50)
            triggerConfetti()
            _selectedMaterial.value = newMat.copy(id = id)
        }
    }

    fun addNote(title: String, content: String, tags: String = "Catatan") {
        viewModelScope.launch {
            repository.insertNote(
                StudyNote(
                    materialId = _selectedMaterial.value?.id,
                    title = title,
                    content = content,
                    tags = tags
                )
            )
            repository.addXp(20)
        }
    }

    fun deleteNote(note: StudyNote) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun setTheme(themeKey: String) {
        viewModelScope.launch {
            repository.updateTheme(themeKey)
        }
    }

    fun toggleTaskReminder(taskId: Long, enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskReminder(taskId, enabled)
        }
    }

    fun saveExam(
        taskId: Long,
        title: String,
        subject: String,
        dueDate: String,
        dueTime: String,
        daysRemaining: Int,
        reminderEnabled: Boolean
    ) {
        viewModelScope.launch {
            repository.updateExamDetails(
                taskId = taskId,
                title = title,
                subject = subject,
                dueDate = dueDate,
                dueTime = dueTime,
                daysRemaining = daysRemaining,
                reminderEnabled = reminderEnabled
            )
            triggerConfetti()
        }
    }

    fun toggleMasterExamReminders(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleMasterExamReminders(enabled)
        }
    }

    fun reviewSpacedRepetition(id: Long) {
        val current = _spacedRepetitions.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = current[index]
            current[index] = item.copy(memoryRetention = 98, isDueToday = false)
            _spacedRepetitions.value = current
            viewModelScope.launch {
                repository.addXp(40)
            }
            triggerConfetti()
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllDataToDefaults()
            _selectedMaterial.value = null
            _aiResponseText.value = ""
            _spacedRepetitions.value = DemoData.initialSpacedRepetition
            triggerConfetti()
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            repository.updateTheme(if (isDark) "DARK_FOCUS" else "LIGHT_CALM")
        }
    }

    fun setAmbience(ambience: String) {
        viewModelScope.launch {
            repository.updateAmbience(ambience)
        }
    }

    fun toggleAmbientAnimations(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleAmbientAnimations(enabled)
        }
    }

    fun updateProfile(name: String, educationLevel: String, targetSubjects: String, dailyGoal: Int) {
        viewModelScope.launch {
            repository.updateProfileSettings(name, educationLevel, targetSubjects, dailyGoal)
        }
    }

    fun triggerConfetti() {
        _showConfetti.value = true
        viewModelScope.launch {
            delay(3500)
            _showConfetti.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
        timerJob?.cancel()
    }
}
