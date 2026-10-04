package com.example.ui.screens.workspace

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyMaterial
import com.example.ui.StudyViewModel
import com.example.ui.theme.*

@Composable
fun LearningWorkspaceScreen(
    viewModel: StudyViewModel,
    onBack: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val currentMaterial by viewModel.selectedMaterial.collectAsState()
    val materials by viewModel.materials.collectAsState()
    val aiResponse by viewModel.aiResponseText.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0: Materi, 1: AI Assistant, 2: Catatan, 3: Bab/Nav
    var customUserQuestion by remember { mutableStateOf("") }
    var newNoteContent by remember { mutableStateOf("") }
    var isFocusModeHidden by remember { mutableStateOf(false) } // hides extra panels

    val mat = currentMaterial ?: materials.firstOrNull()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .testTag("learning_workspace_screen")
    ) {
        val isWideScreen = maxWidth > 800.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // --- Workspace Header ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mat?.title ?: "Ruang Belajar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${mat?.subject ?: "Mata Pelajaran"} • ${mat?.topic ?: "Topik"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Toggle Focus (hide surrounding panels)
                    IconButton(onClick = { isFocusModeHidden = !isFocusModeHidden }) {
                        Icon(
                            imageVector = if (isFocusModeHidden) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Focus Mode",
                            tint = if (isFocusModeHidden) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- Mobile Tab Navigation (when not wide screen) ---
            if (!isWideScreen) {
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Materi", fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("AI Assistant", fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        text = { Text("Catatan", fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = activeTab == 3,
                        onClick = { activeTab = 3 },
                        text = { Text("Daftar Bab", fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
            }

            // --- AI Quick Action Pill Toolbar (Always Accessible) ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickActionChip(
                        title = "Ringkas",
                        icon = Icons.Default.Summarize,
                        onClick = {
                            activeTab = 1
                            viewModel.askAiWorkspace("SUMMARY")
                        }
                    )
                    QuickActionChip(
                        title = "Sederhana",
                        icon = Icons.Default.Lightbulb,
                        onClick = {
                            activeTab = 1
                            viewModel.askAiWorkspace("SIMPLIFY")
                        }
                    )
                    QuickActionChip(
                        title = "Contoh",
                        icon = Icons.Default.TravelExplore,
                        onClick = {
                            activeTab = 1
                            viewModel.askAiWorkspace("EXAMPLE")
                        }
                    )
                    QuickActionChip(
                        title = "Feynman",
                        icon = Icons.Default.RecordVoiceOver,
                        onClick = {
                            activeTab = 1
                            viewModel.askAiWorkspace("FEYNMAN", "Tolong pandu saya melakukan teknik Feynman untuk menguji pemahaman materi ini secara mandiri.")
                        }
                    )
                    QuickActionChip(
                        title = "Flashcard",
                        icon = Icons.Default.Style,
                        onClick = onNavigateToFlashcards
                    )
                    QuickActionChip(
                        title = "Quiz",
                        icon = Icons.Default.Quiz,
                        onClick = onNavigateToQuiz
                    )
                }
            }

            // --- Main Content Area (Responsive 3-Column on Tablet/Desktop, Tabs on Mobile) ---
            if (isWideScreen && !isFocusModeHidden) {
                Row(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    // LEFT: Topic & Material Navigation
                    Surface(
                        modifier = Modifier
                            .width(240.dp)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        WorkspaceLeftPanel(
                            materials = materials,
                            selectedId = mat?.id ?: 0,
                            onSelect = { viewModel.selectMaterial(it) }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // CENTER: Reader & Content
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        WorkspaceCenterReader(material = mat)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // RIGHT: AI Assistant & Quick Notes
                    Surface(
                        modifier = Modifier
                            .width(320.dp)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        WorkspaceRightAiPanel(
                            aiResponse = aiResponse,
                            isLoading = isAiLoading,
                            userQuestion = customUserQuestion,
                            onUserQuestionChange = { customUserQuestion = it },
                            onSendQuestion = {
                                viewModel.askAiWorkspace("CHAT", customUserQuestion)
                                customUserQuestion = ""
                            },
                            noteContent = newNoteContent,
                            onNoteChange = { newNoteContent = it },
                            onSaveNote = {
                                if (newNoteContent.isNotBlank()) {
                                    viewModel.addNote("Catatan: ${mat?.title}", newNoteContent)
                                    newNoteContent = ""
                                }
                            }
                        )
                    }
                }
            } else {
                // Mobile View: Render Selected Tab
                Box(modifier = Modifier.fillMaxSize()) {
                    when (activeTab) {
                        0 -> WorkspaceCenterReader(material = mat)
                        1 -> WorkspaceRightAiPanel(
                            aiResponse = aiResponse,
                            isLoading = isAiLoading,
                            userQuestion = customUserQuestion,
                            onUserQuestionChange = { customUserQuestion = it },
                            onSendQuestion = {
                                viewModel.askAiWorkspace("CHAT", customUserQuestion)
                                customUserQuestion = ""
                            },
                            noteContent = newNoteContent,
                            onNoteChange = { newNoteContent = it },
                            onSaveNote = {
                                if (newNoteContent.isNotBlank()) {
                                    viewModel.addNote("Catatan: ${mat?.title}", newNoteContent)
                                    newNoteContent = ""
                                }
                            }
                        )
                        2 -> WorkspaceNotesTab(
                            noteContent = newNoteContent,
                            onNoteChange = { newNoteContent = it },
                            onSaveNote = {
                                if (newNoteContent.isNotBlank()) {
                                    viewModel.addNote("Catatan: ${mat?.title}", newNoteContent)
                                    newNoteContent = ""
                                }
                            }
                        )
                        else -> WorkspaceLeftPanel(
                            materials = materials,
                            selectedId = mat?.id ?: 0,
                            onSelect = {
                                viewModel.selectMaterial(it)
                                activeTab = 0
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium, fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun WorkspaceCenterReader(material: StudyMaterial?) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        if (material == null) {
            Text("Pilih materi untuk mulai membaca.")
            return@Column
        }

        // Summary Callout Box
        if (material.summary.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp)) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Ringkasan Cepat AI",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = material.summary,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Main Document Content with generous line height for reading comfort
        Text(
            text = material.content,
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 24.sp,
                fontSize = 15.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun WorkspaceLeftPanel(
    materials: List<StudyMaterial>,
    selectedId: Long,
    onSelect: (StudyMaterial) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Daftar Materi & Bab",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(materials) { m ->
            val isSelected = m.id == selectedId
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelect(m) },
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = m.subject,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = m.title,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
fun WorkspaceRightAiPanel(
    aiResponse: String,
    isLoading: Boolean,
    userQuestion: String,
    onUserQuestionChange: (String) -> Unit,
    onSendQuestion: () -> Unit,
    noteContent: String,
    onNoteChange: (String) -> Unit,
    onSaveNote: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AI Study Assistant",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // AI Response Box
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Box(modifier = Modifier.padding(14.dp)) {
                if (isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("AI sedang menganalisis materi...", style = MaterialTheme.typography.bodySmall)
                    }
                } else if (aiResponse.isNotBlank()) {
                    Text(
                        text = aiResponse,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        text = "Gunakan tombol cepat di atas atau ketik pertanyaan di bawah untuk berdiskusi dengan AI Tutor mengenai bab ini.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Question to AI
        OutlinedTextField(
            value = userQuestion,
            onValueChange = onUserQuestionChange,
            placeholder = { Text("Tanyakan apa pun tentang materi ini...", fontSize = 12.sp) },
            trailingIcon = {
                IconButton(onClick = onSendQuestion) {
                    Icon(Icons.Default.Send, contentDescription = "Kirim", tint = MaterialTheme.colorScheme.primary)
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("input_ask_ai_workspace"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Notes Section
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Catatan Cepat",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            TextButton(onClick = onSaveNote) {
                Text("Simpan", fontSize = 12.sp)
            }
        }

        OutlinedTextField(
            value = noteContent,
            onValueChange = onNoteChange,
            placeholder = { Text("Tulis ide atau poin penting di sini...", fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(10.dp),
            maxLines = 4
        )
    }
}

@Composable
fun WorkspaceNotesTab(
    noteContent: String,
    onNoteChange: (String) -> Unit,
    onSaveNote: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Text(
            text = "Catatan Belajar Cepat",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = noteContent,
            onValueChange = onNoteChange,
            placeholder = { Text("Tulis poin ringkasan, rumus penting, atau pertanyaan mandiri...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(12.dp),
            maxLines = 7
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSaveNote,
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Simpan ke Catatan")
        }
    }
}
