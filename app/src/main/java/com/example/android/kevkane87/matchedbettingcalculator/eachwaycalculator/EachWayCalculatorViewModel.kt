package com.example.android.kevkane87.matchedbettingcalculator.eachwaycalculator

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.kevkane87.matchedbettingcalculator.MatchedBetDataItem
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.Repository
import com.example.android.kevkane87.matchedbettingcalculator.database.BetDatabase.Companion.getDatabase
import kotlinx.coroutines.launch

class EachWayCalculatorViewModel(application: Application) : ViewModel() {

    private val repository = Repository(getDatabase(application))

    //Live data properties
    private val _backBetStake = MutableLiveData<Double>()
    val backBetStake: MutableLiveData<Double>
        get() = _backBetStake

    private val _backBetOdds = MutableLiveData<Double>()
    val backBetOdds: MutableLiveData<Double>
        get() = _backBetOdds

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

    private val _parLayStake1 = MutableLiveData<Double>()
    val parLayStake1: MutableLiveData<Double>
        get() = _parLayStake1

    private val _parLayStake2 = MutableLiveData<Double>()
    val parLayStake2: MutableLiveData<Double>
        get() = _parLayStake2

    private val _parLayOdds1 = MutableLiveData<Double>()
    val parLayOdds1: MutableLiveData<Double>
        get() = _parLayOdds1

    private val _parLayOdds2 = MutableLiveData<Double>()
    val parLayOdds2: MutableLiveData<Double>
        get() = _parLayOdds2

    private val _parLayComm1 = MutableLiveData<Double>()
    val parLayComm1: MutableLiveData<Double>
        get() = _parLayComm1

    private val _parLayComm2 = MutableLiveData<Double>()
    val parLayComm2: MutableLiveData<Double>
        get() = _parLayComm2

    private val _layStake = MutableLiveData<Double>()
    val layStake: MutableLiveData<Double>
        get() = _layStake

    private val _layLiabilityPlace = MutableLiveData<Double>()
    val layLiabilityPlace: MutableLiveData<Double>
        get() = _layLiabilityPlace

    private val _layStakePlace = MutableLiveData<Double>()
    val layStakePlace: MutableLiveData<Double>
        get() = _layStakePlace

    private val _layLiability = MutableLiveData<Double>()
    val layLiability: MutableLiveData<Double>
        get() = _layLiability

    private val _profitBackWins = MutableLiveData<Double>()
    val profitBackWins: MutableLiveData<Double>
        get() = _profitBackWins

    private val _profitLayWins = MutableLiveData<Double>()
    val profitLayWins: MutableLiveData<Double>
        get() = _profitLayWins

    private val _profitExtraPlace = MutableLiveData<Double>()
    val profitExtraPlace: MutableLiveData<Double>
        get() = _profitExtraPlace

    private val _bookieName = MutableLiveData<String>()
    val bookieName: MutableLiveData<String>
        get() = _bookieName

    private val _exchangeName = MutableLiveData<String>()
    val exchangeName: MutableLiveData<String>
        get() = _exchangeName

    private val _event = MutableLiveData<String>()
    val event: MutableLiveData<String>
        get() = _event

    private val _eventTime = MutableLiveData<String>()
    val eventTime: MutableLiveData<String>
        get() = _eventTime

    private val _outcome = MutableLiveData<String>()
    val outcome: MutableLiveData<String>
        get() = _outcome

    private val _betType = MutableLiveData<String>()
    val betType: MutableLiveData<String>
        get() = _betType

    private val _resultType = MutableLiveData<String>()
    val resultType: MutableLiveData<String>
        get() = _resultType

    private val _radioInputChecked = MutableLiveData<Int>()
    val radioInputChecked: MutableLiveData<Int>
        get() = _radioInputChecked

    private val _radioResultChecked = MutableLiveData<Int>()
    val radioResultChecked: MutableLiveData<Int>
        get() = _radioResultChecked

    private val _id = MutableLiveData<String>()
    val id: MutableLiveData<String>
        get() = _id

    private var _bet = MutableLiveData<MatchedBetDataItem>()
    val bet: MutableLiveData<MatchedBetDataItem>
        get() = _bet

    init {
        setRadioButton()
    }

    fun clear() {
        _backBetStake.value = 0.0
        _backBetOdds.value = 0.0
        _placePayout.value = 0
        _layBetOdds.value = 0.0
        _exchangeCommission.value = 0.0
        _placeLayOdds.value = 0.0
        _placeLayComm.value = 0.0
        _backCommission.value = 0.0
        _parLayStake1.value = 0.0
        _parLayStake2.value = 0.0
        _parLayOdds1.value = 0.0
        _parLayOdds2.value = 0.0
        _parLayComm1.value = 0.0
        _parLayComm2.value = 0.0
        _layStake.value = 0.0
        _layLiability.value = 0.0
        _profitBackWins.value = 0.0
        _profitLayWins.value = 0.0
        _bookieName.value = ""
        _exchangeName.value = ""
        _event.value = ""
        _outcome.value = ""
        _eventTime.value = ""
        _betType.value = ""
        _resultType.value = ""
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

        when (_resultType.value.toString()) {
            "Normal" ->
                _radioResultChecked.postValue(R.id.normal)
            "" ->
                _radioResultChecked.postValue(R.id.normal)
            "U-lay" ->
                _radioResultChecked.postValue(R.id.underlay)
            "O-lay" ->
                _radioResultChecked.postValue(R.id.overlay)
        }

    }


    //function provides matched bet calculations
    fun calculate() {

        if (_backBetOdds.value != null && _backBetStake.value != null && _layBetOdds.value != null && _backBetOdds.value != 0.0 && _backBetStake.value != 0.0 && _layBetOdds.value != 0.0) {

            val backCommDecimal = backCommission.value!! / 100
            val layCommDecimal = exchangeCommission.value!! / 100

            val layStakePar = layStakePar()
            val layOddsPar = layOddsPar()
            val layCommPar = layCommPar()
            val liabilityPar = layStakePar * layOddsPar - layStakePar

            val layOdds = if(isPartial()) layOddsPar  else _layBetOdds.value!!
            val layComm = if(isPartial()) layCommPar  else layCommDecimal

            when (_betType.value) {

                "Qualifier" -> {

                    when (_resultType.value) {

                        "Normal" ->
                            _layStake.value = layStakeQualNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                        "U-lay" ->
                            _layStake.value = layStakeQualUlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                        "O-lay" ->
                            _layStake.value = layStakeQualOlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )
                    }

                    _layLiability.value = layLiability(layOdds,_layStake.value!!)
                    _profitBackWins.value = profitBackWinsQual(_backBetStake.value!!,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!)
                    _profitLayWins.value = profitLayWinsQual(_layStake.value!!,_backBetStake.value!!,layComm)

                    //each way calc

                    if(isEachWay()) {
                        val place = _placePayout.value!!
                        val backBetPlaceOdds = (_backBetOdds.value!! - 1) / place + 1
                        val layOddsPlace = _placeLayOdds.value!!
                        val layCommPlace = _placeLayComm.value!!/100

                        when (_resultType.value) {

                            "Normal" ->
                                _layStakePlace.value = layStakeQualNor(
                                    _backBetStake.value!!,
                                    backBetPlaceOdds,
                                    backCommDecimal,
                                    layOddsPlace,
                                    layCommPlace
                                )

                            "U-lay" ->
                                _layStakePlace.value = layStakeQualUlay(
                                    _backBetStake.value!!,
                                    backBetPlaceOdds,
                                    backCommDecimal,
                                    layOddsPlace,
                                    layCommPlace
                                )

                            "O-lay" ->
                                _layStakePlace.value = layStakeQualOlay(
                                    _backBetStake.value!!,
                                    backBetPlaceOdds,
                                    backCommDecimal,
                                    layOddsPlace,
                                    layCommPlace
                                )
                        }

                        _layLiabilityPlace.value =
                            layLiability(layOddsPlace, _layStakePlace.value!!)
                        _profitBackWins.value = _profitBackWins.value!! + profitBackWinsQual(
                            _backBetStake.value!!,
                            backBetPlaceOdds,
                            backCommDecimal,
                            _layLiabilityPlace.value!!
                        )
                        _profitLayWins.value = _profitLayWins.value!! + profitLayWinsQual(
                            _layStakePlace.value!!,
                            _backBetStake.value!!,
                            layCommPlace
                        )
                        _profitExtraPlace.value = backBetPlaceOdds * _backBetStake.value!! + _profitBackWins.value!!
                    }


                    if(isPartial()){
                        val ratio = layStakePar / _layStake.value!!
                        val newBackStake = _backBetStake.value!! * (1-ratio)
                        val parBackStake = _backBetStake.value!! * (ratio)
                        when (_resultType.value) {

                            "Normal" ->
                                _layStake.value = layStakeQualNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommPar )
                            "U-lay" ->
                                _layStake.value = layStakeQualUlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommPar )
                            "O-lay" ->
                                _layStake.value = layStakeQualOlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommPar )
//
                        }

                        _layLiability.value = layLiability(_layBetOdds.value!!, _layStake.value!! )
                        _profitBackWins.value = profitBackWinsQual(newBackStake,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!) + profitBackWinsQual(parBackStake,_backBetOdds.value!!,backCommDecimal,liabilityPar)
                        _profitLayWins.value = profitLayWinsQual(_layStake.value!!, newBackStake, layCommPar) + profitLayWinsQual(layStakePar, parBackStake, layCommPar)

                    }

                    _layStake.value = Math.round(_layStake.value!! * 100) / 100.0
                    _layLiability.value = Math.round(_layLiability.value!! * 100) / 100.0
                    _profitBackWins.value = Math.round(_profitBackWins.value!! * 100) / 100.0
                    _profitLayWins.value = Math.round(_profitLayWins.value!! * 100) / 100.0

                }

                "SNR" -> {

                    when (_resultType.value) {

                        "Normal" ->
                            _layStake.value = layStakeSNRNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )
                        "U-lay" ->
                            _layStake.value = layStakeSNRUlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )
                        "O-lay" ->
                            _layStake.value = layStakeSNROlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )
                    }

                    _layLiability.value = layLiability(_layBetOdds.value!!,_layStake.value!!)
                    _profitBackWins.value = profitBackWinsSNR(_backBetStake.value!!,_backBetOdds.value!!, backCommDecimal, _layLiability.value!!)
                    _profitLayWins.value = profitLayWinsSNR(_layStake.value!!,layCommDecimal)


                    if(isPartial()) {
                        val ratio = layStakePar / _layStake.value!!
                        val newBackStake = _backBetStake.value!! * (1 - ratio)
                        val parBackStake = _backBetStake.value!! * (ratio)
                        when (_resultType.value) {

                            "Normal" ->
                                _layStake.value = layStakeSNRNor(
                                    newBackStake,
                                    _backBetOdds.value!!,
                                    backCommDecimal,
                                    _layBetOdds.value!!,
                                    layCommPar
                                )
                            "U-lay" ->
                                _layStake.value = layStakeSNRUlay(
                                    newBackStake,
                                    _backBetOdds.value!!,
                                    backCommDecimal,
                                    _layBetOdds.value!!,
                                    layCommPar
                                )
                            "O-lay" ->
                                _layStake.value = layStakeSNROlay(
                                    newBackStake,
                                    _backBetOdds.value!!,
                                    backCommDecimal,
                                    _layBetOdds.value!!,
                                    layCommPar
                                )
//
                        }

                        _layLiability.value = layLiability(_layBetOdds.value!!, _layStake.value!!)
                        _profitBackWins.value =
                            profitBackWinsSNR(
                                newBackStake,
                                _backBetOdds.value!!,
                                backCommDecimal,
                                _layLiability.value!!
                            ) + profitBackWinsSNR(
                                parBackStake,
                                _backBetOdds.value!!,
                                backCommDecimal,
                                liabilityPar
                            )
                        _profitLayWins.value =
                            profitLayWinsSNR(_layStake.value!!, layCommPar) + profitLayWinsSNR(
                                layStakePar,
                                layCommPar
                            )
                    }


                        _layStake.value = Math.round(_layStake.value!! * 100) / 100.0
                    _layLiability.value = Math.round(_layLiability.value!! * 100) / 100.0
                    _profitBackWins.value = Math.round(_profitBackWins.value!! * 100) / 100.0
                    _profitLayWins.value = Math.round(_profitLayWins.value!! * 100) / 100.0


                }

                "SR" -> {

                    when (_resultType.value) {

                        "Normal" ->
                            _layStake.value = layStakeQualNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                        "U-lay" ->
                            _layStake.value = layStakeQualUlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                        "O-lay" ->
                            _layStake.value = layStakeQualOlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )
                    }

                    _layLiability.value = layLiability(layOdds,_layStake.value!!)
                    _profitBackWins.value = profitBackWinsSR(_backBetStake.value!!,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!)
                    _profitLayWins.value = profitLayWinsSR(_layStake.value!!,layComm)

                    if(isPartial()){
                        val ratio = layStakePar / _layStake.value!!
                        val newBackStake = _backBetStake.value!! * (1-ratio)
                        val parBackStake = _backBetStake.value!! * (ratio)
                        when (_resultType.value) {

                            "Normal" ->
                                _layStake.value = layStakeQualNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommPar )
                            "U-lay" ->
                                _layStake.value = layStakeQualUlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommPar )
                            "O-lay" ->
                                _layStake.value = layStakeQualOlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommPar )
//
                        }

                        _layLiability.value = layLiability(_layBetOdds.value!!, _layStake.value!! )
                        _profitBackWins.value = profitBackWinsSR(newBackStake,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!) + profitBackWinsSR(parBackStake,_backBetOdds.value!!,backCommDecimal,liabilityPar)
                        _profitLayWins.value = profitLayWinsSR(_layStake.value!!, layCommPar) + profitLayWinsSR(layStakePar, layCommPar)

                    }


                    _layStake.value = Math.round(_layStake.value!! * 100) / 100.0
                    _layLiability.value = Math.round(_layLiability.value!! * 100) / 100.0
                    _profitBackWins.value = Math.round(_profitBackWins.value!! * 100) / 100.0
                    _profitLayWins.value = Math.round(_profitLayWins.value!! * 100) / 100.0

                }
            }
        }
        else{
            _layStake.value = 0.0
            _layLiability.value = 0.0
            _profitBackWins.value = 0.0
            _profitLayWins.value = 0.0
        }
    }

    //sets bet info details from fragment
    fun setBetInfoDetails(
        event: String,
        outc: String,
        date: String,
        bookie: String,
        exchange: String
    ) {
        _event.value = event
        _outcome.value = outc
        _eventTime.value = date
        _bookieName.value = bookie
        _exchangeName.value = exchange
    }


    //sets bet type from radio group
    fun setBetType() {
        when (_radioInputChecked.value) {
            R.id.qualifier -> _betType.value = "Qualifier"
            R.id.snr -> _betType.value = "SNR"
            R.id.sr -> _betType.value = "SR"
        }
    }

    //sets bet type from radio group
    fun setResultType() {
        when (_radioResultChecked.value) {
            R.id.normal -> _resultType.value = "Normal"
            R.id.underlay -> _resultType.value = "U-lay"
            R.id.overlay -> _resultType.value = "O-lay"
        }
    }


    //saves bet to database with coroutine
    fun saveBet() {
        val rating = _backBetOdds.value!! / _layBetOdds.value!!
        setBetType()
        setResultType()
        viewModelScope.launch {
            repository.saveBet(
                MatchedBetDTO(
                    _backBetStake.value,
                    _backBetOdds.value,
                    _layBetOdds.value,
                    _exchangeCommission.value,
                    _backCommission.value,
                    _betType.value,
                    _resultType.value!!,
                    _bookieName.value,
                    _exchangeName.value,
                    _event.value,
                    _eventTime.value,
                    _outcome.value,
                    _profitBackWins.value,
                    rating,
                    true
                )
            )
        }
    }

    private val TAG = "CalculatorViewModel"
}