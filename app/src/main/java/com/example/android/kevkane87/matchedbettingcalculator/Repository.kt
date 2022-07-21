package com.example.android.kevkane87.matchedbettingcalculator

import com.example.android.kevkane87.matchedbettingcalculator.database.BetDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


//repository for accessing database
class Repository(private val database: BetDatabase) {

    suspend fun saveBet(bet: MatchedBetDTO) =
            withContext(Dispatchers.IO) {
                try {
                    database.betDao.saveBet(bet)
                }
                catch (ex: Exception) {
                    Result.Error(ex.localizedMessage)
                }
            }

    suspend fun getSavedBets(): Result<List<MatchedBetDTO>> = withContext(Dispatchers.IO){
        withContext(Dispatchers.IO) {
           try {
                Result.Success(database.betDao.getSavedBets())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }
    }
}

