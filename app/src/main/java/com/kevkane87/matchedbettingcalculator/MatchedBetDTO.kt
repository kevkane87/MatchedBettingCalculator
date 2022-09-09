package com.kevkane87.matchedbettingcalculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//database model for matched bet
@Entity(tableName = "bets")
data class MatchedBetDTO(
    @ColumnInfo(name = "betDate") var betDate: String?,
    @ColumnInfo(name = "betName") var betName: String?,
    @ColumnInfo(name = "betType") var betType: String?,
    @ColumnInfo(name = "betDetails") var betDetails: String?,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)




