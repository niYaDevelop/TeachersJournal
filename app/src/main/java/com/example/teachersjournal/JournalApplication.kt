package com.example.teachersjournal

import android.app.Application
import com.example.teachersjournal.data.AppDatabase

class JournalApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}