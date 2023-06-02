package com.example.teachersjournal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
data class VisitorData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val group: String,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Int,
    @ColumnInfo(name = "student_id")
    val studentId: Int,
    @ColumnInfo(name = "is_visited")
    val isVisited: Boolean,
)