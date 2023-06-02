package com.example.teachersjournal.ui.screens


import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teachersjournal.R
import com.example.teachersjournal.data.StudentData
import com.example.teachersjournal.ui.JournalViewModel
import com.example.teachersjournal.ui.theme.TeachersJournalTheme
import java.util.Locale


@Composable
fun GroupScreen(journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)){

    val uiState by journalViewModel.uiState.collectAsState()


    Box() {
        Column() {
            GroupDropdownBox()
            Text(
                text = stringResource(id = R.string.students).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .paddingFromBaseline(40.dp, 8.dp)
                    .padding(horizontal = 16.dp)
            )
            StudentsList(
                arrayListOf(
                    StudentData(
                        group = "1A",
                        name = "Иван Иванович",
                        surname = "Иванов"
                    )
                )
            )
        }
//        if(openDeleteDialog){
//            DeleteDialog(
//                student = uiState.studentToDelete!!,
//                onClose = { /*TODO*/ },
//                onDelete = {}
//            )
//        }
    }
}

@Composable
fun GroupDropdownBox(){}

@Composable
fun StudentsList(
    list: List<StudentData>
){
    lateinit var deletedStudent: StudentData

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn() {
            items(list) { item ->
                StudentCard(item, {})
            }
        }
        ExtendedFloatingActionButton(
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Filled.Add, null) },
            text = { Text(text = stringResource(id = R.string.add_student)) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
//        if(deleteDialogueIsOpen){
//            DeleteDialogue(
//                onClose = {},
//                onDelete = {},
//                student = StudentData()
//            )
//        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentCard(
    student: StudentData,
    onDeleteStudent: () -> Unit
){
    var menuExpanded by remember { mutableStateOf(false) }
    var openDeleteDialogue by remember { mutableStateOf(false) }

    Box() {
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
        ) {
            DropdownMenuItem(
                leadingIcon = { Icon(Icons.Filled.Delete, null) },
                text = {Text(stringResource(id = R.string.delete))},
                onClick = {openDeleteDialogue = true},
            )
        }
//        if(openDeleteDialogue) {
//            DeleteDialogue(
//                student = student,
//                onClose = { openDeleteDialogue = false },
//                onDelete = onDeleteStudent
//            )
//        }
    }
}


@Composable
fun DeleteDialog(
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
                Text("удалить ученика ${student.surname} ${student.name} из группы '${student.group}'?")
        },
        shape = MaterialTheme.shapes.large,
    )
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeachersJournalTheme {
        GroupScreen()
    }
}