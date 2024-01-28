package com.example.teachersjournal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teachersjournal.R
import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.ui.JournalViewModel

// Экран с текущим уроком (время, группа, тема, ДЗ, список учеников)
// (или следующий урок, или, если сегодня нет,
// то надпись "сегодня нет занятий")
@Composable
fun LessonScreen(journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)){

    val uiState by journalViewModel.uiState.collectAsState()

    Column() {
//        LessonHead(
//            onAddGroup = { journalViewModel.openAddGroupDialog() },
//            groupList = uiState.groupList,
//            selectedGroup = if(uiState.currentGroup != null) uiState.currentGroup!!.groupName else stringResource(id = R.string.add_group_text),
//            onGroupChanged = { newGroup -> journalViewModel.changeGroup(newGroup) },
//            onGroupDelete = { groupToDelete -> journalViewModel.openDeleteGroupDialog(groupToDelete) }
//        )
//        LessonStudentsList(
//            studentList = uiState.studentList,
//            onChangeVisitStatus = { status -> journalViewModel.changeVisitStatus(status) },
//            onRateStudent = { student, rate -> journalViewModel.rateStudent(student, rate) }
//        )
    }
}


@Composable
fun LessonHead(
    lessonTheme: String,
    lessonTask: String,
    group: String,
){
    // Тема урока
    // Задание на урок
    // Название группы
}