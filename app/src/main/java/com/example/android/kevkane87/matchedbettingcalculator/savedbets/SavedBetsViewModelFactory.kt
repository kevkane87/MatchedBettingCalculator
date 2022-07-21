package com.example.android.kevkane87.matchedbettingcalculator.savedbets

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SavedBetsViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedBetsViewModel::class.java)) {
            return SavedBetsViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}