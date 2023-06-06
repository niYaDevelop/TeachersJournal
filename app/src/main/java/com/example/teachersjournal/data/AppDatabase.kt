package com.example.teachersjournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.teachersjournal.data.groups.GroupDao
import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.data.lessons.LessonDao
import com.example.teachersjournal.data.lessons.LessonData
import com.example.teachersjournal.data.students.StudentDao
import com.example.teachersjournal.data.students.StudentData
import com.example.teachersjournal.data.visitors.VisitorDao
import com.example.teachersjournal.data.visitors.VisitorData

@Database(entities = arrayOf(StudentData::class, LessonData::class, VisitorData::class, GroupData::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getStudentDao(): StudentDao
    abstract fun getLessonDao(): LessonDao
    abstract fun getVisitorDao(): VisitorDao
    abstract fun getGroupDao(): GroupDao

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