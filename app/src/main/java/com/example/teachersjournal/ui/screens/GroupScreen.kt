package com.example.teachersjournal.ui.screens


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teachersjournal.R
import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.data.students.StudentData
import com.example.teachersjournal.ui.JournalViewModel
import com.example.teachersjournal.ui.theme.TeachersJournalTheme
import java.util.Locale


@Composable
fun GroupScreen(journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)){

    val uiState by journalViewModel.uiState.collectAsState()


    Box() {
        Column() {
            Text(
                text = stringResource(id = R.string.groups).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .paddingFromBaseline(30.dp, 8.dp)
                    .padding(horizontal = 16.dp)
            )
            MainGroupDropdownMenu(
                groupList = uiState.groupList,
                selectedGroup = if(uiState.currentGroup != null) uiState.currentGroup!!.groupName else stringResource(id = R.string.add_group_text),
                onGroupChanged = { newGroup -> journalViewModel.changeGroup(newGroup) },
                onAddGroupClicked = { journalViewModel.openAddGroupDialog() },
                onGroupDelete = { groupToDelete -> journalViewModel.openDeleteGroupDialog(groupToDelete) }
            )
            Text(
                text = stringResource(id = R.string.students).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .paddingFromBaseline(30.dp, 8.dp)
                    .padding(horizontal = 16.dp)
            )
            StudentsList(
                uiState.studentList,
                showAddButton = uiState.currentGroup != null,
                onDeleteStudent = { student -> journalViewModel.openDeleteStudentDialog(student) },
                onAddStudent = { journalViewModel.openAddStudentDialog() }
            )
        }
        if(uiState.addStudent && uiState.currentGroup != null){
//            AddStudentDialog(
//                groupList = uiState.groupList,
//                currentGroup = uiState.currentGroup!!,
//                hideError = {},
//                onClose = { journalViewModel.closeAddStudentDialog() },
//                onAdded = ,
//                isContain =
//            )
        }
        if(uiState.deleteStudent){
            DeleteStudentDialog(
                student = uiState.studentToDelete!!,
                onClose = { journalViewModel.closeDeleteStudentDialog() },
                onDelete = { journalViewModel.deleteStudent() }
            )
        }
        if (uiState.deleteGroup){
            DeleteGroupDialog(
                group = uiState.groupToDelete!!,
                onClose = { journalViewModel.closeDeleteGroupDialog() },
                onDelete = { journalViewModel.deleteGroup() }
            )
        }
        if(uiState.addGroup){
            AddGroupDialog(
                onClose = { journalViewModel.closeAddGroupDialog() },
                onAdded = { group -> journalViewModel.addGroup(group) },
                isContain = uiState.groupAlreadyExist,
                hideError = { journalViewModel.hideAddGroupError() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGroupDropdownMenu(
    groupList: List<GroupData>,
    selectedGroup: String,
    onGroupChanged: (GroupData)-> Unit,
    onAddGroupClicked: ()-> Unit,
    onGroupDelete: (GroupData)-> Unit
){
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = selectedGroup,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .exposedDropdownSize(matchTextFieldWidth = true)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(top = 8.dp)

            ) {
                groupList.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text(text = item.groupName)
                                IconButton(onClick = {
                                    expanded = false
                                    onGroupDelete(item)
                                }) {
                                    Icon(Icons.Filled.Close, contentDescription = null)
                                }
                            }
                               },
                        onClick = {
                            expanded = false
                            onGroupChanged(item)
                        },
                    )
                }
                Button(
                    onClick = {
                        expanded = false
                        onAddGroupClicked()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp)
                ){
                    Icon(Icons.Filled.Add, null)
                }
            }
        }
    }
}

@Composable
fun StudentsList(
    list: List<StudentData>,
    showAddButton: Boolean,
    onDeleteStudent: (StudentData) -> Unit,
    onAddStudent: () -> Unit
){

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn() {
            items(list) { item ->
                StudentCard(item, { onDeleteStudent(item) })
            }
        }
        if(showAddButton) {
            ExtendedFloatingActionButton(
                onClick = onAddStudent,
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text(text = stringResource(id = R.string.add_student)) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentCard(
    student: StudentData,
    onDeleteStudent: () -> Unit
){
    var menuExpanded by remember { mutableStateOf(false) }

    Column() {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { },
                    onLongClick = { menuExpanded = true },
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column() {
                    Text(text = student.surname, style = MaterialTheme.typography.titleLarge)
                    Text(text = student.name)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = student.rating.toString(),
                        Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(text = stringResource(id = R.string.average_rating))
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(text = student.lessonsVisits.toString() + "/" + (student.lessonsVisits + student.lessonsIgnore).toString())
                    Text(text = stringResource(id = R.string.visits))
                }
            }
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            offset = DpOffset(x = 8.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                leadingIcon = { Icon(Icons.Filled.Delete, null) },
                text = {Text(stringResource(id = R.string.delete))},
                onClick = {
                    onDeleteStudent()
                    menuExpanded = false
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddGroupDialog(
    onClose: ()-> Unit,
    onAdded: (String)-> Unit,
    isContain: Boolean,
    hideError: ()-> Unit
){
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(
                onClick = { onAdded(text) }
            ) {
                Text(stringResource(id = R.string.add))
            }
        },
        dismissButton = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {Text("Добавить группу")},
        text  = {
            Column() {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        hideError()
                    },
                    label = { Text(stringResource(id = R.string.group_name)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                if (isContain) {
                    Text(
                        text = "Группа с таким названием уже существует",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentDialog(
    onClose: ()-> Unit,
    onAdded: (StudentData)-> Unit,
    isContain: Boolean,
    hideError: ()-> Unit,
    groupList: List<GroupData>,
    currentGroup: GroupData
){
    var nameText by remember { mutableStateOf("") }
    var surnameText by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf(currentGroup) }

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(
                onClick = { onAdded(StudentData(name = nameText, surname = surnameText, group = selectedGroup.groupName)) }
            ) {
                Text(stringResource(id = R.string.add))
            }
        },
        dismissButton = {},
        title = {Text("Добавить ученика")},
        text  = {
            Column() {
                OutlinedTextField(
                    value = surnameText,
                    onValueChange = {
                        surnameText = it
                        hideError
                    },
                    label = { Text("Фамилия") },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                OutlinedTextField(
                    value = nameText,
                    onValueChange = {
                        nameText = it
                        hideError
                    },
                    label = { Text("Имя Отчество") },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                GroupDropdownMenu(
                    groupList = groupList,
                    selectedGroup = selectedGroup,
                    onGroupChanged = {newGroup -> selectedGroup = newGroup}
                )
                if (isContain) {
                    Text("Этот ученик уже есть в этой группе", color = Color.Red)
                }
            }
        },
        shape = MaterialTheme.shapes.large,
    )
}


@Composable
fun DeleteStudentDialog(
    student: StudentData,
    onClose: ()-> Unit,
    onDelete: ()-> Unit,
){
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(
                onClick = onDelete
            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClose
            ) {
                Text(stringResource(id = R.string.no))
            }
        },
        title = {Text("Вы уверены?")},
        text  = {
                Text("Удалить ученика ${student.surname} ${student.name} из группы '${student.group}'?")
        },
        shape = MaterialTheme.shapes.large,
    )
}



@Composable
fun DeleteGroupDialog(
    group: GroupData,
    onClose: ()-> Unit,
    onDelete: ()-> Unit,
){
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(
                onClick = onDelete
            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClose
            ) {
                Text(stringResource(id = R.string.no))
            }
        },
        title = {Text("Вы уверены?")},
        text  = {
            Text("Удалить группу '${group.groupName}'?")
        },
        shape = MaterialTheme.shapes.large,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDropdownMenu(
    groupList: List<GroupData>,
    selectedGroup: GroupData,
    onGroupChanged: (GroupData)-> Unit,
){
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = selectedGroup.groupName,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                groupList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.groupName) },
                        onClick = {
                            expanded = false
                            onGroupChanged(item)
                        },
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeachersJournalTheme {
        GroupScreen()
    }
}