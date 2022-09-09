package com.kevkane87.matchedbettingcalculator.normalcalculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NormalCalculatorViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NormalCalculatorViewModel::class.java)) {
            return NormalCalculatorViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}