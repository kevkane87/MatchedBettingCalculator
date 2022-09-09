package com.kevkane87.matchedbettingcalculator.eachwaycalculator

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevkane87.matchedbettingcalculator.*
import com.kevkane87.matchedbettingcalculator.database.BetDatabase.Companion.getDatabase
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EachWayCalculatorViewModel(application: Application) : ViewModel() {

    private val repository = Repository(getDatabase(application))

    //Live data properties
    private val _backBetStakeEw = MutableLiveData<Double>()
    val backBetStakeEw: MutableLiveData<Double>
        get() = _backBetStakeEw

    private val _backBetStakeTotal = MutableLiveData<Double>()
    val backBetStakeTotal: MutableLiveData<Double>
        get() = _backBetStakeTotal

    private val _backBetOdds = MutableLiveData<Double>()
    val backBetOdds: MutableLiveData<Double>
        get() = _backBetOdds

    private val _backBetPlaceOdds = MutableLiveData<Double>()
    val backBetPlaceOdds: MutableLiveData<Double>
        get() = _backBetPlaceOdds

    private val _placePayout = MutableLiveData<Int>()
    val placePayout: MutableLiveData<Int>
        get() = _placePayout

    private val _layBetOdds = MutableLiveData<Double>()
    val layBetOdds: MutableLiveData<Double>
        get() = _layBetOdds

    private val _exchangeCommission = MutableLiveData<Double>()
    val exchangeCommission: MutableLiveData<Double>
        get() = _exchangeCommission

    private val _placeLayOdds = MutableLiveData<Double>()
    val placeLayOdds: MutableLiveData<Double>
        get() = _placeLayOdds

    private val _placeLayComm = MutableLiveData<Double>()
    val placeLayComm: MutableLiveData<Double>
        get() = _placeLayComm

    private val _backCommission = MutableLiveData<Double>()
    val backCommission: MutableLiveData<Double>
        get() = _backCommission

    private val _layStakeWin = MutableLiveData<Double>()
    val layStakeWin: MutableLiveData<Double>
        get() = _layStakeWin

    private val _layLiabilityPlace = MutableLiveData<Double>()
    val layLiabilityPlace: MutableLiveData<Double>
        get() = _layLiabilityPlace

    private val _layStakePlace = MutableLiveData<Double>()
    val layStakePlace: MutableLiveData<Double>
        get() = _layStakePlace

    private val _layLiabilityWin = MutableLiveData<Double>()
    val layLiabilityWin: MutableLiveData<Double>
        get() = _layLiabilityWin

    private val _profitBackWins = MutableLiveData<Double>()
    val profitBackWins: MutableLiveData<Double>
        get() = _profitBackWins

    private val _profitLayWins = MutableLiveData<Double>()
    val profitLayWins: MutableLiveData<Double>
        get() = _profitLayWins

    private val _profitExtraPlace = MutableLiveData<Double>()
    val profitExtraPlace: MutableLiveData<Double>
        get() = _profitExtraPlace

    private val _betType = MutableLiveData<String>()
    val betType: MutableLiveData<String>
        get() = _betType

    private val _radioInputChecked = MutableLiveData<Int>()
    val radioInputChecked: MutableLiveData<Int>
        get() = _radioInputChecked

    private val _betDetails = MutableLiveData<String>()
    val betDetails: MutableLiveData<String>
        get() = _betDetails

    private val _betName = MutableLiveData<String>()
    val betName: MutableLiveData<String>
        get() = _betName

    private val _id = MutableLiveData<String>()
    val id: MutableLiveData<String>
        get() = _id

    private var _bet = MutableLiveData<MatchedBetDTO>()
    val bet: MutableLiveData<MatchedBetDTO>
        get() = _bet

    private val _currencySymbol = MutableLiveData<String>()
    val currencySymbol: MutableLiveData<String>
        get() = _currencySymbol

    var defaultLayCommission: Double = 0.0
    var defaultPlacePayout: Int = 5

    val backCommCheckboxSate = MutableLiveData<Boolean>()
    val extraPlaceCheckboxSate = MutableLiveData<Boolean>()

    init {
        setRadioButton()
        backCommCheckboxSate.value = false
        extraPlaceCheckboxSate.value = false
        _betType.value = ""
    }

    fun clear() {
        _backBetStakeEw.value = 0.0
        _backBetStakeTotal.value = 0.0
        _backBetOdds.value = 0.0
        _placePayout.value = 0
        _backBetPlaceOdds.value = 0.0
        _layBetOdds.value = 0.0
        _exchangeCommission.value = defaultLayCommission
        _placeLayOdds.value = 0.0
        _placeLayComm.value = defaultLayCommission
        _backCommission.value = 0.0
        _layStakeWin.value = 0.0
        _layStakePlace.value = 0.0
        _layLiabilityWin.value = 0.0
        _layLiabilityPlace.value = 0.0
        _profitBackWins.value = 0.0
        _profitLayWins.value = 0.0
        _profitExtraPlace.value = 0.0
        _id.value = ""

    }

    //sets radio button for correct bet type
    fun setRadioButton() {
        when (_betType.value.toString()) {
            "Qualifier" ->
                _radioInputChecked.postValue(R.id.qualifier)
            "" ->
                _radioInputChecked.postValue(R.id.qualifier)
            "SNR" ->
                _radioInputChecked.postValue(R.id.snr)
            "SR" ->
                _radioInputChecked.postValue(R.id.sr)
        }
    }

    fun canCalculate(): Boolean {
        return (_backBetOdds.value != null && _backBetStakeEw.value != null && _layBetOdds.value != null && _placePayout.value != null && _placeLayOdds.value != null
                && _backBetOdds.value!! > 1.0 && _backBetStakeEw.value != 0.0 && _layBetOdds.value!! > 1.0 && _placePayout.value != 0 && _placeLayOdds.value!! > 1.0)
    }

    private fun isStakeInput(): Boolean {
        return (_backBetStakeEw.value != null && _backBetStakeEw.value != 0.0)
    }

    private fun isOddsInput(): Boolean {
        return (_backBetOdds.value != null && _backBetOdds.value!! > 1.0 && _placePayout.value != null && _placePayout.value != 0)
    }

    //function provides matched bet calculations
    fun calculate() {

        if (isStakeInput()) _backBetStakeTotal.value = _backBetStakeEw.value!! * 2 else _backBetStakeTotal.value = 0.0

        if (isOddsInput()) _backBetPlaceOdds.value =
            (_backBetOdds.value!! - 1) / _placePayout.value!! + 1 else _backBetPlaceOdds.value = 0.0

        if (canCalculate()) {

            val backCommDecimal = backCommission.value!! / 100
            val layCommDecimal = exchangeCommission.value!! / 100
            val layOdds = _layBetOdds.value!!
            val layOddsPlace = _placeLayOdds.value!!
            val layCommPlace = _placeLayComm.value!! / 100

            when (_betType.value) {

                "Qualifier" -> {


                    _layStakeWin.value = layStakeQualNor(
                        _backBetStakeEw.value!!,
                        _backBetOdds.value!!,
                        backCommDecimal,
                        layOdds,
                        layCommDecimal
                    )
                    _layStakePlace.value = layStakeQualNor(
                        _backBetStakeEw.value!!,
                        _backBetPlaceOdds.value!!,
                        backCommDecimal,
                        layOddsPlace,
                        layCommPlace
                    )

                    _layLiabilityWin.value = layLiability(layOdds, _layStakeWin.value!!)
                    _profitBackWins.value = profitBackWinsQual(
                        _backBetStakeEw.value!!,
                        _backBetOdds.value!!,
                        backCommDecimal,
                        _layLiabilityWin.value!!
                    )
                    _profitLayWins.value = profitLayWinsQual(
                        _layStakeWin.value!!,
                        _backBetStakeEw.value!!,
                        layCommDecimal
                    )
                    _layLiabilityPlace.value = layLiability(layOddsPlace, _layStakePlace.value!!)
                    _profitBackWins.value = _profitBackWins.value!! + profitBackWinsQual(
                        _backBetStakeEw.value!!,
                        _backBetPlaceOdds.value!!,
                        backCommDecimal,
                        _layLiabilityPlace.value!!
                    )
                    _profitLayWins.value = _profitLayWins.value!! + profitLayWinsQual(
                        _layStakePlace.value!!,
                        _backBetStakeEw.value!!,
                        layCommPlace
                    )
                    _profitExtraPlace.value =
                        _backBetStakeEw.value!! + (_backBetPlaceOdds.value!! * _backBetStakeEw.value!! - _backBetStakeEw.value!!) * (1 - backCommDecimal) + _profitBackWins.value!!
                }

                "SNR" -> {

                    _layStakeWin.value = layStakeSNRNor(
                        _backBetStakeEw.value!!,
                        _backBetOdds.value!!,
                        backCommDecimal,
                        layOdds,
                        layCommDecimal
                    )
                    _layStakePlace.value = layStakeSNRNor(
                        _backBetStakeEw.value!!,
                        _backBetPlaceOdds.value!!,
                        backCommDecimal,
                        layOddsPlace,
                        layCommPlace
                    )

                    _layLiabilityWin.value = layLiability(_layBetOdds.value!!, _layStakeWin.value!!)
                    _profitBackWins.value = profitBackWinsSNR(
                        _backBetStakeEw.value!!,
                        _backBetOdds.value!!,
                        backCommDecimal,
                        _layLiabilityWin.value!!
                    )
                    _profitLayWins.value = profitLayWinsSNR(_layStakeWin.value!!, layCommDecimal)
                    _layLiabilityPlace.value = layLiability(layOddsPlace, _layStakePlace.value!!)
                    _profitBackWins.value = _profitBackWins.value!! + profitBackWinsSNR(
                        _backBetStakeEw.value!!,
                        _backBetPlaceOdds.value!!,
                        backCommDecimal,
                        _layLiabilityPlace.value!!
                    )
                    _profitLayWins.value = _profitLayWins.value!! + profitLayWinsSNR(
                        _layStakePlace.value!!,
                        layCommPlace
                    )
                    _profitExtraPlace.value =
                        (_backBetPlaceOdds.value!! * _backBetStakeEw.value!! - _backBetStakeEw.value!!) * (1 - backCommDecimal) + _profitBackWins.value!!
                }

                "SR" -> {

                    _layStakeWin.value = layStakeQualNor(
                        _backBetStakeEw.value!!,
                        _backBetOdds.value!!,
                        backCommDecimal,
                        layOdds,
                        layCommDecimal
                    )
                    _layStakePlace.value = layStakeQualNor(
                        _backBetStakeEw.value!!,
                        _backBetPlaceOdds.value!!,
                        backCommDecimal,
                        layOddsPlace,
                        layCommPlace
                    )
                    _layLiabilityWin.value = layLiability(layOdds, _layStakeWin.value!!)
                    _profitBackWins.value = profitBackWinsSR(
                        _backBetStakeEw.value!!,
                        _backBetOdds.value!!,
                        backCommDecimal,
                        _layLiabilityWin.value!!
                    )
                    _profitLayWins.value = profitLayWinsSR(_layStakeWin.value!!, layCommDecimal)
                    _layLiabilityPlace.value = layLiability(layOddsPlace, _layStakePlace.value!!)
                    _profitBackWins.value = _profitBackWins.value!! + profitBackWinsSR(
                        _backBetStakeEw.value!!,
                        _backBetPlaceOdds.value!!,
                        backCommDecimal,
                        _layLiabilityPlace.value!!
                    )
                    _profitLayWins.value = _profitLayWins.value!! + profitLayWinsSR(
                        _layStakePlace.value!!,
                        layCommPlace
                    )
                    _profitExtraPlace.value =
                        (_backBetPlaceOdds.value!! * _backBetStakeEw.value!!) * (1 - backCommDecimal) + _profitBackWins.value!!

                }
            }
        } else {
            _layStakeWin.value = 0.0
            _layLiabilityWin.value = 0.0
            _layStakePlace.value = 0.0
            _layLiabilityPlace.value = 0.0
            _profitBackWins.value = 0.0
            _profitLayWins.value = 0.0
        }
    }


    //sets bet type from radio group
    fun setBetType() {
        when (_radioInputChecked.value) {
            R.id.qualifier -> _betType.value = "Qualifier"
            R.id.snr -> _betType.value = "SNR"
            R.id.sr -> _betType.value = "SR"
        }
    }

    fun setBetDetails() {

        val builder = StringBuilder()
        val df = DecimalFormat("#.######")
        val cf = NumberFormat.getCurrencyInstance(Locale.UK)

        builder.append("E/W stake: " + cf.format(_backBetStakeEw.value))
        builder.append(", Back odds: " + df.format(_backBetOdds.value))
        builder.append(", Pl payout: 1/" + _placePayout.value)
        if (_backCommission.value!! > 0.0) builder.append(
            ", Back comm: " + df.format(
                _backCommission.value
            ) + "%\n"
        ) else builder.append("\n")

        builder.append("Lay odds (win): " + df.format(_layBetOdds.value))
        builder.append(", Lay comm (win): " + df.format(_exchangeCommission.value) + "%\n")

        builder.append("Lay odds (pl): " + df.format(_placeLayOdds.value))
        builder.append(", Lay comm (pl): " + df.format(_placeLayComm.value) + "%\n")

        builder.append("Lay stake (win): " + cf.format(_layStakeWin.value))
        builder.append(", Lay liab (win): " + cf.format(_layLiabilityWin.value) + "\n")

        builder.append("Lay stake (pl): " + cf.format(_layStakePlace.value))
        builder.append(", Lay liab (pl): " + cf.format(_layLiabilityPlace.value) + "\n")


        if (extraPlaceCheckboxSate.value!!) {
            builder.append("Profit:  " + cf.format(_profitBackWins.value) + " (not ex pl), ")
            builder.append(cf.format(_profitExtraPlace.value) + " (ex pl)")
        }
        else{
            builder.append("Profit: " + cf.format(_profitBackWins.value))
        }

        _betDetails.value = builder.toString()
    }


    private fun getDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val currentDateTime = LocalDate.now()
        return currentDateTime.format(formatter)
    }

    //saves bet to database with coroutine
    fun saveBet() {
        val dateToday = getDate()
        viewModelScope.launch {
            repository.saveBet(
                MatchedBetDTO(dateToday, _betName.value!!, "Each Way Bet", _betDetails.value!!)
            )
        }
    }

}