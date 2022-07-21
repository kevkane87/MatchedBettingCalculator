package com.example.android.kevkane87.matchedbettingcalculator

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


//domain level model for matched bet
data class MatchedBetDataItem(

    var betDate: String,
    var betName: String,
    var betDetails: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()

): Serializable