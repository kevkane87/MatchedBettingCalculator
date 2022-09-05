package com.example.android.kevkane87.matchedbettingcalculator.refundcalculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class RefundCalculatorViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefundCalculatorViewModel::class.java)) {
            return RefundCalculatorViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}