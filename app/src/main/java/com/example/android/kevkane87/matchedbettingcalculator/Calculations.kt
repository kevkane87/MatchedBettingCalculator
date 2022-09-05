package com.example.android.kevkane87.matchedbettingcalculator

import java.lang.Double.max
import java.lang.Double.min


fun layStakeQualNor(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double):Double {
   return (backStake + (backStake * backOdds - backStake) * (1 - backComm)) / (layOdds - layComm)
}

fun layStakeQualUlay(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double):Double {
    return  min(
        backStake / (1 - layComm),
        (backStake * backOdds - backStake) * (1 - backComm) / (layOdds - 1))
}

fun layStakeQualOlay(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double):Double {
    return max(
        backStake / (1 - layComm),
        (backStake * backOdds - backStake) * (1 - backComm) / (layOdds - 1))
}

fun layStakeSNRNor(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double):Double {
    return (backStake * backOdds - backStake) * (1 - backComm) / (layOdds - layComm)
}

fun layStakeSNRUlay(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double):Double {
    return  min(
        backStake / (1 - layComm),
        ((backStake * backOdds - backStake) * (1 - backComm) - backStake) / (layOdds - 1))
}

fun layStakeSNROlay(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double):Double {
    return max(
        backStake / (1 - layComm),
        ((backStake * backOdds - backStake) * (1 - backComm) - backStake) / (layOdds - 1))
}

fun layLiability(layOdds: Double, layStake: Double):Double {
    return layOdds * layStake - layStake
}

fun profitBackWinsQual(backStake: Double,backOdds: Double, backComm: Double, layLiability: Double ):Double{
    return (backStake * backOdds - backStake)*(1-backComm)-layLiability
}

fun profitLayWinsQual(layStake: Double, backStake: Double, layComm: Double ):Double{
    return layStake*(1-layComm)-backStake
}

fun profitBackWinsSNR(backStake: Double,backOdds: Double, backComm: Double, layLiability: Double ):Double{
    return (backStake * backOdds - backStake)*(1-backComm)-layLiability
}

fun profitLayWinsSNR(layStake: Double, layComm: Double ):Double{
    return layStake*(1-layComm)
}

fun profitBackWinsSR(backStake: Double, backOdds: Double, backComm: Double, layLiability: Double ):Double{
    return (backStake * backOdds - backStake)*(1-backComm)-layLiability + backStake
}

fun profitLayWinsSR(layStake: Double, layComm: Double ):Double{
    return layStake*(1-layComm)
}

fun layStakeQualNorRef(backStake: Double, backOdds: Double, backComm: Double, layOdds: Double, layComm: Double, refund: Double):Double {
    return ((backStake + (backStake * backOdds - backStake) * (1 - backComm)) - refund)/ (layOdds - layComm)
}




