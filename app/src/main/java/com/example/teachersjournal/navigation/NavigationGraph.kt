package com.example.teachersjournal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teachersjournal.ui.JournalViewModel
import com.example.teachersjournal.ui.screens.DailyScreen
import com.example.teachersjournal.ui.screens.GroupScreen
import com.example.teachersjournal.ui.screens.LessonScreen
import com.example.teachersjournal.ui.screens.SettingsScreen

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: JournalViewModel, modifier : Modifier) {
    NavHost(navController, startDestination = BottomNavItem.Groups.screen_route, modifier = modifier) {
        composable(BottomNavItem.Lesson.screen_route) {
            LessonScreen(viewModel)
        }
        composable(BottomNavItem.Daily.screen_route) {
            DailyScreen(viewModel)
        }
        composable(BottomNavItem.Groups.screen_route) {
            GroupScreen(viewModel)
        }
        composable(BottomNavItem.Settings.screen_route) {
            SettingsScreen(viewModel)
        }
    }
}