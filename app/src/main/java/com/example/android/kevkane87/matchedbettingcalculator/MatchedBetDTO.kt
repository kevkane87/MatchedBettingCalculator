package com.example.android.kevkane87.matchedbettingcalculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//database model for matched bet
@Entity(tableName = "bets")
data class MatchedBetDTO(
    @ColumnInfo(name = "betDate") var betDate: String?,
    @ColumnInfo(name = "betName") var betName: String?,
    @ColumnInfo(name = "betDetails") var betDetails: String?,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)




