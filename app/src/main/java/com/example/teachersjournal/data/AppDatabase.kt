package com.example.teachersjournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(StudentData::class, LessonData::class, VisitorData::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getStudentDao(): StudentDao
    abstract fun getLessonDao(): LessonDao
    abstract fun getVisitorDao(): VisitorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "journal")
                    .createFromAsset("database/journal.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}