package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyMaterial
import com.example.ui.components.AmbienceBackground
import com.example.ui.components.ConfettiOverlay
import com.example.ui.components.MiniMusicPlayerBar
import com.example.ui.screens.dashboard.HomeScreen
import com.example.ui.screens.exam.ExamSimulationScreen
import com.example.ui.screens.flashcards.FlashcardsScreen
import com.example.ui.screens.focus.FocusRoomScreen
import com.example.ui.screens.materials.MaterialLibraryScreen
import com.example.ui.screens.materials.UploadMaterialDialog
import com.example.ui.screens.notes.NotesScreen
import com.example.ui.screens.onboarding.OnboardingDialog
import com.example.ui.screens.planner.ExamReminderDialog
import com.example.ui.screens.planner.StudyPlannerScreen
import com.example.ui.screens.progress.ProgressGamificationScreen
import com.example.ui.screens.quiz.QuizScreen
import com.example.ui.screens.settings.SettingsDialog
import com.example.ui.screens.tutor.AiTutorScreen
import com.example.ui.screens.workspace.CheatSheetDialog
import com.example.ui.screens.workspace.LearningWorkspaceScreen
import com.example.ui.theme.*

enum class StudyDestination(val label: String) {
    HOME("Home"),
    MATERIALS("My Materials"),
    WORKSPACE("Workspace"),
    AI_TUTOR("AI Tutor"),
    FLASHCARDS("Flashcards"),
    QUIZ("Quiz & Practice"),
    EXAM_SIM("Exam Simulation"),
    PLANNER("Study Planner"),
    FOCUS_ROOM("Focus Room"),
    NOTES("Notes"),
    PROGRESS("Progress")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: StudyViewModel) {
    var currentDestination by remember { mutableStateOf(StudyDestination.HOME) }
    val profile by viewModel.userProfile.collectAsState()
    val showConfetti by viewModel.showConfetti.collectAsState()

    var showUploadDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showExamReminderDialog by remember { mutableStateOf(false) }
    var showCheatSheetDialog by remember { mutableStateOf(false) }
    var showMoreBottomSheet by remember { mutableStateOf(false) }
    var showOnboarding by remember { mutableStateOf(false) }

    val isDark = profile.selectedTheme == "DARK_FOCUS"

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpandedScreen = maxWidth > 840.dp

        AmbienceBackground(
            ambience = profile.selectedAmbience,
            isDark = isDark,
            animationsEnabled = profile.ambientAnimationsEnabled
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets.safeDrawing,
                bottomBar = {
                    if (!isExpandedScreen && currentDestination != StudyDestination.WORKSPACE) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            // Mini Music Player Bar above bottom bar
                            MiniMusicPlayerBar(viewModel = viewModel)

                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentDestination == StudyDestination.HOME,
                                    onClick = { currentDestination = StudyDestination.HOME },
                                    icon = {
                                        Icon(
                                            if (currentDestination == StudyDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                            contentDescription = "Home"
                                        )
                                    },
                                    label = { Text("Home", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("nav_home")
                                )

                                NavigationBarItem(
                                    selected = currentDestination == StudyDestination.MATERIALS,
                                    onClick = { currentDestination = StudyDestination.MATERIALS },
                                    icon = {
                                        Icon(
                                            if (currentDestination == StudyDestination.MATERIALS) Icons.Filled.LibraryBooks else Icons.Outlined.LibraryBooks,
                                            contentDescription = "Materi"
                                        )
                                    },
                                    label = { Text("Materi", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("nav_materials")
                                )

                                NavigationBarItem(
                                    selected = currentDestination == StudyDestination.FOCUS_ROOM,
                                    onClick = { currentDestination = StudyDestination.FOCUS_ROOM },
                                    icon = {
                                        Icon(
                                            if (currentDestination == StudyDestination.FOCUS_ROOM) Icons.Filled.HourglassBottom else Icons.Outlined.HourglassEmpty,
                                            contentDescription = "Focus Room"
                                        )
                                    },
                                    label = { Text("Focus", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("nav_focus")
                                )

                                NavigationBarItem(
                                    selected = currentDestination == StudyDestination.AI_TUTOR,
                                    onClick = { currentDestination = StudyDestination.AI_TUTOR },
                                    icon = {
                                        Icon(
                                            if (currentDestination == StudyDestination.AI_TUTOR) Icons.Filled.Psychology else Icons.Outlined.Psychology,
                                            contentDescription = "AI Tutor"
                                        )
                                    },
                                    label = { Text("AI Tutor", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("nav_tutor")
                                )

                                NavigationBarItem(
                                    selected = showMoreBottomSheet,
                                    onClick = { showMoreBottomSheet = true },
                                    icon = { Icon(Icons.Default.Menu, contentDescription = "More") },
                                    label = { Text("Menu", fontSize = 11.sp) },
                                    modifier = Modifier.testTag("nav_more")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Desktop / Tablet Navigation Rail
                    if (isExpandedScreen) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                            header = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 8.dp)) {
                                    Surface(
                                        modifier = Modifier.size(42.dp),
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.School, contentDescription = null, tint = DeepNavy)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                    FloatingActionButton(
                                        onClick = { showUploadDialog = true },
                                        containerColor = SoftLavender,
                                        contentColor = DeepNavy,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Add Material")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))

                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.HOME,
                                onClick = { currentDestination = StudyDestination.HOME },
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                label = { Text("Home", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.MATERIALS,
                                onClick = { currentDestination = StudyDestination.MATERIALS },
                                icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
                                label = { Text("Materi", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.FOCUS_ROOM,
                                onClick = { currentDestination = StudyDestination.FOCUS_ROOM },
                                icon = { Icon(Icons.Default.Timer, contentDescription = null) },
                                label = { Text("Focus", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.AI_TUTOR,
                                onClick = { currentDestination = StudyDestination.AI_TUTOR },
                                icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                                label = { Text("Tutor", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.FLASHCARDS,
                                onClick = { currentDestination = StudyDestination.FLASHCARDS },
                                icon = { Icon(Icons.Default.Style, contentDescription = null) },
                                label = { Text("Flashcard", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.QUIZ,
                                onClick = { currentDestination = StudyDestination.QUIZ },
                                icon = { Icon(Icons.Default.Quiz, contentDescription = null) },
                                label = { Text("Quiz", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.EXAM_SIM,
                                onClick = { currentDestination = StudyDestination.EXAM_SIM },
                                icon = { Icon(Icons.Default.Assessment, contentDescription = null) },
                                label = { Text("Ujian", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.PLANNER,
                                onClick = { currentDestination = StudyDestination.PLANNER },
                                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                                label = { Text("Planner", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.NOTES,
                                onClick = { currentDestination = StudyDestination.NOTES },
                                icon = { Icon(Icons.Default.EditNote, contentDescription = null) },
                                label = { Text("Notes", fontSize = 10.sp) }
                            )
                            NavigationRailItem(
                                selected = currentDestination == StudyDestination.PROGRESS,
                                onClick = { currentDestination = StudyDestination.PROGRESS },
                                icon = { Icon(Icons.Default.Leaderboard, contentDescription = null) },
                                label = { Text("Progress", fontSize = 10.sp) }
                            )
                        }
                    }

                    // Main Destination Views
                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        when (currentDestination) {
                            StudyDestination.HOME -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToWorkspace = { mat ->
                                    if (mat != null) viewModel.selectMaterial(mat)
                                    currentDestination = StudyDestination.WORKSPACE
                                },
                                onNavigateToFocusRoom = { currentDestination = StudyDestination.FOCUS_ROOM },
                                onNavigateToAiTutor = { currentDestination = StudyDestination.AI_TUTOR },
                                onNavigateToMaterials = { currentDestination = StudyDestination.MATERIALS },
                                onOpenUploadDialog = { showUploadDialog = true },
                                onOpenSettings = { showSettingsDialog = true }
                            )

                            StudyDestination.MATERIALS -> MaterialLibraryScreen(
                                viewModel = viewModel,
                                onSelectMaterial = { mat ->
                                    viewModel.selectMaterial(mat)
                                    currentDestination = StudyDestination.WORKSPACE
                                },
                                onOpenUploadDialog = { showUploadDialog = true }
                            )

                            StudyDestination.WORKSPACE -> LearningWorkspaceScreen(
                                viewModel = viewModel,
                                onBack = { currentDestination = StudyDestination.MATERIALS },
                                onNavigateToFlashcards = { currentDestination = StudyDestination.FLASHCARDS },
                                onNavigateToQuiz = { currentDestination = StudyDestination.QUIZ }
                            )

                            StudyDestination.AI_TUTOR -> AiTutorScreen(viewModel = viewModel)

                            StudyDestination.FOCUS_ROOM -> FocusRoomScreen(
                                viewModel = viewModel,
                                onNavigateToQuiz = { currentDestination = StudyDestination.QUIZ }
                            )

                            StudyDestination.FLASHCARDS -> FlashcardsScreen(viewModel = viewModel)

                            StudyDestination.QUIZ -> QuizScreen(viewModel = viewModel)

                            StudyDestination.EXAM_SIM -> ExamSimulationScreen(viewModel = viewModel)

                            StudyDestination.PLANNER -> StudyPlannerScreen(viewModel = viewModel)

                            StudyDestination.NOTES -> NotesScreen(viewModel = viewModel)

                            StudyDestination.PROGRESS -> ProgressGamificationScreen(viewModel = viewModel)
                        }
                    }
                }
            }

            // Confetti Layer
            ConfettiOverlay(visible = showConfetti)
        }
    }

    // --- More Menu Modal Bottom Sheet (Mobile) ---
    if (showMoreBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreBottomSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Semua Fitur Belajar",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                MoreMenuItem(icon = Icons.Default.Style, title = "Flashcards Interaktif", subtitle = "Latihan memori cepat & hafalan") {
                    currentDestination = StudyDestination.FLASHCARDS
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.Quiz, title = "Quiz & Practice", subtitle = "Uji pemahaman dengan pembahasan AI") {
                    currentDestination = StudyDestination.QUIZ
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.Assessment, title = "Exam Prediction & Simulation", subtitle = "Simulasi ujian nyata & prediksi skor") {
                    currentDestination = StudyDestination.EXAM_SIM
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.CalendarMonth, title = "Study Planner", subtitle = "Jadwal harian & pengingat ujian") {
                    currentDestination = StudyDestination.PLANNER
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.EditNote, title = "Notes & Insights", subtitle = "Buku catatan materi digital") {
                    currentDestination = StudyDestination.NOTES
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.NotificationsActive, title = "Pengingat Ujian", subtitle = "Atur tanggal & nyalakan notifikasi ujian") {
                    showExamReminderDialog = true
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.Functions, title = "Kamus Rumus & Cheat Sheet", subtitle = "Kumpulan rumus & konsep kunci kilat") {
                    showCheatSheetDialog = true
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.Leaderboard, title = "Progress & Gamification", subtitle = "XP, Streak, level & medali prestasi") {
                    currentDestination = StudyDestination.PROGRESS
                    showMoreBottomSheet = false
                }
                MoreMenuItem(icon = Icons.Default.Settings, title = "Pengaturan & Suasana", subtitle = "Ganti tema, reset data, dan profil") {
                    showSettingsDialog = true
                    showMoreBottomSheet = false
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // --- Upload Material Dialog ---
    if (showUploadDialog) {
        UploadMaterialDialog(
            viewModel = viewModel,
            onDismiss = { showUploadDialog = false }
        )
    }

    // --- Settings Dialog ---
    if (showSettingsDialog) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { showSettingsDialog = false }
        )
    }

    // --- Exam Reminder Dialog ---
    if (showExamReminderDialog) {
        ExamReminderDialog(
            viewModel = viewModel,
            onDismiss = { showExamReminderDialog = false }
        )
    }

    // --- Cheat Sheet Dialog ---
    if (showCheatSheetDialog) {
        CheatSheetDialog(
            viewModel = viewModel,
            onDismiss = { showCheatSheetDialog = false }
        )
    }

    // --- Onboarding Dialog on First Launch ---
    if (showOnboarding) {
        OnboardingDialog(
            viewModel = viewModel,
            onDismiss = { showOnboarding = false }
        )
    }
}

@Composable
fun MoreMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
