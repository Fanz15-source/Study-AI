package com.example.data.local

import com.example.data.model.*

object DemoData {
    val initialMaterials = listOf(
        StudyMaterial(
            id = 1,
            title = "Biologi Molekuler: Replikasi DNA & Sintesis Protein",
            subject = "Biologi",
            topic = "Genetika & DNA",
            fileType = "PDF",
            dateAdded = "2 hari yang lalu",
            lastStudied = "Hari ini",
            progress = 0.75f,
            content = """
# Biologi Molekuler: Replikasi DNA

Replikasi DNA merupakan proses biologis fundamental di mana molekul DNA heliks ganda digandakan untuk menghasilkan dua salinan yang identik. Proses ini bersifat **semi-konservatif**, artinya setiap untai DNA baru terdiri dari satu untai cetakan induk dan satu untai baru yang disintesis.

## 1. Enzim-Enzim Kunci
- **Helikase**: Membuka ikatan hidrogen antara basa nitrogen untuk memisahkan kedua untai DNA pada garpu replikasi (*replication fork*).
- **Single-Strand DNA-Binding Proteins (SSB)**: Menstabilkan untai tunggal DNA agar tidak berikatan kembali sebelum disalin.
- **Primase**: Mensintesis primer RNA pendek (~10-12 nukleotida) yang menyediakan gugus 3'-OH bebas untuk inisiasi DNA Polimerase.
- **DNA Polimerase III**: Enzim utama yang memperpanjang untai baru dengan menambahkan deoksiribonukleotida pada arah 5' ke 3'.
- **DNA Polimerase I**: Menghilangkan primer RNA dan menggantinya dengan nukleotida DNA.
- **DNA Ligase**: Menyambung celah (*nick*) pada rantai gula-fosfat di antara fragmen Okazaki pada untai lambat (*lagging strand*).

## 2. Leading Strand vs Lagging Strand
- **Leading Strand**: Disintesis secara kontinu ke arah membukanya garpu replikasi karena arah sintesis 5' ke 3' searah dengan pergerakan helikase.
- **Lagging Strand**: Disintesis secara diskontinu menjauhi garpu replikasi dalam bentuk potongan-potongan pendek yang disebut **Fragmen Okazaki**.

## 3. Transkripsi & Translasi
- **Transkripsi**: DNA dicetak menjadi mRNA oleh enzim RNA Polimerase di nukleus.
- **Translasi**: Ribosom membaca kodon pada mRNA untuk merangkai asam amino dengan bantuan tRNA di sitoplasma.
            """.trimIndent(),
            summary = "Replikasi DNA bersifat semi-konservatif. Enzim utama meliputi Helikase (pembuka untai), Primase (pembuat primer), DNA Polimerase (sintesis 5'->3'), dan Ligase (penyambung fragmen Okazaki).",
            keyConcepts = "Semi-konservatif, Helikase, Primase, DNA Polimerase, Fragmen Okazaki, Ligase, Transkripsi"
        ),
        StudyMaterial(
            id = 2,
            title = "Fisika: Hukum Termodinamika & Entropi",
            subject = "Fisika",
            topic = "Energi Termal & Mesin Kalor",
            fileType = "DOCX",
            dateAdded = "4 hari yang lalu",
            lastStudied = "Kemarin",
            progress = 0.50f,
            content = """
# Hukum-Hukum Termodinamika

Termodinamika mempelajari hubungan antara kalor, kerja, suhu, dan energi.

## Hukum Ke-Nol Termodinamika
Jika sistem A berada dalam kesetimbangan termal dengan sistem B, dan B berada dalam kesetimbangan termal dengan sistem C, maka A dan C berada dalam kesetimbangan termal satu sama lain. Ini mendasari konsep suhu dan termometer.

## Hukum Pertama Termodinamika (Kekekalan Energi)
Energi tidak dapat diciptakan atau dimusnahkan, hanya dapat diubah bentuknya.
Rumus dasar:
ΔU = Q - W
di mana:
- ΔU = Perubahan energi dalam (Joule)
- Q = Kalor yang diserap sistem (+ jika diserap, - jika dilepas)
- W = Usaha yang dilakukan sistem (+ jika melakukan usaha, - jika menerima usaha)

## Hukum Kedua Termodinamika (Entropi)
Kalor tidak dapat mengalir secara spontan dari benda dingin ke benda panas tanpa usaha dari luar. Dalam setiap proses alami terisolasi, entropi total (tingkat ketidakteraturan sistem) selalu meningkat:
ΔS ≥ 0

## Siklus Carnot
Siklus paling efisien yang beroperasi antara dua reservoir suhu (T_tinggi dan T_rendah).
Efisiensi Carnot:
η = 1 - (T_c / T_h) (dalam Kelvin)
            """.trimIndent(),
            summary = "Hukum Termodinamika mengatur kekekalan energi (Hukum 1: ΔU = Q - W) dan arah aliran spontanitas energi serta peningkatan entropi (Hukum 2). Efisiensi mesin kalor dibatasi oleh siklus Carnot.",
            keyConcepts = "Energi Dalam, Kalor, Usaha, Entropi, Kesetimbangan Termal, Siklus Carnot"
        ),
        StudyMaterial(
            id = 3,
            title = "Kalkulus: Teorema Dasar Integral & Luas Daerah",
            subject = "Matematika",
            topic = "Integral Kalkulus",
            fileType = "PDF",
            dateAdded = "1 minggu yang lalu",
            lastStudied = "3 hari yang lalu",
            progress = 0.40f,
            content = """
# Teorema Dasar Kalkulus (Fundamental Theorem of Calculus)

Teorema Dasar Kalkulus menghubungkan dua konsep sentral dalam kalkulus: diferensiasi (turunan) dan integrasi.

## Bagian 1
Jika f kontinu pada interval tertutup [a, b], dan g(x) = ∫[a ke x] f(t) dt, maka g kontinu pada [a, b], diferensiabel pada (a, b), dan:
g'(x) = f(x)

Artinya, integrasi dan diferensiasi adalah operasi yang saling berkebalikan (invers).

## Bagian 2 (Evaluasi Integral Tentu)
Jika f kontinu pada [a, b] dan F adalah sebarang antiturunan dari f (artinya F' = f), maka:
∫[a ke b] f(x) dx = F(b) - F(a)

## Teknik Integrasi Utama:
1. **Substitusi**: Menyerupai aturan rantai turunan kebalikannya.
2. **Integral Parsial**: Berdasarkan aturan perkalian diferensial: ∫ u dv = uv - ∫ v du.
3. **Pecahan Parsial**: Untuk mengintegrasikan fungsi rasional.
            """.trimIndent(),
            summary = "Teorema Dasar Kalkulus menghubungkan turunan dan integral. Evaluasi integral tentu menghitung perubahan total antiturunan: ∫[a,b] f(x)dx = F(b) - F(a).",
            keyConcepts = "Antiturunan, Integral Tentu, Aturan Substitusi, Integral Parsial, Luas Daerah"
        ),
        StudyMaterial(
            id = 4,
            title = "Sejarah Dunia Modern: Dampak Revolusi Industri",
            subject = "Sejarah",
            topic = "Modernisasi Abad 18-19",
            fileType = "NOTE",
            dateAdded = "5 hari yang lalu",
            lastStudied = "4 hari yang lalu",
            progress = 0.90f,
            content = """
# Revolusi Industri (1760 - 1840)

Revolusi Industri bermula di Britania Raya pada pertengahan abad ke-18 dan mengubah tatanan ekonomi dunia dari agraris-kerajinan menjadi berbasis manufaktur mekanis berskala massal.

## Faktor Pendorong di Inggris:
- Ketersediaan batu bara dan bijih besi melimpah.
- Revolusi Pertanian yang meningkatkan populasi dan tenaga kerja.
- Stabilitas politik dan perlindungan hak kekayaan intelektual (paten).
- Jaringan perdagangan kolonial yang luas.

## Inovasi Kunci:
- **Mesin Uap James Watt (1769)**: Menggantikan tenaga air/otot hewan, memungkinkan pabrik dibangun di mana saja.
- **Spinning Jenny & Power Loom**: Mengubah industri tekstil menjadi sangat cepat dan terjangkau.
- **Jalur Kereta Api (Locomotive)**: Mengakselerasi mobilitas logistik dan barang antarwilayah.

## Transformasi Sosial:
- Terjadinya urbanisasi masif ke kota-kota pabrik (Manchester, Birmingham).
- Munculnya kelas sosial baru: kaum borjuis industri (pemilik modal) dan kaum proletar (buruh pabrik).
- Perkembangan gagasan ideologi baru: Kapitalisme pasar bebas (Adam Smith) dan Sosialisme/Marxisme (Karl Marx).
            """.trimIndent(),
            summary = "Revolusi Industri mengubah peradaban dari agraris ke manufaktur mekanik melalui mesin uap dan mekanisasi tekstil, memicu urbanisasi dan pergeseran kelas sosial kapitalis-proletar.",
            keyConcepts = "Mesin Uap James Watt, Urbanisasi, Kapitalisme, Kaum Proletar, Industri Tekstil, Jalur Kereta Api"
        )
    )

    val initialFlashcards = listOf(
        Flashcard(
            id = 1,
            materialId = 1,
            subject = "Biologi",
            question = "Apa fungsi utama enzim DNA Helikase dalam replikasi DNA?",
            answer = "Membuka ikatan hidrogen antara pasangan basa nitrogen untuk memisahkan kedua untai ganda DNA pada garpu replikasi.",
            isMastered = true,
            reviewCount = 3
        ),
        Flashcard(
            id = 2,
            materialId = 1,
            subject = "Biologi",
            question = "Mengapa replikasi DNA disebut semi-konservatif?",
            answer = "Karena setiap molekul DNA baru yang dihasilkan terdiri dari satu untai lama (induk) dan satu untai yang baru disintesis.",
            isMastered = false,
            reviewCount = 2
        ),
        Flashcard(
            id = 3,
            materialId = 1,
            subject = "Biologi",
            question = "Apa itu Fragmen Okazaki dan di mana ia terbentuk?",
            answer = "Potongan-potongan pendek DNA yang disintesis secara diskontinu pada rantai lambat (lagging strand) dengan arah 5' ke 3'.",
            isMastered = false,
            reviewCount = 1
        ),
        Flashcard(
            id = 4,
            materialId = 2,
            subject = "Fisika",
            question = "Tuliskan persamaan matematis Hukum Pertama Termodinamika dan artinya!",
            answer = "ΔU = Q - W. Perubahan energi dalam sama dengan kalor yang diserap dikurangi usaha yang dilakukan oleh sistem.",
            isMastered = true,
            reviewCount = 4
        ),
        Flashcard(
            id = 5,
            materialId = 2,
            subject = "Fisika",
            question = "Apa makna fisis dari konsep Entropi dalam Hukum Kedua Termodinamika?",
            answer = "Entropi mengukur derajat ketidakteraturan atau dispersi energi suatu sistem; dalam proses spontan alami terisolasi, entropi selalu meningkat.",
            isMastered = false,
            reviewCount = 2
        ),
        Flashcard(
            id = 6,
            materialId = 3,
            subject = "Matematika",
            question = "Bagaimana rumus integral parsial dinyatakan?",
            answer = "∫ u dv = uv - ∫ v du (berasal dari aturan perkalian turunan d(uv) = u dv + v du).",
            isMastered = true,
            reviewCount = 3
        )
    )

    val initialQuizQuestions = listOf(
        QuizQuestion(
            id = 1,
            materialId = 1,
            subject = "Biologi",
            question = "Enzim manakah yang berperan menyambungkan celah antar fragmen Okazaki pada rantai lagging?",
            optionA = "DNA Polimerase I",
            optionB = "DNA Ligase",
            optionC = "RNA Primase",
            optionD = "Helikase",
            correctIndex = 1,
            explanation = "DNA Ligase mengkatalisis pembentukan ikatan fosfodiester untuk menyambungkan celah rantai gula-fosfat di antara fragmen Okazaki."
        ),
        QuizQuestion(
            id = 2,
            materialId = 1,
            subject = "Biologi",
            question = "Arah sintesis pemanjangan untai baru oleh DNA Polimerase selalu bergerak ke arah...",
            optionA = "3' ke 5'",
            optionB = "5' ke 3'",
            optionC = "Bebas dari kedua arah",
            optionD = "Dari tengah ke kedua ujung",
            correctIndex = 1,
            explanation = "DNA Polimerase hanya dapat menambahkan nukleotida baru pada ujung 3'-OH bebas, sehingga arah pemanjangannya selalu 5' ke 3'."
        ),
        QuizQuestion(
            id = 3,
            materialId = 2,
            subject = "Fisika",
            question = "Pada proses isokhorik (volume konstan), besarnya usaha W yang dilakukan oleh gas ideal adalah...",
            optionA = "Maksimum",
            optionB = "Nol (W = 0)",
            optionC = "W = P · ΔV",
            optionD = "W = -Q",
            correctIndex = 1,
            explanation = "Karena volume konstan (ΔV = 0), maka kerja W = ∫ P dV = 0. Seluruh kalor yang diserap digunakan untuk menaikkan energi dalam (ΔU = Q)."
        ),
        QuizQuestion(
            id = 4,
            materialId = 2,
            subject = "Fisika",
            question = "Sebuah mesin kalor Carnot bekerja antara suhu reservoir 600 K dan 300 K. Efisiensi teoritis maksimumnya adalah...",
            optionA = "25%",
            optionB = "33%",
            optionC = "50%",
            optionD = "75%",
            correctIndex = 2,
            explanation = "Efisiensi Carnot η = 1 - (Tc / Th) = 1 - (300 / 600) = 1 - 0.5 = 0.5 atau 50%."
        ),
        QuizQuestion(
            id = 5,
            materialId = 4,
            subject = "Sejarah",
            question = "Inovasi teknologi apa yang menjadi penggerak utama revolusi mekanisasi pada abad ke-18?",
            optionA = "Mesin Cetak Gutenberg",
            optionB = "Mesin Uap James Watt",
            optionC = "Telegraf Listrik",
            optionD = "Dinamit",
            correctIndex = 1,
            explanation = "Mesin uap yang disempurnakan James Watt pada 1769 memungkinkan pabrik beroperasi tanpa bergantung pada aliran air atau tenaga hewan."
        )
    )

    val initialTasks = listOf(
        StudyTask(
            id = 1,
            title = "Ujian Tengah Semester: Biologi Sel & Genetika",
            subject = "Biologi",
            dueDate = "06 Oktober 2026",
            dueTime = "08:00",
            isExam = true,
            isCompleted = false,
            reminderEnabled = true,
            daysRemaining = 2
        ),
        StudyTask(
            id = 2,
            title = "Ujian Akhir Semester: Fisika Termodinamika",
            subject = "Fisika",
            dueDate = "10 Oktober 2026",
            dueTime = "10:30",
            isExam = true,
            isCompleted = false,
            reminderEnabled = true,
            daysRemaining = 6
        ),
        StudyTask(
            id = 3,
            title = "Latihan Soal Siklus Carnot & Entropi",
            subject = "Fisika",
            dueDate = "Hari ini, 20:00",
            dueTime = "20:00",
            isExam = false,
            isCompleted = false,
            reminderEnabled = true,
            daysRemaining = 0
        ),
        StudyTask(
            id = 4,
            title = "Review Flashcard Kalkulus Integral Parsial",
            subject = "Matematika",
            dueDate = "Hari ini, 21:30",
            dueTime = "21:30",
            isExam = false,
            isCompleted = true,
            reminderEnabled = false,
            daysRemaining = 0
        ),
        StudyTask(
            id = 5,
            title = "Kuis Prediksi Sejarah Dunia Modern",
            subject = "Sejarah",
            dueDate = "14 Oktober 2026",
            dueTime = "14:00",
            isExam = true,
            isCompleted = false,
            reminderEnabled = true,
            daysRemaining = 10
        )
    )

    val initialCheatSheets = listOf(
        CheatSheetItem(
            id = 1,
            subject = "Fisika",
            topic = "Termodinamika",
            title = "Hukum I Termodinamika",
            formulaOrRule = "ΔU = Q - W",
            explanation = "Q (+) jika sistem menyerap kalor; W (+) jika sistem memuai (melakukan usaha ke luar)."
        ),
        CheatSheetItem(
            id = 2,
            subject = "Fisika",
            topic = "Termodinamika",
            title = "Efisiensi Mesin Carnot",
            formulaOrRule = "η = (1 - Tc/Th) × 100%",
            explanation = "Tc = reservoir dingin (Kelvin), Th = reservoir panas (Kelvin). Efisiensi maksimum teoritis."
        ),
        CheatSheetItem(
            id = 3,
            subject = "Matematika",
            topic = "Kalkulus",
            title = "Rumus Integral Parsial",
            formulaOrRule = "∫ u dv = u·v - ∫ v du",
            explanation = "Pilih u berdasarkan aturan LIATE (Logaritma, Invers trigonometri, Aljabar, Trigonometri, Eksponensial)."
        ),
        CheatSheetItem(
            id = 4,
            subject = "Biologi",
            topic = "Genetika",
            title = "Aturan Pasangan Basa Chargaff",
            formulaOrRule = "A = T (2 ikatan H), G ≡ C (3 ikatan H)",
            explanation = "Adenin selalu berpasangan dengan Timin; Guanin dengan Sitosin pada molekul DNA heliks ganda."
        ),
        CheatSheetItem(
            id = 5,
            subject = "Informatika",
            topic = "Algoritma",
            title = "Kompleksitas Pencarian Biner",
            formulaOrRule = "T(n) = O(log n)",
            explanation = "Membelah ruang pencarian menjadi separuh setiap iterasi pada array terurut."
        )
    )

    val initialSpacedRepetition = listOf(
        SpacedRepetitionItem(
            id = 1,
            topic = "Enzim Replikasi DNA (Helikase & Ligase)",
            subject = "Biologi",
            stage = "H+1 Review",
            memoryRetention = 85,
            isDueToday = true
        ),
        SpacedRepetitionItem(
            id = 2,
            topic = "Persamaan Gas Ideal & Usaha Isokhorik",
            subject = "Fisika",
            stage = "H+3 Review",
            memoryRetention = 68,
            isDueToday = true
        ),
        SpacedRepetitionItem(
            id = 3,
            topic = "Aturan Rantai & Substitusi Trigonometri",
            subject = "Matematika",
            stage = "H+7 Review",
            memoryRetention = 92,
            isDueToday = false
        )
    )

    val initialNotes = listOf(
        StudyNote(
            id = 1,
            materialId = 1,
            title = "Ringkasan Replikasi vs Transkripsi",
            content = "Replikasi: menggandakan seluruh genom DNA menggunakan DNA polimerase, terjadi saat fase S interfase sel.\nTranskripsi: menyalin gen spesifik menjadi molekul mRNA menggunakan RNA polimerase.",
            tags = "DNA, Gen, Perbedaan"
        ),
        StudyNote(
            id = 2,
            materialId = 2,
            title = "Trik Cepat Menghafal Tanda Termodinamika",
            content = "Kalor masuk = Q positif (+) | Kalor keluar = Q negatif (-)\nGas memuai (melakukan usaha ke lingkungan) = W positif (+)\nGas dimampatkan (menerima kerja dari lingkungan) = W negatif (-)",
            tags = "Fisika, Rumus, Hafalan"
        )
    )

    val initialProfile = UserProfile(
        id = 1,
        name = "Irfan",
        educationLevel = "Universitas",
        targetSubjects = "Biologi, Fisika, Matematika, Sejarah",
        streakDays = 5,
        totalXp = 1250,
        level = 4,
        dailyGoalMinutes = 45,
        todayStudiedMinutes = 35,
        selectedTheme = "DARK_FOCUS",
        selectedAmbience = "CALM_GRADIENT",
        ambientAnimationsEnabled = true,
        soundVolume = 0.5f,
        unlockedAchievements = "FIRST_MATERIAL,FOCUS_STARTER,FIRST_QUIZ"
    )

    val allAchievements = listOf(
        AchievementItem(
            id = "FIRST_MATERIAL",
            title = "First Material",
            description = "Buka atau unggah materi pelajaran pertamamu.",
            iconName = "library_books",
            xpReward = 100,
            isUnlocked = true
        ),
        AchievementItem(
            id = "FOCUS_STARTER",
            title = "Focus Starter",
            description = "Selesaikan sesi timer belajar pertamamu di Focus Room.",
            iconName = "timer",
            xpReward = 150,
            isUnlocked = true
        ),
        AchievementItem(
            id = "FIRST_QUIZ",
            title = "First Quiz",
            description = "Selesaikan kuis latihan dan uji pemahamanmu.",
            iconName = "quiz",
            xpReward = 200,
            isUnlocked = true
        ),
        AchievementItem(
            id = "FLASHCARD_MASTER",
            title = "Flashcard Master",
            description = "Kuasai setidaknya 5 flashcard sampai status hafal.",
            iconName = "style",
            xpReward = 250,
            isUnlocked = false
        ),
        AchievementItem(
            id = "CONSISTENT_LEARNER",
            title = "Consistent Learner",
            description = "Capai 5 hari beruntun belajar (5-Day Streak).",
            iconName = "local_fire_department",
            xpReward = 300,
            isUnlocked = true
        ),
        AchievementItem(
            id = "EXAM_WARRIOR",
            title = "Exam Warrior",
            description = "Selesaikan satu sesi Simulasi Ujian lengkap.",
            iconName = "military_tech",
            xpReward = 350,
            isUnlocked = false
        )
    )

    val musicTracks = listOf(
        MusicTrack(
            id = "rain_1",
            title = "Gentle Rainfall Ambience",
            artist = "Study Hub Synth Engine",
            category = "Rain Sounds",
            platform = "BUILT_IN",
            synthMode = "RAIN"
        ),
        MusicTrack(
            id = "focus_noise_1",
            title = "Deep Focus Pink Noise",
            artist = "Acoustic Flow",
            category = "Deep Focus",
            platform = "BUILT_IN",
            synthMode = "PINK_NOISE"
        ),
        MusicTrack(
            id = "binaural_1",
            title = "Alpha Wave Study Frequency (432Hz)",
            artist = "Binaural Mind",
            category = "Ambient Space",
            platform = "BUILT_IN",
            synthMode = "DEEP_BINAURAL"
        ),
        MusicTrack(
            id = "spotify_lofi",
            title = "Lofi Beats to Study/Relax To",
            artist = "Lofi Girl on Spotify",
            category = "Lo-fi Study",
            platform = "SPOTIFY",
            externalUrl = "https://open.spotify.com/playlist/0vvXsWCC9xrXsKd4FyS8kM"
        ),
        MusicTrack(
            id = "spotify_piano",
            title = "Peaceful Piano Study",
            artist = "Spotify Curated",
            category = "Piano Instrumental",
            platform = "SPOTIFY",
            externalUrl = "https://open.spotify.com/playlist/37i9dQZF1DX4sWSpwq3LiO"
        ),
        MusicTrack(
            id = "ytm_lofi",
            title = "Lofi Study Session",
            artist = "YouTube Music",
            category = "Lo-fi Study",
            platform = "YOUTUBE",
            externalUrl = "https://music.youtube.com/playlist?list=RDCLAK5uy_kbcYq1n0r4Z6hN2WbU_vA2F4r1MvUq1f8"
        ),
        MusicTrack(
            id = "ytm_classical",
            title = "Classical for Deep Work",
            artist = "YouTube Music",
            category = "Classical Focus",
            platform = "YOUTUBE",
            externalUrl = "https://music.youtube.com/playlist?list=RDCLAK5uy_k1C_4e4tD2n9WvU"
        )
    )
}
