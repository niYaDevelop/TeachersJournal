package com.example.teachersjournal.data.lessons

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LessonDao {

    @Query("DELETE FROM lessons WHERE 'group' = :group")
    suspend fun deleteGroup(group: String)
}