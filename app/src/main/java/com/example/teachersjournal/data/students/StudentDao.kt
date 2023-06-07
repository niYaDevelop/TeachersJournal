package com.example.teachersjournal.data.students

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE student_group = :group ORDER BY surname ASC")
    suspend fun getAllStudentsInGroup(group: String): MutableList<StudentData>

    @Insert
    suspend fun addStudent(student: StudentData)

    @Query("DELETE FROM students WHERE id = :studentId")
    suspend fun removeStudent(studentId: Int)

    @Query("DELETE FROM students WHERE student_group = :group")
    suspend fun deleteGroup(group: String)
}