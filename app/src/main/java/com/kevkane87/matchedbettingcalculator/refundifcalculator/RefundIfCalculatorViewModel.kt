package com.kevkane87.matchedbettingcalculator.refundifcalculator

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

class RefundIfCalculatorViewModel(application: Application) : ViewModel() {

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

    private val _refund = MutableLiveData<Double>()
    val refund: MutableLiveData<Double>
        get() = _refund

    private val _refundRetention = MutableLiveData<Double>()
    val refundRetention: MutableLiveData<Double>
        get() = _refundRetention

    private val _layOddsRefund = MutableLiveData<Double>()
    val layOddsRefund: MutableLiveData<Double>
        get() = _layOddsRefund

    private val _refundLayComm = MutableLiveData<Double>()
    val refundLayComm: MutableLiveData<Double>
        get() = _refundLayComm

    private val _backCommission = MutableLiveData<Double>()
    val backCommission: MutableLiveData<Double>
        get() = _backCommission

    private val _layStake = MutableLiveData<Double>()
    val layStake: MutableLiveData<Double>
        get() = _layStake

    private val _layLiability = MutableLiveData<Double>()
    val layLiability: MutableLiveData<Double>
        get() = _layLiability

    private val _layStakeRefund = MutableLiveData<Double>()
    val layStakeRefund: MutableLiveData<Double>
        get() = _layStakeRefund

    private val _layLiabilityRefund = MutableLiveData<Double>()
    val layLiabilityRefund: MutableLiveData<Double>
        get() = _layLiabilityRefund

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


    init {
        backCommCheckboxSate.value = false
        _betType.value = ""

    }

    fun clear() {
        _backBetStake.value = 0.0
        _backBetOdds.value = 0.0
        _layBetOdds.value = 0.0
        _refund.value = 0.0
        _refundRetention.value = 0.0
        _exchangeCommission.value = defaultLayCommission
        _refundLayComm.value = defaultLayCommission
        _backCommission.value = 0.0
        _layStake.value = 0.0
        _layLiability.value = 0.0
        _layOddsResults.value = 0.0
        _profitBackWins.value = 0.0
        _profitLayWins.value = 0.0
        _id.value = ""

    }

    //sets radio button for correct bet type


    fun canCalculate(): Boolean {

        return _backBetOdds.value != null && _backBetStake.value != null && _layBetOdds.value != null && _layOddsRefund.value != null && _refund.value != null && _refundRetention.value != null && _backBetOdds.value!! > 1.0 && _backBetStake.value != 0.0 && _layBetOdds.value!! > 1.0 && _layOddsRefund.value!! > 1.0  && _refund.value != 0.0 && _refundRetention.value != 0.0
    }

    //function provides matched bet calculations
    fun calculate() {

        if (canCalculate()) {

            val backCommDecimal = _backCommission.value!! / 100
            val layCommDecimal = _exchangeCommission.value!! / 100
            val layCommRefundDecimal = _refundLayComm.value!! / 100
            val refundAmount = _refund.value!! * (_refundRetention.value!! / 100)


            val layOdds =  _layBetOdds.value!!

            _layStake.value = layStakeQualNor(
                _backBetStake.value!!,
                _backBetOdds.value!!,
                backCommDecimal,
                layOdds,
                layCommDecimal
            )

            _layLiability.value = layLiability(layOdds, _layStake.value!!)
            _profitBackWins.value = profitBackWinsQual(
                _backBetStake.value!!,
                _backBetOdds.value!!,
                backCommDecimal,
                _layLiability.value!!
            )

            _profitLayWins.value =
                profitLayWinsQual(_layStake.value!!, _backBetStake.value!!, layCommDecimal)


            _layStakeRefund.value = refundAmount  / (_layOddsRefund.value!! * (1-layCommRefundDecimal))

            _layLiabilityRefund.value = layLiability(_layOddsRefund.value!!, _layStakeRefund.value!!)

            _profitBackWins.value = _profitBackWins.value!! + _layStakeRefund.value!!
            _profitLayWins.value = _profitLayWins.value!! + _layStakeRefund.value!!


        } else {
            _layStake.value = 0.0
            _layLiability.value = 0.0
            _layOddsResults.value = 0.0
            _layStakeRefund.value = 0.0
            _layLiabilityRefund.value = 0.0
            _layOddsRefund.value = 0.0
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

        builder.append("Refund amount: " + cf.format(_refund.value))
        builder.append(", Refund retention: " + df.format(_refundRetention.value) + "%\n")

        builder.append("Lay odds init: " + df.format(_layBetOdds.value))
        builder.append(", Lay comm init: " + df.format(_exchangeCommission.value) + "%\n")

        builder.append("Lay odds refund trig: " + df.format(_layOddsRefund.value))
        builder.append(", Lay comm refund trig: " + df.format(_refundLayComm.value) + "%\n")

        builder.append("Lay stake init: " + cf.format(_layStake.value))
        builder.append(", Lay liab init: " + cf.format(_layLiability.value) + "\n")

        builder.append("Lay stake trig: " + cf.format(_layStakeRefund.value))
        builder.append(", Lay liab trig: " + cf.format(_layLiabilityRefund.value) + "\n")

        builder.append("Profit: " + cf.format(_profitBackWins.value))

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
                MatchedBetDTO(dateToday, _betName.value!!, "Refund If", _betDetails.value!!)
            )
        }
    }
}

