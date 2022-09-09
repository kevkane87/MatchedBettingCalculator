package com.kevkane87.matchedbettingcalculator.eachwaycalculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EachWayCalculatorViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EachWayCalculatorViewModel::class.java)) {
            return EachWayCalculatorViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}