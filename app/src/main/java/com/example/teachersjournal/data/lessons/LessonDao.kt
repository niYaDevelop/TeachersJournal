package com.example.teachersjournal.data.lessons

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LessonDao {

    @Query("DELETE FROM lessons WHERE lesson_group = :group")
    suspend fun deleteGroup(group: String)
    @Query("SELECT * FROM lessons WHERE lesson_group = :group ORDER BY dateInMillis ASC, hour ASC, minute ASC")
    suspend fun getAllLessonsInGroup(group: String): MutableList<LessonData>
    @Query("DELETE FROM lessons WHERE id = :lessonId")
    suspend fun removeLesson(lessonId: Int)
    @Insert
    suspend fun addLesson(lesson: LessonData)
}