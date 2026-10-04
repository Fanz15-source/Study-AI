package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        StudyMaterial::class,
        Flashcard::class,
        QuizQuestion::class,
        StudyNote::class,
        StudyTask::class,
        StudySession::class,
        UserProfile::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_study_hub_db"
                )
                .fallbackToDestructiveMigration(true)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        seedInitialData(context, scope)
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        seedInitialData(context, scope)
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private fun seedInitialData(context: Context, scope: CoroutineScope) {
            scope.launch(Dispatchers.IO) {
                val dao = getDatabase(context, scope).studyDao()
                dao.insertMaterials(DemoData.initialMaterials)
                dao.insertFlashcards(DemoData.initialFlashcards)
                dao.insertQuizQuestions(DemoData.initialQuizQuestions)
                dao.insertTasks(DemoData.initialTasks)
                DemoData.initialNotes.forEach { dao.insertNote(it) }
                dao.insertOrUpdateProfile(DemoData.initialProfile)
            }
        }
    }
}
