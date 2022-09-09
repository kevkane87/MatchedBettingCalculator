package com.kevkane87.matchedbettingcalculator.refundifcalculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RefundIfCalculatorViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefundIfCalculatorViewModel::class.java)) {
            return RefundIfCalculatorViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}