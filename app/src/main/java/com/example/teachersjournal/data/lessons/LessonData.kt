package com.example.teachersjournal.data.lessons

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val theme: String,
    @ColumnInfo(name = "home_work")
    val homeWork: String,
    @ColumnInfo(name = "lesson_group")
    val group: String,
    val dateInMillis: Long,
    val hour: Int,
    val minute: Int,
    val passed: Boolean = false
)
