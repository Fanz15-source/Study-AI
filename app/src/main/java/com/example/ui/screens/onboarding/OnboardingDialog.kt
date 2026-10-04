package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun OnboardingDialog(
    viewModel: StudyViewModel,
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var selectedLevel by remember { mutableStateOf("Universitas") }
    var selectedGoal by remember { mutableStateOf(45) }
    val selectedSubjects = remember { mutableStateListOf("Biologi", "Fisika", "Matematika") }

    val levels = listOf("SMP", "SMA / SMK", "Universitas", "Profesional / Karir")
    val subjectsList = listOf("Biologi", "Fisika", "Matematika", "Sejarah", "Informatika", "Kimia", "Ekonomi", "Bahasa")
    val goals = listOf(25, 45, 60, 90)

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Column {
                Text(
                    text = "Selamat Datang di AI Study Hub",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Langkah $step dari 3 • Personalisasi Ruang Belajarmu",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (step) {
                    1 -> {
                        Text(
                            text = "Pilih Tingkat Pendidikanmu:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        levels.forEach { lvl ->
                            val isSelected = selectedLevel == lvl
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedLevel = lvl },
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(selected = isSelected, onClick = { selectedLevel = lvl })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(lvl, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                    2 -> {
                        Text(
                            text = "Mata Pelajaran yang Sedang Dipelajari:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            subjectsList.chunked(2).forEach { rowSubjs ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowSubjs.forEach { subj ->
                                        val isChecked = selectedSubjects.contains(subj)
                                        FilterChip(
                                            selected = isChecked,
                                            onClick = {
                                                if (isChecked) selectedSubjects.remove(subj)
                                                else selectedSubjects.add(subj)
                                            },
                                            label = { Text(subj, fontSize = 11.sp) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    3 -> {
                        Text(
                            text = "Target Waktu Belajar Harian:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            goals.forEach { g ->
                                val isSelected = selectedGoal == g
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedGoal = g },
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 12.dp)) {
                                        Text(
                                            text = "$g mnt",
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) DeepNavy else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✨ Demo materi akademik siap pakai telah disiapkan di perpustakaanmu.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (step < 3) {
                Button(onClick = { step++ }) {
                    Text("Lanjut")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            name = "Alex",
                            educationLevel = selectedLevel,
                            targetSubjects = selectedSubjects.joinToString(", "),
                            dailyGoal = selectedGoal
                        )
                        onDismiss()
                    }
                ) {
                    Text("Mulai Belajar 🚀")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Lewati")
            }
        }
    )
}
