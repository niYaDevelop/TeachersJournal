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
    val group: String,
)
