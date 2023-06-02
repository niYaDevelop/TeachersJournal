package com.example.teachersjournal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.teachersjournal.JournalApplication
import com.example.teachersjournal.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JournalViewModel(
    private val journalDatabase: AppDatabase
): ViewModel() {

    private val _uiState = MutableStateFlow(JournalStateUI())
    val uiState: StateFlow<JournalStateUI> = _uiState.asStateFlow()





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