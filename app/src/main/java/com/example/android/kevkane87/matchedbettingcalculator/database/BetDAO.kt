package com.example.android.kevkane87.matchedbettingcalculator.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.kevkane87.matchedbettingcalculator.MatchedBetDTO

//DAO functions for accessing database
@Dao
interface BetDAO {

    @Query("SELECT * FROM bets")
    suspend fun getSavedBets(): List<MatchedBetDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBet(bet: MatchedBetDTO)

    @Query("DELETE FROM bets where id = :betId")
    suspend fun deleteBetById(betId: String)

}