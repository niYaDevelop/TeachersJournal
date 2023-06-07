package com.example.teachersjournal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.teachersjournal.JournalApplication
import com.example.teachersjournal.data.AppDatabase
import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.data.students.StudentData
import kotlinx.coroutines.CoroutineScope
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
        viewModelScope.launch {
            val job = readGroupList(viewModelScope)
            job.join()
            setCurrentGroup(if(_uiState.value.groupList.isNotEmpty()) _uiState.value.groupList[0] else null)
        }
    }


    private suspend fun readGroupList(scope: CoroutineScope) = scope.launch {
        try {
            _uiState.update { currentState ->
                currentState.copy(
                    groupList = journalDatabase.getGroupDao().getAllGroups()
                )
            }
        }catch (e: Exception){ println(e) }
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
                if(_uiState.value.groupList.any { it.groupName == group }){
                    _uiState.update { currentState ->
                        currentState.copy(
                            groupAlreadyExist = true,
                        )
                    }
                } else{
                    val newGroup = GroupData(groupName = group)
                    journalDatabase.getGroupDao().addGroup(newGroup)
                    _uiState.update { currentState ->
                        currentState.copy(
                            groupList = currentState.groupList.plus(newGroup).toMutableList(),
                            addGroup = false,
                            groupAlreadyExist = false
                        )
                    }
//                    val job = readGroupList(viewModelScope)
//                    job.join()
                    setCurrentGroup(newGroup)
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

    fun hideAddStudentError(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        studentAlreadyExist = false
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }



    fun openAddStudentDialog(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        addStudent = true,
                        studentAlreadyExist = false
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun closeAddStudentDialog(){
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        addStudent = false,
                        studentAlreadyExist = false
                    )
                }
            }catch (e: Exception){ println(e) }
        }
    }

    fun addStudent(student: StudentData){
        viewModelScope.launch {
            try {
                val students = _uiState.value.studentList
                val isContain = students.any { (it.name == student.name) && (it.surname == student.surname) }
                if(isContain){
                    _uiState.update { currentState ->
                        currentState.copy(
                            studentAlreadyExist = true,
                        )
                    }
                }else {
                    journalDatabase.getStudentDao().addStudent(student)
                    _uiState.update { currentState ->
                        currentState.copy(
                            addStudent = false,
                            studentAlreadyExist = false,
                            studentList = currentState.studentList.plus(student).toMutableList()
                        )
                    }
                }
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