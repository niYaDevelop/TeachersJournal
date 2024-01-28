package com.example.teachersjournal.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.teachersjournal.R

sealed class BottomNavItem(var title:Int, var icon: ImageVector, var screen_route:String){

    object Lesson : BottomNavItem(R.string.lessons , Icons.Filled.Search,"lesson")
    object Daily: BottomNavItem(R.string.daily, Icons.Filled.Favorite,"daily")
    object Groups : BottomNavItem(R.string.groups, Icons.Filled.Search,"groups")
    object Settings: BottomNavItem(R.string.settings, Icons.Filled.Favorite,"settings")
}
