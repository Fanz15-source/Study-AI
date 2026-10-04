package com.example.ui.screens.quiz

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.QuizQuestion
import com.example.ui.StudyViewModel
import com.example.ui.theme.*

@Composable
fun QuizScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val quizList by viewModel.quizQuestions.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    val currentQuestion = quizList.getOrNull(currentIndex)

    if (isFinished) {
        // --- Score Recap Screen ---
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("quiz_recap_view"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MintAccent.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = GoldStar,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Kuis Latihan Selesai!",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Skor Akhir: $score dari ${quizList.size} Benar",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "+${score * 30 + 50} XP Diperoleh!",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MintAccent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    currentIndex = 0
                    selectedOptionIndex = null
                    isSubmitted = false
                    score = 0
                    isFinished = false
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ulangi Kuis", fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .testTag("quiz_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Header & Progress ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quiz & Practice",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (quizList.isNotEmpty()) {
                    Text(
                        text = "Soal ${currentIndex + 1} / ${quizList.size}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (quizList.isNotEmpty()) {
                LinearProgressIndicator(
                    progress = { ((currentIndex + 1).toFloat() / quizList.size.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        // --- Question & Options ---
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
                    .padding(vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Question Statement Card
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        tonalElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = currentQuestion.subject,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 24.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Multiple Choice Options
                items(options.size) { optIdx ->
                    val optText = options[optIdx]
                    val isSelected = selectedOptionIndex == optIdx
                    val isCorrect = optIdx == currentQuestion.correctIndex

                    val (containerColor, borderColor) = when {
                        !isSubmitted && isSelected -> Pair(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), MaterialTheme.colorScheme.primary)
                        isSubmitted && isCorrect -> Pair(MintAccent.copy(alpha = 0.2f), MintAccent)
                        isSubmitted && isSelected && !isCorrect -> Pair(CrimsonError.copy(alpha = 0.15f), CrimsonError)
                        else -> Pair(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(enabled = !isSubmitted) { selectedOptionIndex = optIdx }
                            .testTag("quiz_option_$optIdx"),
                        shape = RoundedCornerShape(12.dp),
                        color = containerColor,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Letter pill (A, B, C, D)
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = ('A'.code + optIdx).toChar().toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) DeepNavy else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )

                            // Status Icon (Check / Cross) so not relying only on color
                            if (isSubmitted) {
                                if (isCorrect) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Benar", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MintAccent))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Benar", tint = MintAccent)
                                    }
                                } else if (isSelected) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Salah", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = CrimsonError))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.Cancel, contentDescription = "Salah", tint = CrimsonError)
                                    }
                                }
                            }
                        }
                    }
                }

                // Explanation Box after submission
                if (isSubmitted) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Pembahasan:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Bottom Action Button ---
        Surface(modifier = Modifier.fillMaxWidth()) {
            if (!isSubmitted) {
                Button(
                    onClick = {
                        if (selectedOptionIndex != null) {
                            isSubmitted = true
                            if (selectedOptionIndex == currentQuestion?.correctIndex) {
                                score++
                            }
                        }
                    },
                    enabled = selectedOptionIndex != null,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_quiz_answer")
                ) {
                    Text("Periksa Jawaban", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = {
                        if (currentIndex < quizList.size - 1) {
                            currentIndex++
                            selectedOptionIndex = null
                            isSubmitted = false
                        } else {
                            isFinished = true
                            viewModel.triggerConfetti()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_next_quiz_question")
                ) {
                    Text(
                        text = if (currentIndex < quizList.size - 1) "Soal Berikutnya" else "Lihat Rekap Hasil",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
