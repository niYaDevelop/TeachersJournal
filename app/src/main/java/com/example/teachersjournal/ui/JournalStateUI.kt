package com.example.teachersjournal.ui

import com.example.teachersjournal.data.StudentData

data class JournalStateUI (
    val studentToDelete: StudentData? = null,
    val studentList: MutableList<StudentData> = mutableListOf()
)