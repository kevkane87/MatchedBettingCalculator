package com.kevkane87.matchedbettingcalculator.savedbets

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevkane87.matchedbettingcalculator.Repository
import com.kevkane87.matchedbettingcalculator.database.BetDatabase
import com.kevkane87.matchedbettingcalculator.Result
import com.kevkane87.matchedbettingcalculator.MatchedBetDTO
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class SavedBetsViewModel (application: Application) : ViewModel(){

    private val repository = Repository(BetDatabase.getDatabase(application))

     val betList = MutableLiveData<MutableList<MatchedBetDTO>>()

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
                            bet.betType,
                            bet.betDetails,
                            bet.id
                        )
                    })

                    dataList.reverse()
                    betList.value = dataList
                }
                else -> {}
            }
        }
    }

    fun deleteBet(id: String){
        viewModelScope.launch {
            repository.deleteBet((id))
        }
    }
}
private const val TAG = "SavedBetsViewModel"