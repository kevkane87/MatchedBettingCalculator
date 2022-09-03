package com.example.android.kevkane87.matchedbettingcalculator.oddsconvertercalculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OddsConverterCalculatorViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OddsConverterCalculatorViewModel::class.java)) {
            return OddsConverterCalculatorViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}