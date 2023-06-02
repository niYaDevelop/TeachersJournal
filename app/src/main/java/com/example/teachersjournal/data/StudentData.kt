package com.example.teachersjournal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val group: String,
    val name: String,
    val surname: String,
    val rating: Float = 0f,
    @ColumnInfo(name = "rate_count")
    val rateCount: Int = 0,
    @ColumnInfo(name = "lessons_visits")
    val lessonsVisits: Int = 0,
    @ColumnInfo(name = "lessons_ignore")
    val lessonsIgnore: Int = 0,
)
