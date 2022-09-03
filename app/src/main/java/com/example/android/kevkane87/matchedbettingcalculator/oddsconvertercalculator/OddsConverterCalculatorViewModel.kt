package com.example.android.kevkane87.matchedbettingcalculator.oddsconvertercalculator

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.kevkane87.matchedbettingcalculator.MatchedBetDTO
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.Repository
import com.example.android.kevkane87.matchedbettingcalculator.database.BetDatabase
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

class OddsConverterCalculatorViewModel(application: Application) : ViewModel() {

    private val repository = Repository(BetDatabase.getDatabase(application))

    private val _fractionalOddsNumerator = MutableLiveData<Int>()
    val fractionalOddsNumerator: MutableLiveData<Int>
        get() = _fractionalOddsNumerator

    private val _fractionalOddsDenominator = MutableLiveData<Int>()
    val fractionalOddsDenominator: MutableLiveData<Int>
        get() = _fractionalOddsDenominator

    private val _decimalOdds = MutableLiveData<Double>()
    val decimalOdds: MutableLiveData<Double>
        get() = _decimalOdds

    private val _americanOdds = MutableLiveData<Double>()
    val americanOdds: MutableLiveData<Double>
        get() = _americanOdds

    private val _americanOddsSign = MutableLiveData<String>()
    val americanOddsSign: MutableLiveData<String>
        get() = _americanOddsSign

    private val _probability = MutableLiveData<Double>()
    val probability: MutableLiveData<Double>
        get() = _probability

    private val _betName = MutableLiveData<String>()
    val betName: MutableLiveData<String>
        get() = _betName

    private val _betDetails = MutableLiveData<String>()


    fun calculateFromFractional(){

        val dec = _fractionalOddsNumerator.value!!.toDouble() / _fractionalOddsDenominator.value!!.toDouble()

        _decimalOdds.value = dec + 1

        calculateAmerican(dec)

        _probability.value = 100 / _decimalOdds.value!!

    }

    fun calculateFromDecimal(){

        val dec = _decimalOdds.value!!-1
        calculateFraction(dec)
        calculateAmerican(dec)
        _probability.value = 100 / _decimalOdds.value!!

    }

    fun calculateFromAmerican(){

        val dec: Double = if(_americanOddsSign.value=="+"){
            _americanOdds.value!!/100.00
        } else{
            100.00/_americanOdds.value!!
        }

        _decimalOdds.value = dec + 1
        calculateFraction(dec)
        _probability.value = 100 / _decimalOdds.value!!
    }

    fun calculateFromProbability(){

        _decimalOdds.value = 100/probability.value!!
        val dec = _decimalOdds.value!!-1
        calculateFraction(dec)
        calculateAmerican(dec)

    }

    private fun calculateAmerican(dec: Double){

        if (dec >= 1) {
            _americanOddsSign.value = "+"
            _americanOdds.value = dec*100
        }
        else{
            _americanOddsSign.value = "-"
            _americanOdds.value = 100/dec
        }
    }

    private fun calculateFraction(x: Double) {
        val tolerance = 1.0E-6
        var h1=1.0
        var h2=0.0
        var k1=0.0
        var k2=1.0
        var b = x
        do {
            val a = floor(b)
            var aux = h1
            h1 = a*h1+h2
            h2 = aux
            aux = k1
            k1 = a*k1+k2
            k2 = aux
            b = 1/(b-a)
        } while (abs(x-h1/k1) > x*tolerance)

        _fractionalOddsNumerator.value = h1.toInt()
        _fractionalOddsDenominator.value = k1.toInt()
    }

    fun saveBet() {
        val dateToday = getDate()
        viewModelScope.launch {
            repository.saveBet(
                MatchedBetDTO(dateToday, _betName.value!!, "Odds Conversion", _betDetails.value!!)
            )
        }
    }

    private fun getDate(): String{
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val currentDateTime = LocalDate.now()
        return currentDateTime.format(formatter)
    }

    fun setBetDetails(){

        val builder = StringBuilder()
        val df = DecimalFormat("#.##")


        builder.append("Factional: " + _fractionalOddsNumerator.value + " / " + _fractionalOddsDenominator.value + "\n")
        builder.append("Decimal: " + df.format(_decimalOdds.value) + "\n")
        builder.append("American: " + _americanOddsSign.value + df.format(_americanOdds.value) + "\n")
        builder.append("probability: " + df.format(_probability.value) + "%")


        _betDetails.value = builder.toString()
    }

}