package com.example.ui.screens.focus

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DemoData
import com.example.ui.StudyViewModel
import com.example.ui.components.MiniMusicPlayerBar
import com.example.ui.theme.*

@Composable
fun FocusRoomScreen(
    viewModel: StudyViewModel,
    onNavigateToQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingSeconds by viewModel.timerRemainingSeconds.collectAsState()
    val totalSeconds by viewModel.timerTotalSeconds.collectAsState()
    val isRunning by viewModel.isTimerRunning.collectAsState()
    val isBreakMode by viewModel.isBreakMode.collectAsState()
    val focusPreset by viewModel.focusPreset.collectAsState()
    val showSummary by viewModel.showSessionSummary.collectAsState()
    val lastCompletedMin by viewModel.lastCompletedMinutes.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val currentMaterial by viewModel.selectedMaterial.collectAsState()

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val progress = if (totalSeconds > 0) (remainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f) else 1f

    val ambiences = listOf(
        Pair("CALM_GRADIENT", "Calm Gradient"),
        Pair("RAINY_WINDOW", "Rainy Window"),
        Pair("COZY_LIBRARY", "Midnight Library"),
        Pair("NIGHT_SKY", "Night Sky"),
        Pair("NATURE_CALM", "Quiet Forest"),
        Pair("MINIMAL_WORKSPACE", "Minimal Desk")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("focus_room_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- 1. Top Bar: Focus Room Header & Ambience Selector ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBreakMode) "Break Time ☕" else "Focus Room",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isBreakMode) "Waktunya relaksasi sejenak" else "Ruang belajar virtual imersif",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Preset Pills
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        FocusPresetChip(label = "25/5", isSelected = focusPreset == "25_5") {
                            viewModel.setFocusPreset("25_5")
                        }
                        FocusPresetChip(label = "45/10", isSelected = focusPreset == "45_10") {
                            viewModel.setFocusPreset("45_10")
                        }
                        FocusPresetChip(label = "50/10", isSelected = focusPreset == "50_10") {
                            viewModel.setFocusPreset("50_10")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current Studying Material Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Materi: ${currentMaterial?.title ?: "Sesi Belajar Mandiri"}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- 2. Center: Big Circular Pomodoro Timer ---
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(260.dp)
        ) {
            // Background track
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                trackColor = Color.Transparent
            )

            // Animated Countdown Progress
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
                color = if (isBreakMode) MintAccent else MaterialTheme.colorScheme.primary,
                trackColor = Color.Transparent
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 54.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isBreakMode) "Waktu Istirahat" else if (isRunning) "Sedang Fokus..." else "Siap Belajar",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isBreakMode) MintAccent else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // --- 3. Timer Control Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset / Stop
            IconButton(
                onClick = { viewModel.stopTimer() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Icon(Icons.Default.Stop, contentDescription = "Stop", tint = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Play / Pause Main Button
            FilledIconButton(
                onClick = {
                    if (isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                },
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .testTag("focus_timer_toggle_btn"),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isBreakMode) MintAccent else MaterialTheme.colorScheme.primary,
                    contentColor = DeepNavy
                )
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Play",
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Break button toggle
            IconButton(
                onClick = { viewModel.startBreak(5) },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Icon(Icons.Default.Coffee, contentDescription = "Break", tint = MaterialTheme.colorScheme.onSurface)
            }
        }

        // --- 4. Ambience Visual Selector & Mini Music Bar ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Suasana Ruang Belajar",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ambiences) { (key, label) ->
                    val isSelected = profile.selectedAmbience == key
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.setAmbience(key) },
                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary) else null,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mini Music Player Bar
            MiniMusicPlayerBar(viewModel = viewModel)
        }
    }

    // --- Break Time Modal / Mode Dialog ---
    if (isBreakMode && remainingSeconds == 0) {
        AlertDialog(
            onDismissRequest = { viewModel.setFocusPreset(focusPreset) },
            title = {
                Text("Good work. Take a little break.", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Sesi istirahatmu selesai! Apakah kamu ingin kembali fokus belajar atau menambah 5 menit istirahat?")
            },
            confirmButton = {
                Button(onClick = { viewModel.setFocusPreset(focusPreset) }) {
                    Text("Lanjut Belajar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.startBreak(5) }) {
                    Text("Perpanjang Istirahat (+5m)")
                }
            }
        )
    }

    // --- Session Complete Summary Modal ---
    if (showSummary) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSessionSummary() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Celebration, contentDescription = null, tint = GoldStar)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sesi Fokus Selesai!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text("Hebat! Kamu telah fokus belajar selama $lastCompletedMin menit.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "+${lastCompletedMin * 5} XP diperoleh! 🌟",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MintAccent
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Mau menguji pemahaman dengan mini quiz singkat?")
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.dismissSessionSummary()
                    onNavigateToQuiz()
                }) {
                    Text("Mulai Mini Quiz")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissSessionSummary() }) {
                    Text("Nanti Saja")
                }
            }
        )
    }
}

@Composable
fun FocusPresetChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isSelected) DeepNavy else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
