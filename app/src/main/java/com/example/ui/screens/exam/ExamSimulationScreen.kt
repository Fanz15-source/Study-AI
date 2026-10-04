package com.example.ui.screens.exam

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
fun ExamSimulationScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val quizQuestions by viewModel.quizQuestions.collectAsState()
    var isExamStarted by remember { mutableStateOf(false) }
    var currentExamIndex by remember { mutableStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    var isSubmitted by remember { mutableStateOf(false) }

    val currentQuestion = quizQuestions.getOrNull(currentExamIndex)

    if (!isExamStarted) {
        // --- Exam Introduction & Prediction Card ---
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
                .testTag("exam_intro_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Simulasi & Prediksi Ujian AI",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Uji kesiapan akademikmu dalam format ujian nyata dengan timer dan analisis kelemahan otomatis.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // AI Readiness Prediction Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Prediksi Kesiapan AI Terkini",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Peluang Nilai A/B", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("84.5%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = MintAccent))
                        }
                        Column {
                            Text("Topik Perlu Review", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Siklus Carnot", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = OrangeWarning))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { isExamStarted = true },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .testTag("btn_start_exam_sim")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mulai Simulasi Ujian", fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    if (isSubmitted) {
        // --- Exam Results Analysis ---
        val correctCount = userAnswers.count { (idx, ans) ->
            quizQuestions.getOrNull(idx)?.correctIndex == ans
        }
        val percent = if (quizQuestions.isNotEmpty()) (correctCount * 100) / quizQuestions.size else 0

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hasil Ujian & Analisis Skor",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (percent >= 70) MintAccent else OrangeWarning
                )
            )
            Text(
                text = "$correctCount benar dari ${quizQuestions.size} soal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isExamStarted = false
                    isSubmitted = false
                    userAnswers.clear()
                    currentExamIndex = 0
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kembali ke Menu Ujian")
            }
        }
        return
    }

    // --- Active Exam Screen ---
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header with question jumper
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Simulasi Ujian",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "Sisa Waktu: 28:40",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OrangeWarning
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question Numbers Palette
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(quizQuestions) { index, _ ->
                    val isAnswered = userAnswers.containsKey(index)
                    val isCurrent = index == currentExamIndex
                    Surface(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable { currentExamIndex = index },
                        color = if (isCurrent) MaterialTheme.colorScheme.primary
                        else if (isAnswered) MintAccent.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) DeepNavy else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Current Question Card
        if (currentQuestion != null) {
            val options = listOf(
                currentQuestion.optionA,
                currentQuestion.optionB,
                currentQuestion.optionC,
                currentQuestion.optionD
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Soal ${currentExamIndex + 1}:",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(options.size) { optIdx ->
                    val isSelected = userAnswers[currentExamIndex] == optIdx
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { userAnswers[currentExamIndex] = optIdx },
                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${('A'.code + optIdx).toChar()}.",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = options[optIdx], style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        // Bottom Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { if (currentExamIndex > 0) currentExamIndex-- },
                enabled = currentExamIndex > 0
            ) {
                Text("Sebelumnya")
            }

            if (currentExamIndex < quizQuestions.size - 1) {
                Button(onClick = { currentExamIndex++ }) {
                    Text("Berikutnya")
                }
            } else {
                Button(
                    onClick = {
                        isSubmitted = true
                        viewModel.triggerConfetti()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintAccent, contentColor = DeepNavy)
                ) {
                    Text("Kumpulkan Jawaban", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
