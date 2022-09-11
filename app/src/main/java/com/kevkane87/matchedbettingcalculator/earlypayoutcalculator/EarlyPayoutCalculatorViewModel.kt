package com.kevkane87.matchedbettingcalculator.earlypayoutcalculator

import android.app.Application
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

class EarlyPayoutCalculatorViewModel(application: Application) : ViewModel() {

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

    private val _inPlayBackOdds = MutableLiveData<Double>()
    val inPlayBackOdds: MutableLiveData<Double>
        get() = _inPlayBackOdds

    private val _backCommission = MutableLiveData<Double>()
    val backCommission: MutableLiveData<Double>
        get() = _backCommission

    private val _maxPayout = MutableLiveData<Double>()
    val maxPayout: MutableLiveData<Double>
        get() = _maxPayout

    private val _layStake = MutableLiveData<Double>()
    val layStake: MutableLiveData<Double>
        get() = _layStake

    private val _inPlayBackStake = MutableLiveData<Double>()
    val inPlayBackStake: MutableLiveData<Double>
        get() = _inPlayBackStake

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

    private val _seekMax = MutableLiveData<Int>()
    val seekMax: MutableLiveData<Int>
        get() = _seekMax

    private val _seekMin = MutableLiveData<Int>()
    val seekMin: MutableLiveData<Int>
        get() = _seekMin

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

    val maxPayoutCheckboxSate = MutableLiveData<Boolean>()

    private val _isCustomStake = MutableLiveData<Boolean>()
    val isCustomStake: MutableLiveData<Boolean>
        get() = _isCustomStake

    private val _layStakeCustom = MutableLiveData<Double>()
    val layStakeCustom: MutableLiveData<Double>
        get() = _layStakeCustom


    init {
        backCommCheckboxSate.value = false
        maxPayoutCheckboxSate.value = false
        _betType.value = ""
        _seekMax.value = 0
        _seekMin.value = 0
        _layStakeCustom.value = 0.0

    }

    fun clear() {
        _backBetStake.value = 0.0
        _backBetOdds.value = 0.0
        _layBetOdds.value = 0.0
        _inPlayBackOdds.value = 0.0
        _exchangeCommission.value = defaultLayCommission
        _backCommission.value = 0.0
        _layStake.value = 0.0
        _layLiability.value = 0.0
        _maxPayout.value = 0.0
        _layOddsResults.value = 0.0
        _profitBackWins.value = 0.0
        _profitLayWins.value = 0.0
        _isCustomStake.value = false
        _id.value = ""

    }

    //sets radio button for correct bet type


    fun canCalculate(): Boolean {

        return _backBetOdds.value != null && _backBetStake.value != null && _layBetOdds.value != null  && _inPlayBackOdds.value != null && _backBetOdds.value!! > 1.0 && _backBetStake.value != 0.0 && _layBetOdds.value!! > 1.0  && _inPlayBackOdds.value!! > 1.0
    }

    //function provides matched bet calculations
    fun calculate() {

        if (canCalculate()) {

            val backCommDecimal = _backCommission.value!! / 100
            val layCommDecimal = _exchangeCommission.value!! / 100
            val layOdds =  _layBetOdds.value!!



            _layStake.value = layStakeQualNor(
                _backBetStake.value!!,
                _backBetOdds.value!!,
                backCommDecimal,
                layOdds,
                layCommDecimal,
            )

            _layLiability.value = layLiability(layOdds, _layStake.value!!)


            if (_isCustomStake.value!!){
                _inPlayBackStake.value = _layStakeCustom.value
            }
            else{
                if (maxPayoutCheckboxSate.value!! && _maxPayout.value!! != 0.0){
                    if ((_backBetOdds.value!! * _backBetStake.value!!) > _maxPayout.value!!){

                        _inPlayBackStake.value = ((_maxPayout.value!!) + layCommDecimal*_layStake.value!!)/ (inPlayBackOdds.value!!)

                    }
                    else{
                        _inPlayBackStake.value = ((_backBetOdds.value!! * _backBetStake.value!!) + layCommDecimal*_layStake.value!!)/ (inPlayBackOdds.value!!)
                    }
                }
                else{
                    _inPlayBackStake.value = ((_backBetOdds.value!! * _backBetStake.value!!) + layCommDecimal*_layStake.value!!)/ (inPlayBackOdds.value!!)
                }


                _seekMin.value = (profitLayWinsQual(_layStake.value!!, _backBetStake.value!!, layCommDecimal)*100).toInt()
                _seekMax.value = (_inPlayBackStake.value!! * 100).toInt()
                _layStakeCustom.value = _seekMax.value!!.toDouble()/100
                _isCustomStake.value = true
            }

            _profitBackWins.value = profitBackWinsQual(
                _backBetStake.value!!,
                _backBetOdds.value!!,
                backCommDecimal,
                _layLiability.value!!
            ) + (_inPlayBackStake.value!! * inPlayBackOdds.value!! - _inPlayBackStake.value!!)


                if (maxPayoutCheckboxSate.value!! && _maxPayout.value!! != 0.0){
                    if ((_backBetOdds.value!! * _backBetStake.value!!) > _maxPayout.value!!){
                        _profitLayWins.value =
                            profitLayWinsQual(_layStake.value!!, _backBetStake.value!!, layCommDecimal) + (_maxPayout.value!!) - _inPlayBackStake.value!!
                    }
                    else{
                        _profitLayWins.value =
                            profitLayWinsQual(_layStake.value!!, _backBetStake.value!!, layCommDecimal) + (_backBetStake.value!! * _backBetOdds.value!!) - _inPlayBackStake.value!!
                    }
                }
                else{
                    _profitLayWins.value =
                        profitLayWinsQual(_layStake.value!!, _backBetStake.value!!, layCommDecimal) + (_backBetStake.value!! * _backBetOdds.value!!) - _inPlayBackStake.value!!
                }


        } else {
            _layStake.value = 0.0
            _layLiability.value = 0.0
            _layOddsResults.value = 0.0
            _profitBackWins.value = 0.0
            _profitLayWins.value = 0.0
        }
    }


    fun setBetDetails(currency: String){

        val cf = NumberFormat.getCurrencyInstance(Locale.UK)

        when(currency){
            "£" -> cf.currency = Currency.getInstance("GBP")
            "€" -> cf.currency = Currency.getInstance("EUR")
            "$" -> cf.currency = Currency.getInstance("USD")
        }

        val builder = StringBuilder()
        val df = DecimalFormat("#.######")


        builder.append("Back stake: " + cf.format(_backBetStake.value))
        builder.append(", Back odds: " + df.format(_backBetOdds.value))
        if (_backCommission.value!! > 0.0) builder.append(
            ", Back comm : " + df.format(
                _backCommission.value
            ) + "%\n"
        ) else builder.append("\n")


        builder.append("Lay odds: " + df.format(_layBetOdds.value))
        builder.append(", Lay comm: " + df.format(_exchangeCommission.value) + "%\n")

        builder.append("In-play back odds: " + df.format(_layBetOdds.value)+"%\n")

        if (maxPayoutCheckboxSate.value!! && _maxPayout.value!! > 0.0){
            builder.append("Max payout: " + cf.format(_maxPayout.value)+"%\n")
        }

        builder.append("Lay stake init: " + cf.format(_layStake.value))
        builder.append(", Lay liab init: " + cf.format(_layLiability.value) + "\n")


        builder.append("In-play back stake: " + cf.format(_layStake.value)+"\n")

        builder.append("Profit: " + cf.format(_profitBackWins.value) + " (Back wins) " + cf.format(_profitLayWins.value) + " (Lay wins)")

        _betDetails.value = builder.toString().replace("US","")
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
                MatchedBetDTO(dateToday, _betName.value!!, "Early Payout", _betDetails.value!!)
            )
        }
    }
}

