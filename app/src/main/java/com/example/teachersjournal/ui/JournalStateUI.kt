package com.example.teachersjournal.ui

import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.data.lessons.LessonData
import com.example.teachersjournal.data.students.StudentData

data class JournalStateUI (


    val groupList: MutableList<GroupData> = mutableListOf(),
    val currentGroup: GroupData? = null,
    val studentList: MutableList<StudentData> = mutableListOf(),
    val lessonList: MutableList<LessonData> = mutableListOf(),

    val addStudent: Boolean = false,
    val studentAlreadyExist: Boolean = false,
    val studentToAdd: StudentData? = null,
    val deleteStudent: Boolean = false,
    val studentToDelete: StudentData? = null,

    val addLesson: Boolean = false,
    val lessonAlreadyExist: Boolean = false,
    val lessonToAdd: LessonData? = null,
    val deleteLesson: Boolean = false,
    val lessonToDelete: LessonData? = null,

    val addGroup: Boolean = false,
    val groupAlreadyExist: Boolean = false,
    val groupToAdd: String? = null,
    val deleteGroup: Boolean = false,
    val groupToDelete: GroupData? = null,
)