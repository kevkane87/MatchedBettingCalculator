package com.example.android.kevkane87.matchedbettingcalculator.savedbets

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.kevkane87.matchedbettingcalculator.Repository
import com.example.android.kevkane87.matchedbettingcalculator.database.BetDatabase
import com.example.android.kevkane87.matchedbettingcalculator.Result
import com.example.android.kevkane87.matchedbettingcalculator.MatchedBetDTO
import kotlinx.coroutines.launch

class SavedBetsViewModel (application: Application) : ViewModel(){

    private val repository = Repository(BetDatabase.getDatabase(application))

     val betList = MutableLiveData<List<MatchedBetDTO>>()

    //loads saved bets using coroutine
    fun loadBets() {
        viewModelScope.launch {
            when (val result = repository.getSavedBets()) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<MatchedBetDTO>()
                    dataList.addAll((result.data as List<MatchedBetDTO>).map { bet ->
                        //map the bet data from the DB to the be ready to be displayed on the UI
                        MatchedBetDTO(
                            bet.betDate,
                            bet.betName,
                            bet.betDetails,
                            bet.id
                        )
                    })
                    betList.value = dataList
                }
                is Result.Error ->
                    //Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                Log.e(TAG,"Error getting data")
            }
        }
    }
}
private const val TAG = "SavedBetsViewModel"