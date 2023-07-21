package com.example.teachersjournal.data.visitors

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VisitorDao {

    @Query("DELETE FROM visitors WHERE id = :studentId")
    suspend fun removeStudent(studentId: Int)

    @Query("DELETE FROM visitors WHERE 'student_group' = :group")
    suspend fun deleteGroup(group: String)
}