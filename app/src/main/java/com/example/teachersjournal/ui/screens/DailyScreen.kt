package com.example.teachersjournal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teachersjournal.R
import com.example.teachersjournal.ui.JournalViewModel

// Расписание занятий на сегодня, завтра, выбранную дату.
// Добавить возможность создавать занятия на этом экране
@Composable
fun DailyScreen(journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)){

    val uiState by journalViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.daily)
        )
    }
}