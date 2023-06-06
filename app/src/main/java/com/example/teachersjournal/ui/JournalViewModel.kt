package com.example.teachersjournal.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.teachersjournal.JournalApplication
import com.example.teachersjournal.data.AppDatabase
import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.data.students.StudentData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JournalViewModel(
    private val journalDatabase: AppDatabase
): ViewModel() {

    private val _uiState = MutableStateFlow(JournalStateUI())
    val uiState: StateFlow<JournalStateUI> = _uiState.asStateFlow()


    init{
        readGroupList()
    }


    private fun readGroupList(){
        viewModelScope.launch {
            try {
                val list = journalDatabase.getGroupDao().getAllGroups()
                _uiState.update { currentState ->
                    currentState.copy(
                        groupList = list,
                        currentGroup = if(list.isNotEmpty()) list[0] else null
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }



    private fun setCurrentGroup(group: GroupData?){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentGroup = group,
                        studentList = if(group!= null) journalDatabase.getStudentDao().getAllStudentsInGroup(group.groupName) else mutableListOf()
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun addGroup(group: String){
        viewModelScope.launch {
            try {
                val groups = _uiState.value.groupList
                val isContain = groups.any { it.groupName == group }
                if(!isContain){
                    val newGroup = GroupData(groupName = group)
                    journalDatabase.getGroupDao().addGroup(newGroup)
                    readGroupList()
                    setCurrentGroup(newGroup)
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        addGroup = isContain,
                        groupAlreadyExist = isContain,
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun hideAddGroupError(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        groupAlreadyExist = false
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }



    fun openAddStudentDialog(){

    }

    fun addStudent(student: StudentData){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        studentList = currentState.studentList.plus(student).toMutableList(),
                        addStudent = false
                    )
                }
                journalDatabase.getStudentDao().addStudent(student)
            }catch (e: Exception){ println(e) }
        }
    }

    fun deleteStudent(){
        viewModelScope.launch {
            try {
                val student = _uiState.value.studentToDelete!!
                _uiState.update { currentState ->
                    currentState.copy(
                        deleteStudent = false,
                        studentList = currentState.studentList.minus(student).toMutableList()
                    )
                }
                journalDatabase.getStudentDao().removeStudent(student.id)
                journalDatabase.getVisitorDao().removeStudent(student.id)
            }catch (e: Exception){ println(e) }
        }
    }

    fun deleteGroup(){
        viewModelScope.launch {
            try {
                val group = _uiState.value.groupToDelete!!
                journalDatabase.getGroupDao().deleteGroup(group.groupName)
                journalDatabase.getStudentDao().deleteGroup(group.groupName)
                journalDatabase.getVisitorDao().deleteGroup(group.groupName)
                journalDatabase.getLessonDao().deleteGroup(group.groupName)

                val newGroupList = _uiState.value.groupList.minus(group).toMutableList()
                val newCurrentGroup = if(newGroupList.isNotEmpty()) newGroupList[0] else null

                _uiState.update { currentState ->
                    currentState.copy(
                        deleteGroup = false,
                        groupList = newGroupList,
                        groupToDelete = null
                    )
                }
                setCurrentGroup(newCurrentGroup)
            }catch (e: Exception){ println(e) }
        }
    }

    fun changeGroup(group: GroupData){
        setCurrentGroup(group)
    }

    fun openAddGroupDialog(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        addGroup = true,
                        groupAlreadyExist = false
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun closeAddGroupDialog(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        addGroup = false,
                        groupAlreadyExist = false
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun openDeleteGroupDialog(group: GroupData){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        deleteGroup = true,
                        groupToDelete = group
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun closeDeleteGroupDialog(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        deleteGroup = false,
                        groupToDelete = null
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }


    fun openDeleteStudentDialog(student: StudentData){
        _uiState.update { currentState ->
            currentState.copy(
                deleteStudent = true,
                studentToDelete = student
            )
        }
    }

    fun closeDeleteStudentDialog(){
        _uiState.update { currentState ->
            currentState.copy(
                deleteStudent = false,
                studentToDelete = null
            )
        }
    }




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JournalApplication)
                val journalDatabase = application.database
                JournalViewModel( journalDatabase = journalDatabase)
            }
        }
    }
}