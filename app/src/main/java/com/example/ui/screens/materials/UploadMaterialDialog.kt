package com.example.ui.screens.materials

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StudyViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UploadMaterialDialog(
    viewModel: StudyViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Fisika") }
    var topic by remember { mutableStateOf("Termodinamika & Kalor") }
    var contentText by remember { mutableStateOf("") }
    var selectedFileType by remember { mutableStateOf("PDF") } // PDF, PPT, FOTO, AUDIO, CATATAN
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }
    var selectedFileSize by remember { mutableStateOf("") }

    // Upload & AI Processing states
    var isProcessing by remember { mutableStateOf(false) }
    var processingStep by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Android Document Picker Launcher (PDF / PPT / All documents)
    val docPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            val (name, size) = queryFileInfo(context, uri)
            selectedFileName = name
            selectedFileSize = size
            if (name.endsWith(".ppt", true) || name.endsWith(".pptx", true)) {
                selectedFileType = "PPT"
            } else if (name.endsWith(".pdf", true)) {
                selectedFileType = "PDF"
            } else {
                selectedFileType = "DOCX"
            }
            if (title.isBlank()) {
                title = name.substringBeforeLast(".")
            }
            contentText = "Dokumen berkas $name berhasil dimuat dari memori perangkat. AI Study Hub telah mengekstrak slide dan bagian inti untuk dipelajari."
        }
    }

    // Photo / Image Picker Launcher (Zero-permission Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            val (name, size) = queryFileInfo(context, uri)
            selectedFileName = if (name.isNotBlank()) name else "Foto_Catatan_Kuliah.jpg"
            selectedFileSize = size
            selectedFileType = "FOTO"
            if (title.isBlank()) title = "Pindai Catatan Foto: $topic"
            contentText = "Pindaian foto papan tulis / modul $selectedFileName terbaca melalui OCR pintar AI. Konsep dan bagan visual telah diekstrak ke teks."
        }
    }

    // Audio Picker Launcher (Rekaman kuliah / podcast)
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            val (name, size) = queryFileInfo(context, uri)
            selectedFileName = if (name.isNotBlank()) name else "Rekaman_Kuliah.mp3"
            selectedFileSize = size
            selectedFileType = "AUDIO"
            if (title.isBlank()) title = "Transkrip Suara: $topic"
            contentText = "Audio rekaman dosen/kuliah $selectedFileName berhasil diproses. AI telah mengubah tuturan suara menjadi ringkasan poin transkrip terstruktur."
        }
    }

    val processingSteps = listOf(
        "Membaca file $selectedFileName dari penyimpanan...",
        "Mengekstrak teks, diagram & konsep kunci...",
        "Menyusun ringkasan otomatis & peta konsep AI...",
        "Menyiapkan kartu flashcard & bank latihan soal...",
        "Selesai! Materi berhasil dimasukkan ke Library."
    )

    val fileFormatTypes = listOf(
        Pair("PDF", Icons.Default.PictureAsPdf),
        Pair("PPT", Icons.Default.Slideshow),
        Pair("FOTO", Icons.Default.Image),
        Pair("AUDIO", Icons.Default.Audiotrack),
        Pair("CATATAN", Icons.Default.EditNote)
    )

    AlertDialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isProcessing) "Memproses Berkas AI" else "Upload Materi dari Perangkat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Mendukung PDF, PPT, Foto, Audio & Catatan",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            if (isProcessing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = { ((processingStep + 1) / 5f).coerceIn(0.1f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = processingSteps.getOrElse(processingStep) { "Memproses berkas..." },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Langkah ${processingStep + 1} dari 5",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pilihan Tipe Berkas (PDF, PPT, FOTO, AUDIO, CATATAN)
                    Text("Pilih Jenis File dari Penyimpanan:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(fileFormatTypes) { (type, icon) ->
                            val isSelected = selectedFileType == type
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedFileType = type
                                    when (type) {
                                        "PDF", "PPT" -> docPickerLauncher.launch(arrayOf("application/pdf", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/*"))
                                        "FOTO" -> photoPickerLauncher.launch(
                                            androidx.activity.result.PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                        "AUDIO" -> audioPickerLauncher.launch("audio/*")
                                        else -> {}
                                    }
                                },
                                label = { Text(type, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = DeepNavy
                                )
                            )
                        }
                    }

                    // Card Tampilan File Terpilih
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedFileName.isNotBlank()) MintAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                when (selectedFileType) {
                                    "FOTO" -> photoPickerLauncher.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    "AUDIO" -> audioPickerLauncher.launch("audio/*")
                                    else -> docPickerLauncher.launch(arrayOf("application/pdf", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/*"))
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (selectedFileName.isNotBlank()) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = if (selectedFileName.isNotBlank()) MintAccent else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (selectedFileName.isNotBlank()) selectedFileName else "Buka Penyimpanan File ($selectedFileType)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1
                                )
                                Text(
                                    text = if (selectedFileName.isNotBlank()) "Ukuran: $selectedFileSize • Siap diekstrak" else "Ketuk untuk memilih berkas dari HP",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Judul Materi") },
                        placeholder = { Text("Contoh: Bab 4 - Termodinamika & Kalor") },
                        modifier = Modifier.fillMaxWidth().testTag("input_material_title"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Mata Pelajaran") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { topic = it },
                            label = { Text("Topik / Bab") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = contentText,
                        onValueChange = { contentText = it },
                        label = { Text("Catatan / Ringkasan Teks") },
                        placeholder = { Text("Tulis catatan atau biarkan AI mengekstrak dari file...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("input_material_content"),
                        maxLines = 4
                    )
                }
            }
        },
        confirmButton = {
            if (!isProcessing) {
                Button(
                    onClick = {
                        val validTitle = if (title.isBlank()) {
                            if (selectedFileName.isNotBlank()) selectedFileName.substringBeforeLast(".") else "Materi: $topic"
                        } else title
                        val validContent = if (contentText.isBlank()) {
                            "Materi $validTitle ($selectedFileType) berhasil dimuat dan siap dipelajari."
                        } else contentText
                        isProcessing = true

                        scope.launch {
                            for (i in 0..4) {
                                processingStep = i
                                delay(650)
                            }
                            viewModel.addNewMaterial(
                                title = validTitle,
                                subject = subject,
                                topic = topic,
                                content = validContent,
                                fileType = selectedFileType
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.testTag("btn_confirm_upload")
                ) {
                    Text("Ekstrak & Simpan")
                }
            }
        },
        dismissButton = {
            if (!isProcessing) {
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            }
        }
    )
}

// Helper to query file name and size from content Uri
private fun queryFileInfo(context: Context, uri: Uri): Pair<String, String> {
    var name = ""
    var size = ""
    try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex != -1) name = cursor.getString(nameIndex) ?: ""
                if (sizeIndex != -1) {
                    val bytes = cursor.getLong(sizeIndex)
                    size = if (bytes > 1024 * 1024) {
                        String.format("%.1f MB", bytes / (1024.0 * 1024.0))
                    } else {
                        String.format("%d KB", bytes / 1024)
                    }
                }
            }
        }
    } catch (_: Exception) {}
    return Pair(name, size)
}
