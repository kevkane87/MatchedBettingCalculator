package com.kevkane87.matchedbettingcalculator.normalcalculator

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevkane87.matchedbettingcalculator.*
import com.kevkane87.matchedbettingcalculator.database.BetDatabase.Companion.getDatabase
import com.kevkane87.matchedbettingcalculator.MatchedBetDTO
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NormalCalculatorViewModel(application: Application) : ViewModel() {

    private val repository = Repository(getDatabase(application))

    //Live data properties
    private val _backBetStake = MutableLiveData<Double>()
    val backBetStake: MutableLiveData<Double>
        get() = _backBetStake

    private val _backBetOdds = MutableLiveData<Double>()
    val backBetOdds: MutableLiveData<Double>
        get() = _backBetOdds

    private val _layBetOdds = MutableLiveData<Double>()
    val layBetOdds: MutableLiveData<Double>
        get() = _layBetOdds

    private val _exchangeCommission = MutableLiveData<Double>()
    val exchangeCommission: MutableLiveData<Double>
        get() = _exchangeCommission

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

    private val _layStakeCustom = MutableLiveData<Double>()
    val layStakeCustom: MutableLiveData<Double>
        get() = _layStakeCustom

    private val _layLiability = MutableLiveData<Double>()
    val layLiability: MutableLiveData<Double>
        get() = _layLiability

    private val _profitBackWins = MutableLiveData<Double>()
    val profitBackWins: MutableLiveData<Double>
        get() = _profitBackWins

    private val _profitLayWins = MutableLiveData<Double>()
    val profitLayWins: MutableLiveData<Double>
        get() = _profitLayWins

    private val _layOddsResults = MutableLiveData<Double>()
    val layOddsResults: MutableLiveData<Double>
        get() = _layOddsResults

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

    private val _customMax = MutableLiveData<Int>()
    val customMax: MutableLiveData<Int>
        get() = _customMax

    private val _customMin = MutableLiveData<Int>()
    val customMin: MutableLiveData<Int>
        get() = _customMin

    private val _setCustomMaxMin = MutableLiveData<Boolean>()
    val setCustomMaxMin : MutableLiveData<Boolean>
        get() = _setCustomMaxMin

    private val _betDetails = MutableLiveData<String>()

    private val _betName = MutableLiveData<String>()
    val betName: MutableLiveData<String>
        get() = _betName

    private val _id = MutableLiveData<String>()
    val id: MutableLiveData<String>
        get() = _id

    var defaultLayCommission: Double = 0.0

    private val _currencySymbol = MutableLiveData<String>()
    val currencySymbol: MutableLiveData<String>
        get() = _currencySymbol

    val backCommCheckboxSate = MutableLiveData<Boolean>()
    val parLaySwitchState = MutableLiveData<Boolean>()
    val parLay2Visibility = MutableLiveData<Boolean>()


    init {
        backCommCheckboxSate.value = false
        parLaySwitchState.value = false
        parLay2Visibility.value = false
        _setCustomMaxMin.value = false
        _customMax.value = 0
        _customMin.value = 0
        _betType.value = ""
        _resultType.value = ""
        setRadioButton()
    }

    fun clear() {
        _backBetStake.value = 0.0
        _backBetOdds.value = 0.0
        _layBetOdds.value = 0.0
        _exchangeCommission.value = defaultLayCommission
        _backCommission.value = 0.0
        _parLayStake1.value = 0.0
        _parLayStake2.value = 0.0
        _parLayOdds1.value = 0.0
        _parLayOdds2.value = 0.0
        _parLayComm1.value = defaultLayCommission
        _parLayComm2.value = defaultLayCommission
        _layStake.value = 0.0
        _layStakeCustom.value = 0.0
        _layLiability.value = 0.0
        _layOddsResults.value = 0.0
        _profitBackWins.value = 0.0
        _profitLayWins.value = 0.0
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
            "Custom" ->
                _radioResultChecked.postValue(R.id.custom)
        }
    }

    private fun isPartial():Boolean{
        return (_parLayStake1.value != null && _parLayOdds1.value != null && _parLayStake1.value != 0.0 && _parLayOdds1.value!! > 1.0)
    }
    private fun isPartial2():Boolean{
        return (_parLayStake2.value != null && _parLayOdds2.value != null && _parLayStake2.value != 0.0 && _parLayOdds2.value!! > 1.0)
    }

    private fun layStakePar():Double{
        return if(isPartial()&&isPartial2()) {
            _parLayStake1.value!! + _parLayStake2.value!!
        } else
            _parLayStake1.value!!
    }

    private fun layOddsPar():Double{
        return if(isPartial()&&isPartial2()) {
            (_parLayOdds1.value!! * _parLayStake1.value!! + _parLayOdds2.value!! * _parLayStake2.value!!) / ( _parLayStake1.value!! + _parLayStake2.value!!)
        } else
            _parLayOdds1.value!!
    }

    private fun layCommPar():Double{
        return if(isPartial()&&isPartial2()) {
           // (_parLayComm1.value!! +  (1-_parLayStake1.value!!/_parLayStake2.value!!) * (_parLayComm2.value!!-_parLayComm1.value!!))/100
            (_parLayComm1.value!! * _parLayStake1.value!! + _parLayComm2.value!! * _parLayStake2.value!!) / ( _parLayStake1.value!! + _parLayStake2.value!!)/100
        } else
            _parLayComm1.value!!/100
    }

    fun canCalculate():Boolean{

        return _backBetOdds.value != null && _backBetStake.value != null && _layBetOdds.value != null && _backBetOdds.value!! > 1.0 && _backBetStake.value != 0.0 && _layBetOdds.value!! > 1.0
    }

    //function provides matched bet calculations
    fun calculate() {

        if (canCalculate()) {

            val backCommDecimal = backCommission.value!! / 100
            val layCommDecimal = exchangeCommission.value!! / 100

            val layStakePar = layStakePar()
            val layOddsPar = layOddsPar()
            val layCommPar = layCommPar()
            val liabilityPar = layStakePar * layOddsPar - layStakePar

            val layOdds = if(isPartial()) layOddsPar  else _layBetOdds.value!!
            val layComm =  if(isPartial()) layCommPar  else layCommDecimal

            when (_betType.value) {

                "Qualifier" -> {

                    when (_resultType.value) {

                        "Normal" ->
                            _layStake.value = layStakeQualNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                        "U-lay" ->
                            _layStake.value = layStakeQualUlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)

                        "O-lay" ->
                            _layStake.value = layStakeQualOlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                        "Custom" -> {

                            _layStake.value = _layStakeCustom.value!!

                                if (_setCustomMaxMin.value!!) {
                                    _layStake.value = layStakeQualNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                                    _customMax.value = (_layStake.value!! * 110).toInt()

                                    _customMin.value = (_layStake.value!! * 90).toInt()

                                    if (!isPartial())
                                    _setCustomMaxMin.value = false
                                }

                        }

                    }

                    _layLiability.value = layLiability(layOdds,_layStake.value!!)
                    _profitBackWins.value = profitBackWinsQual(_backBetStake.value!!,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!)
                    _profitLayWins.value = profitLayWinsQual(_layStake.value!!,_backBetStake.value!!,layComm)


                    if(isPartial()){
                        val ratio = layStakePar / _layStake.value!!
                        val newBackStake = _backBetStake.value!! * (1-ratio)
                        val parBackStake = _backBetStake.value!! * (ratio)
                        when (_resultType.value) {

                            "Normal" ->
                                _layStake.value = layStakeQualNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )
                            "U-lay" ->
                                _layStake.value = layStakeQualUlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )
                            "O-lay" ->
                                _layStake.value = layStakeQualOlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )

                            "Custom" -> {
                                _layStake.value = _layStakeCustom.value!!

                                if (_setCustomMaxMin.value!!) {
                                    _layStake.value = layStakeQualNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )

                                    _customMax.value = (_layStake.value!! * 110).toInt()


                                    _customMin.value = (_layStake.value!! * 90).toInt()


                                    _setCustomMaxMin.value = false
                                }
                            }
                        }

                        _layLiability.value = layLiability(_layBetOdds.value!!, _layStake.value!! )
                        _profitBackWins.value = profitBackWinsQual(newBackStake,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!) + profitBackWinsQual(parBackStake,_backBetOdds.value!!,backCommDecimal,liabilityPar)
                        _profitLayWins.value = profitLayWinsQual(_layStake.value!!, newBackStake, layCommDecimal) + profitLayWinsQual(layStakePar, parBackStake, layCommPar)

                    }

                }

                "SNR" -> {

                    when (_resultType.value) {

                        "Normal" ->
                            _layStake.value = layStakeSNRNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)
                        "U-lay" ->
                            _layStake.value = layStakeSNRUlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)
                        "O-lay" ->
                            _layStake.value = layStakeSNROlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)
                        "Custom" -> {
                            _layStake.value = _layStakeCustom.value!!

                            if (_setCustomMaxMin.value!!) {
                                _layStake.value = layStakeSNRNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                                _customMax.value = (_layStake.value!! * 110).toInt()


                                _customMin.value = (_layStake.value!! * 90).toInt()


                                if (!isPartial())
                                    _setCustomMaxMin.value = false
                            }
                        }
                    }

                    _layLiability.value = layLiability(_layBetOdds.value!!,_layStake.value!!)
                    _profitBackWins.value = profitBackWinsSNR(_backBetStake.value!!,_backBetOdds.value!!, backCommDecimal, _layLiability.value!!)
                    _profitLayWins.value = profitLayWinsSNR(_layStake.value!!,layComm)


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
                                    layCommDecimal
                                )
                            "U-lay" ->
                                _layStake.value = layStakeSNRUlay(
                                    newBackStake,
                                    _backBetOdds.value!!,
                                    backCommDecimal,
                                    _layBetOdds.value!!,
                                    layCommDecimal
                                )
                            "O-lay" ->
                                _layStake.value = layStakeSNROlay(
                                    newBackStake,
                                    _backBetOdds.value!!,
                                    backCommDecimal,
                                    _layBetOdds.value!!,
                                    layCommDecimal
                                )

                            "Custom" -> {
                                _layStake.value = _layStakeCustom.value!!

                                if (_setCustomMaxMin.value!!) {
                                    _layStake.value = layStakeSNRNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )

                                    _customMax.value = (_layStake.value!! * 110).toInt()


                                    _customMin.value = (_layStake.value!! * 90).toInt()


                                    _setCustomMaxMin.value = false
                                }
                            }

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
                            profitLayWinsSNR(_layStake.value!!, layCommDecimal) + profitLayWinsSNR(
                                layStakePar,
                                layCommPar
                            )
                    }

                }

                "SR" -> {

                    when (_resultType.value) {

                        "Normal" ->
                            _layStake.value = layStakeQualNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)

                        "U-lay" ->
                            _layStake.value = layStakeQualUlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)

                        "O-lay" ->
                            _layStake.value = layStakeQualOlay(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm)

                        "Custom" -> {
                            _layStake.value = _layStakeCustom.value!!

                            if (_setCustomMaxMin.value!!) {
                                _layStake.value = layStakeQualNor(_backBetStake.value!!, _backBetOdds.value!!, backCommDecimal, layOdds, layComm )

                                _customMax.value = (_layStake.value!! * 110).toInt()


                                _customMin.value = (_layStake.value!! * 90).toInt()


                                if (!isPartial())
                                    _setCustomMaxMin.value = false
                            }
                        }
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
                                _layStake.value = layStakeQualNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )
                            "U-lay" ->
                                _layStake.value = layStakeQualUlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )
                            "O-lay" ->
                                _layStake.value = layStakeQualOlay(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )
                            "Custom" -> {
                                _layStake.value = _layStakeCustom.value!!

                                if (_setCustomMaxMin.value!!) {
                                    _layStake.value = layStakeQualNor(newBackStake, _backBetOdds.value!!, backCommDecimal, _layBetOdds.value!!, layCommDecimal )

                                    _customMax.value = (_layStake.value!! * 110).toInt()


                                    _customMin.value = (_layStake.value!! * 90).toInt()


                                    _setCustomMaxMin.value = false
                                }
                            }
                        }

                        _layLiability.value = layLiability(_layBetOdds.value!!, _layStake.value!! )
                        _profitBackWins.value = profitBackWinsSR(newBackStake,_backBetOdds.value!!,backCommDecimal,_layLiability.value!!) + profitBackWinsSR(parBackStake,_backBetOdds.value!!,backCommDecimal,liabilityPar)
                        _profitLayWins.value = profitLayWinsSR(_layStake.value!!, layCommDecimal) + profitLayWinsSR(layStakePar, layCommPar)

                    }
                }
            }
        }
        else{
            _layStake.value = 0.0
            _layLiability.value = 0.0
            _layOddsResults.value = 0.0
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

    //sets bet type from radio group
    fun setResultType() {
        when (_radioResultChecked.value) {
            R.id.normal -> _resultType.value = "Normal"
            R.id.underlay -> _resultType.value = "U-lay"
            R.id.overlay -> _resultType.value = "O-lay"
            R.id.custom -> _resultType.value = "Custom"
        }
    }

    fun setBetDetails(){

        val builder = StringBuilder()
        val df = DecimalFormat("#.######")
        val cf = NumberFormat.getCurrencyInstance(Locale.UK)

        builder.append("Matched bet type: " + _betType.value + "\n")

        builder.append("Back stake: " + cf.format(_backBetStake.value))
        builder.append(", Back odds: " + df.format(_backBetOdds.value))
        if(_backCommission.value!! > 0.0)builder.append(", Back comm : " + df.format(_backCommission.value) + "%\n")else builder.append("\n")

        builder.append("Lay odds: " + df.format(_layBetOdds.value))
        builder.append(", Lay comm: " + df.format(_exchangeCommission.value) + "%\n")

        if (isPartial()){
            builder.append("Part lay stake: " + cf.format(_parLayStake1.value))
            builder.append(", Part lay odds: " + df.format(_parLayOdds1.value))
            builder.append(", Part lay comm: " + df.format(_parLayComm1.value) + "%\n")

            if (isPartial2()){
                builder.append("Part lay stake: " + cf.format(_parLayStake2.value))
                builder.append(", Part lay odds: " + df.format(_parLayOdds2.value))
                builder.append(", Part lay comm: " + df.format(_parLayComm2.value) + "%\n")
            }
        }

        builder.append("Lay stake: " + cf.format(_layStake.value))
        builder.append(", Lay liab: " + cf.format(_layLiability.value) + "\n")

        builder.append("Profit: " + cf.format(_profitBackWins.value) + " (Back wins) " + cf.format(_profitLayWins.value) + " (Lay wins)")


        _betDetails.value = builder.toString()
    }

    private fun getDate(): String{
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val currentDateTime = LocalDate.now()
        return currentDateTime.format(formatter)
    }

    //saves bet to database with coroutine
    fun saveBet() {
        val dateToday = getDate()
        viewModelScope.launch {
            repository.saveBet(
                MatchedBetDTO(dateToday, _betName.value!!, "Matched Bet", _betDetails.value!!)
            )
        }
    }
}

