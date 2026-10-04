package com.example.ui.screens.settings

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StudyViewModel
import com.example.ui.theme.*

@Composable
fun SettingsDialog(
    viewModel: StudyViewModel,
    onDismiss: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    var selectedThemeKey by remember { mutableStateOf(profile.selectedTheme) }
    var animationsEnabled by remember { mutableStateOf(profile.ambientAnimationsEnabled) }
    var selectedAmbience by remember { mutableStateOf(profile.selectedAmbience) }
    var nameInput by remember { mutableStateOf(profile.name) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val themes = listOf(
        Triple("DARK_FOCUS", "🌙 Dark Focus", DeepNavy),
        Triple("LIGHT_CALM", "☀️ Light Calm", WarmCream),
        Triple("CYBER_INDIGO", "⚡ Cyber Indigo", CyberBg),
        Triple("FOREST_MATCHA", "🍵 Forest Matcha", ForestBg),
        Triple("TWILIGHT_AMBER", "🌅 Twilight Amber", TwilightBg)
    )

    val ambiences = listOf(
        Pair("CALM_GRADIENT", "Calm Gradient"),
        Pair("RAINY_WINDOW", "Rainy Window"),
        Pair("COZY_LIBRARY", "Cozy Library"),
        Pair("NIGHT_SKY", "Night Sky"),
        Pair("NATURE_CALM", "Nature Calm"),
        Pair("MINIMAL_WORKSPACE", "Minimal Desk")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Tune, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pengaturan Ruang Belajar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Name
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nama Pengguna") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Theme Selection (5 Themes)
                Text("Tema Warna Interface:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    themes.chunked(2).forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { (key, label, colorIndicator) ->
                                val isSelected = selectedThemeKey == key
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            selectedThemeKey = key
                                            viewModel.setTheme(key)
                                        },
                                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(CircleShape)
                                                .background(colorIndicator)
                                                .border(1.dp, Color.Gray, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = label,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                            if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Ambience Selection
                Text("Pilihan Suasana Ambience:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    ambiences.chunked(2).forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { (key, label) ->
                                val isSelected = selectedAmbience == key
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedAmbience = key
                                        viewModel.setAmbience(key)
                                    },
                                    label = { Text(label, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Ambient Animations Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Animasi Partikel Ambient", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        Text("Efek hujan/bintang latar belakang", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = animationsEnabled,
                        onCheckedChange = {
                            animationsEnabled = it
                            viewModel.toggleAmbientAnimations(it)
                        }
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Tombol Reset Data
                OutlinedButton(
                    onClick = { showResetConfirm = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonError),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset Semua Data Belajar ke Awal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameInput.isNotBlank()) {
                        viewModel.updateProfile(
                            name = nameInput,
                            educationLevel = profile.educationLevel,
                            targetSubjects = profile.targetSubjects,
                            dailyGoal = profile.dailyGoalMinutes
                        )
                    }
                    onDismiss()
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )

    // Konfirmasi Reset Data Dialog
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Konfirmasi Reset Data", fontWeight = FontWeight.Bold) },
            text = {
                Text("Seluruh catatan kustom, progres materi, dan sesi akan direset kembali ke materi demo awal yang bersih. Lanjutkan?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllData()
                        showResetConfirm = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonError)
                ) {
                    Text("Ya, Reset Data")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
