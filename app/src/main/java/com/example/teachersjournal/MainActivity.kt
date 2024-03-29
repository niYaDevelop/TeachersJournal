package com.example.teachersjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.teachersjournal.navigation.BottomNavigationBar
import com.example.teachersjournal.navigation.NavigationGraph
import com.example.teachersjournal.ui.JournalViewModel
import com.example.teachersjournal.ui.screens.GroupScreen
import com.example.teachersjournal.ui.theme.TeachersJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeachersJournalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScreen()
                }
            }
        }
    }
}

@Composable
fun AppScreen(){
    val navController = rememberNavController()
    val journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavigationGraph(navController = navController, journalViewModel, Modifier.padding(innerPadding))
    }
}