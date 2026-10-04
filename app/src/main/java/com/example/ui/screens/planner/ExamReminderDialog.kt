package com.example.ui.screens.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.StudyTask
import com.example.ui.StudyViewModel
import com.example.ui.theme.*

@Composable
fun ExamReminderDialog(
    viewModel: StudyViewModel,
    onDismiss: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    val examTasks = tasks.filter { it.isExam }

    var isAddingNewExam by remember { mutableStateOf(false) }
    var examTitle by remember { mutableStateOf("") }
    var examSubject by remember { mutableStateOf("Fisika") }
    var examDate by remember { mutableStateOf("12 Oktober 2026") }
    var examTime by remember { mutableStateOf("08:00 WIB") }
    var daysRemainingInput by remember { mutableStateOf(5) }
    var reminderEnabled by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = OrangeWarning
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pengingat Ujian", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                IconButton(onClick = { isAddingNewExam = !isAddingNewExam }) {
                    Icon(
                        imageVector = if (isAddingNewExam) Icons.Default.Close else Icons.Default.AddCircle,
                        contentDescription = "Tambah Ujian",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Master Switch: Aktifkan Semua Notifikasi Pengingat Ujian
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Semua Pengingat Ujian",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (profile.examRemindersMasterToggle) "Aktif • Notifikasi H-3 & H-1 menyala" else "Nonaktif • Tidak ada banner pengingat",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (profile.examRemindersMasterToggle) MintAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = profile.examRemindersMasterToggle,
                            onCheckedChange = { viewModel.toggleMasterExamReminders(it) },
                            modifier = Modifier.testTag("master_exam_reminder_switch")
                        )
                    }
                }

                if (isAddingNewExam) {
                    // Form Tambah / Set Tanggal Ujian Baru
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Atur Jadwal Ujian Baru",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            )

                            OutlinedTextField(
                                value = examTitle,
                                onValueChange = { examTitle = it },
                                label = { Text("Nama Ujian / Mata Kuliah") },
                                placeholder = { Text("Misal: UTS Fisika Termodinamika") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("input_exam_title")
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = examSubject,
                                    onValueChange = { examSubject = it },
                                    label = { Text("Mapel") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = daysRemainingInput.toString(),
                                    onValueChange = { daysRemainingInput = it.toIntOrNull() ?: 1 },
                                    label = { Text("Sisa Hari (H-)") },
                                    singleLine = true,
                                    modifier = Modifier.width(90.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = examDate,
                                    onValueChange = { examDate = it },
                                    label = { Text("Tanggal Pelaksanaan") },
                                    placeholder = { Text("Contoh: 12 Oktober 2026") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.5f).testTag("input_exam_date")
                                )
                                OutlinedTextField(
                                    value = examTime,
                                    onValueChange = { examTime = it },
                                    label = { Text("Pukul") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Nyalakan Pengingat", style = MaterialTheme.typography.bodySmall)
                                Switch(
                                    checked = reminderEnabled,
                                    onCheckedChange = { reminderEnabled = it }
                                )
                            }

                            Button(
                                onClick = {
                                    if (examTitle.isNotBlank()) {
                                        viewModel.saveExam(
                                            taskId = 0,
                                            title = examTitle,
                                            subject = examSubject,
                                            dueDate = examDate,
                                            dueTime = examTime,
                                            daysRemaining = daysRemainingInput,
                                            reminderEnabled = reminderEnabled
                                        )
                                        isAddingNewExam = false
                                        examTitle = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("btn_save_exam_reminder"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Simpan Jadwal Ujian")
                            }
                        }
                    }
                }

                // Daftar Jadwal Ujian Tersimpan
                Text(
                    text = "Daftar Ujian Tersimpan (${examTasks.size})",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(examTasks) { exam ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (exam.reminderEnabled) OrangeWarning.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "H-${exam.daysRemaining}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = OrangeWarning
                                            ),
                                            modifier = Modifier
                                                .background(OrangeWarning.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = exam.subject,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = exam.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${exam.dueDate} • ${exam.dueTime}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Toggle Reminder Switch per Exam
                                Switch(
                                    checked = exam.reminderEnabled,
                                    onCheckedChange = { isChecked ->
                                        viewModel.toggleTaskReminder(exam.id, isChecked)
                                    },
                                    modifier = Modifier.testTag("exam_reminder_toggle_${exam.id}")
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Selesai")
            }
        }
    )
}
