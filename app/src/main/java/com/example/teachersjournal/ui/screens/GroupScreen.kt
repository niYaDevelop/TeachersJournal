package com.example.teachersjournal.ui.screens


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teachersjournal.R
import com.example.teachersjournal.data.groups.GroupData
import com.example.teachersjournal.data.lessons.LessonData
import com.example.teachersjournal.data.students.StudentData
import com.example.teachersjournal.ui.JournalViewModel
import com.example.teachersjournal.ui.theme.TeachersJournalTheme
import java.sql.Time
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun GroupScreen(journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)){

    val uiState by journalViewModel.uiState.collectAsState()


    Box() {
        Column() {
            GroupSegment(
                onAddGroup = { journalViewModel.openAddGroupDialog() },
                groupList = uiState.groupList,
                selectedGroup = if(uiState.currentGroup != null) uiState.currentGroup!!.groupName else stringResource(id = R.string.add_group_text),
                onGroupChanged = { newGroup -> journalViewModel.changeGroup(newGroup) },
                onGroupDelete = { groupToDelete -> journalViewModel.openDeleteGroupDialog(groupToDelete) }
            )
            LessonSegment(
                lessonList = uiState.lessonList,
                onAddLesson = { journalViewModel.openAddLessonDialog() },
                onDeleteLesson = { lesson -> journalViewModel.openDeleteLessonDialog(lesson) },
                showAddButton = uiState.currentGroup != null,
            )
            StudentSegment(
                studentList = uiState.studentList,
                onAddStudent = { journalViewModel.openAddStudentDialog() },
                onDeleteStudent = { student -> journalViewModel.openDeleteStudentDialog(student) },
                showAddButton = uiState.currentGroup != null,
            )
        }
        if(uiState.addStudent && uiState.currentGroup != null){
            AddStudentDialog(
                currentGroup = uiState.currentGroup!!,
                hideError = { journalViewModel.hideAddStudentError() },
                onClose = { journalViewModel.closeAddStudentDialog() },
                onAdded = { student -> journalViewModel.addStudent(student) },
                isContain = uiState.studentAlreadyExist
            )
        }
        if(uiState.deleteStudent){
            DeleteStudentDialog(
                student = uiState.studentToDelete!!,
                onClose = { journalViewModel.closeDeleteStudentDialog() },
                onDelete = { journalViewModel.deleteStudent() }
            )
        }
        if(uiState.addLesson && uiState.currentGroup != null){
            AddLessonDialog(
                onClose = { journalViewModel.closeAddLessonDialog() },
                onAdded = { lesson -> journalViewModel.addLesson(lesson) },
                currentGroup = uiState.currentGroup!!,
                lessonAlreadyExist = uiState.lessonAlreadyExist
            )
        }
        if(uiState.deleteLesson){
            DeleteLessonDialog(
                onClose = { journalViewModel.closeDeleteLessonDialog() },
                onDelete = { journalViewModel.deleteLesson() }
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

@Composable
fun GroupSegment(
    onAddGroup: () -> Unit,
    groupList: List<GroupData>,
    selectedGroup: String,
    onGroupChanged: (GroupData) -> Unit,
    onGroupDelete: (GroupData) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddGroup() }
            .paddingFromBaseline(30.dp, 8.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(Icons.Filled.Add, contentDescription = null)
        Text(
            text = stringResource(id = R.string.groups).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
    MainGroupDropdownMenu(
        groupList = groupList,
        selectedGroup = selectedGroup,
        onGroupChanged = onGroupChanged,
        onGroupDelete = onGroupDelete
    )
}

@Composable
fun LessonSegment(
    lessonList: List<LessonData>,
    onAddLesson: ()-> Unit,
    onDeleteLesson: (LessonData)-> Unit,
    showAddButton: Boolean,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if(showAddButton) onAddLesson() }
            .paddingFromBaseline(30.dp, 8.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(showAddButton) Icon(Icons.Filled.Add, contentDescription = null)
        Text(
            text = stringResource(id = R.string.lessons).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
    LessonList(
        lessonList = lessonList,
        onDeleteLesson = onDeleteLesson
    )
}


@Composable
fun LessonList(
    lessonList: List<LessonData>,
    onDeleteLesson: (LessonData) -> Unit,
){
    LazyRow(){
        items(lessonList) { item ->
            LessonCard(item, { onDeleteLesson(item) })
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LessonCard(
    lesson: LessonData,
    onDeleteLesson: () -> Unit,
){
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(min = 100.dp, max = 380.dp)
                .combinedClickable(
                    onClick = { },
                    onLongClick = { menuExpanded = true },
                )
        ) {
            Column(
            ) {
                Text(text = lesson.theme, maxLines = 2, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(all = 8.dp))
                Text(text = "Д/з: " + lesson.homeWork, maxLines = 2, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(horizontal = 8.dp))
                Row(
                    modifier = Modifier.padding(all = 8.dp)
                ){
                    Icon(Icons.Filled.DateRange, null, Modifier.padding(end = 8.dp))
                    Text(text = getDateInstance().format(Date(lesson.dateInMillis)), Modifier.padding(end = 16.dp))
                    Icon(
                        painter = painterResource(R.drawable.baseline_access_time_24),
                        contentDescription = null,
                        Modifier.padding(end = 8.dp)
                    )
                    Text(text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Time(lesson.hour, lesson.minute, 0)))
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
                    onDeleteLesson()
                    menuExpanded = false
                },
            )
        }
    }
}


@Composable
fun StudentSegment(
        studentList: List<StudentData>,
        onDeleteStudent: (StudentData)-> Unit,
        onAddStudent: ()-> Unit,
        showAddButton: Boolean,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if(showAddButton) onAddStudent() }
            .paddingFromBaseline(30.dp, 8.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(showAddButton) Icon(Icons.Filled.Add, contentDescription = null)
        Text(
            text = stringResource(id = R.string.students).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
    StudentsList(
        list = studentList,
        onDeleteStudent = onDeleteStudent,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGroupDropdownMenu(
    groupList: List<GroupData>,
    selectedGroup: String,
    onGroupChanged: (GroupData)-> Unit,
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
            }
        }
    }
}


@Composable
fun StudentsList(
    list: List<StudentData>,
    onDeleteStudent: (StudentData) -> Unit
){
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn() {
            items(list) { item ->
                StudentCard(item, { onDeleteStudent(item) })
            }
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column() {
                    Text(text = student.surname, style = MaterialTheme.typography.titleLarge)
                    Text(text = student.name)
                }
                Spacer(Modifier.weight(1f))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonDialog(
    onClose: ()-> Unit,
    onAdded: (LessonData)-> Unit,
    currentGroup: GroupData,
    lessonAlreadyExist: Boolean
){
    var lessonTheme by remember{ mutableStateOf("") }
    var showLessonThemeError by remember{ mutableStateOf(false) }
    var homeWork by remember{ mutableStateOf("") }
    var showDatePicker by remember{ mutableStateOf(false) }
    var showTimePicker by remember{ mutableStateOf(false) }
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date()


    val datePickerState = rememberDatePickerState (
        yearRange = (2023 .. 2050),
        initialSelectedDateMillis = mCalendar.timeInMillis,
        initialDisplayedMonthMillis = null,
        initialDisplayMode = DisplayMode.Picker
    )
    val timePickerState = remember {
        TimePickerState(
            is24Hour = true,
            initialHour = mCalendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = mCalendar.get(Calendar.MINUTE)
        )
    }

    AlertDialog(
        onDismissRequest = {
            if(showTimePicker || showDatePicker){
                showTimePicker = false
                showDatePicker = false
            } else onClose()
                           },
        confirmButton = {
            when{
                showDatePicker || showTimePicker ->
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            showTimePicker = false
                        }
                    ){
                        Text("Выбрать")
                    }
                else ->
                    TextButton(
                        onClick = {
                            if(lessonTheme.isEmpty()) showLessonThemeError = true
                            else onAdded(LessonData(0, lessonTheme, homeWork, currentGroup.groupName, datePickerState.selectedDateMillis!!, timePickerState.hour, timePickerState.minute))
                        }
                    ){
                        Text("Добавить")
                    }
            }
        },
        dismissButton = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            when {
                showDatePicker -> Text("Выберите дату")
                showTimePicker -> Text("Выберите время")
                (!showDatePicker && !showTimePicker) -> Text("Добавить занятие")
        }},
        text  = {
            when {
                (showDatePicker) ->
                    DatePicker(state = datePickerState, title = null)
                (showTimePicker) ->
                    TimePicker(state = timePickerState)
                (!showDatePicker && !showTimePicker) ->
                    Column(
                    ) {
                        OutlinedTextField(
                            value = lessonTheme,
                            onValueChange = {
                                lessonTheme = it
                                showLessonThemeError = false
                                            },
                            label = { Text("Тема") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            maxLines = 2,
                        )
                        if (showLessonThemeError) {
                            Text(
                                text = "введите тему занятия",
                                color = Color.Red,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        OutlinedTextField(
                            value = homeWork,
                            onValueChange = { homeWork = it },
                            label = { Text("Домашнее задание") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            maxLines = 2,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Button(
                                onClick = { showDatePicker = true }
                            ) {
                                Row (

                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                        ){
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = null,
                                        Modifier.padding(end = 8.dp)
                                    )
                                    Text(text = getDateInstance().format(Date(datePickerState.selectedDateMillis!!)))
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(1.dp)
                            )
                            Button(
                                onClick = { showTimePicker = true }
                            ) {
                                Row (

                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                        ){
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_access_time_24),
                                        contentDescription = null,
                                        Modifier.padding(end = 8.dp)
                                    )
                                    Text(text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Time(timePickerState.hour, timePickerState.minute, 0)))
                                }
                            }
                        }
                        if (lessonAlreadyExist) {
                            Text(
                                text = "на это время уже запланировано занятие",
                                color = Color.Red,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
            }
        },
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}


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

@Composable
fun AddStudentDialog(
    onClose: ()-> Unit,
    onAdded: (StudentData)-> Unit,
    isContain: Boolean,
    hideError: ()-> Unit,
    currentGroup: GroupData
){
    var nameText by remember { mutableStateOf("") }
    var surnameText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(
                onClick = { onAdded(StudentData(name = nameText, surname = surnameText, group = currentGroup.groupName)) }
            ) {
                Text(stringResource(id = R.string.add))
            }
        },
        dismissButton = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text("Добавить ученика") },
        text  = {
            Column() {
                OutlinedTextField(
                    value = surnameText,
                    onValueChange = {
                        surnameText = it
                        hideError()
                    },
                    label = { Text("Фамилия") },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                OutlinedTextField(
                    value = nameText,
                    onValueChange = {
                        nameText = it
                        hideError()
                    },
                    label = { Text("Имя Отчество") },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                if (isContain) { Text("Этот ученик уже есть в этой группе", color = Color.Red) }
            }
        },
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.padding(horizontal = 8.dp),
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
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}

@Composable
fun DeleteLessonDialog(
    onClose: () -> Unit,
    onDelete: () -> Unit
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
        shape = MaterialTheme.shapes.large,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 8.dp),
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
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeachersJournalTheme {
        GroupScreen()
    }
}