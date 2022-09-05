package com.example.android.kevkane87.matchedbettingcalculator.refundcalculator

import android.content.*
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentRefundCalculatorBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RefundCalculatorFragment : Fragment() {

    private lateinit var layCommissionDefault: String
    private lateinit var currency: String

    private val viewModel: RefundCalculatorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            RefundCalculatorViewModelFactory(activity.application)
        )[RefundCalculatorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentRefundCalculatorBinding>(
            inflater,
            R.layout.fragment_refund_calculator, container, false
        )


        binding.refundCalculatorViewModel = viewModel

        setDefaults(binding)

        viewModel.clear()

        if (viewModel.canCalculate()) binding.layoutResults.isVisible = true
        else binding.layoutResults.isGone = true

        binding.lifecycleOwner = this

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.refund_calculator)


        binding.groupBackBetCommission.isVisible = viewModel.backCommCheckboxSate.value!!



        binding.checkBoxBackComm.setOnClickListener {
            if (binding.checkBoxBackComm.isChecked) {
                viewModel.backCommCheckboxSate.value = true
                binding.groupBackBetCommission.isVisible = true
            } else {
                viewModel.backCommCheckboxSate.value = false
                binding.groupBackBetCommission.isVisible = false
                binding.backBetCommission.text = Editable.Factory.getInstance().newEditable("")
                viewModel.backCommission.value = 0.0
            }
        }

        //save button
        binding.buttonSave.setOnClickListener {

            if (viewModel.backBetOdds.value != 0.0 && viewModel.backBetOdds.value != 0.0 && viewModel.layBetOdds.value != 0.0) {
                saveBet()
            } else {
                Toast.makeText(context, R.string.please_enter_bet_input_details, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.buttonClear.setOnClickListener {
            clear(binding)
        }

        binding.buttonCopyInitial.setOnClickListener {
            var stake = binding.layStakeInitial.text
            stake = stake.drop(1)
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("copy_lay_stake", stake)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "$stake copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }


        //listeners to calculate for any user input changes

        binding.backBetStake.doAfterTextChanged {
            calculate(binding)
        }
        binding.backBetOdds.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetOdds.doAfterTextChanged {
            calculate(binding)
        }
        binding.exCommission.doAfterTextChanged {
            calculate(binding)
        }
        binding.backBetCommission.doAfterTextChanged {
            calculate(binding)
        }
        binding.refund.doAfterTextChanged {
            calculate(binding)
        }
        binding.refundRetention.doAfterTextChanged {
            calculate(binding)
        }

        return binding.root
    }

    //function to trigger calculation
    private fun calculate(binding: FragmentRefundCalculatorBinding) {

        if (binding.backBetStake.text.startsWith('.')) binding.backBetStake.text.insert(0, "0")
        if (binding.backBetStake.text.isNullOrEmpty()) viewModel.backBetStake.value = 0.0
        else viewModel.backBetStake.value = binding.backBetStake.text.toString().toDouble()

        if (binding.backBetOdds.text.startsWith('.')) binding.backBetOdds.text.insert(0, "0")
        if (binding.backBetOdds.text.isNullOrEmpty()) viewModel.backBetOdds.value = 0.0
        else viewModel.backBetOdds.value = binding.backBetOdds.text.toString().toDouble()

        if (binding.exLayBetOdds.text.startsWith('.')) binding.exLayBetOdds.text.insert(0, "0")
        if (binding.exLayBetOdds.text.isNullOrEmpty()) viewModel.layBetOdds.value = 0.0
        else viewModel.layBetOdds.value = binding.exLayBetOdds.text.toString().toDouble()

        if (binding.exCommission.text.startsWith('.')) binding.exCommission.text.insert(0, "0")
        if (binding.exCommission.text.isNullOrEmpty()) viewModel.exchangeCommission.value = 0.0
        else viewModel.exchangeCommission.value = binding.exCommission.text.toString().toDouble()

        if (binding.backBetCommission.text.startsWith('.')) binding.backBetCommission.text.insert(
            0,
            "0"
        )
        if (binding.backBetCommission.text.isNullOrEmpty()) viewModel.backCommission.value = 0.0
        else viewModel.backCommission.value = binding.backBetCommission.text.toString().toDouble()

        if (binding.refund.text.startsWith('.')) binding.refund.text.insert(0, "0")
        if (binding.refund.text.isNullOrEmpty()) viewModel.refund.value = 0.0
        else viewModel.refund.value = binding.refund.text.toString().toDouble()

        if (binding.refundRetention.text.startsWith('.')) binding.refundRetention.text.insert(0, "0")
        if (binding.refundRetention.text.isNullOrEmpty()) viewModel.refundRetention.value = 0.0
        else viewModel.refundRetention.value = binding.refundRetention.text.toString().toDouble()

        if (viewModel.canCalculate()) binding.layoutResults.isVisible = true
        else {
            binding.layoutResults.isGone = true
        }

        viewModel.calculate()
    }

    private fun clear(binding: FragmentRefundCalculatorBinding) {
        //clear button
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        layCommissionDefault = sharedPreferences?.getString(
            activity?.getString(R.string.key_default_lay_commission),
            ""
        ).toString()

        viewModel.clear()
        binding.backBetStake.text = Editable.Factory.getInstance().newEditable("")
        binding.backBetOdds.text = Editable.Factory.getInstance().newEditable("")
        binding.exCommission.text = Editable.Factory.getInstance().newEditable(layCommissionDefault)
        binding.backBetCommission.text = Editable.Factory.getInstance().newEditable("")
        binding.exLayBetOdds.text = Editable.Factory.getInstance().newEditable("")
        binding.refund.text = Editable.Factory.getInstance().newEditable("")
        binding.refundRetention.text = Editable.Factory.getInstance().newEditable("")


    }


    private fun saveBet() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Set Bet Name")

        val input = EditText(requireContext())
        input.hint = "Enter Bet Name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            // Here you get get input text from the Edittext
            val name = input.text.toString()
            if (name.isEmpty()) viewModel.betName.value = "" else viewModel.betName.value =
                name
            viewModel.setBetDetails()
            viewModel.saveBet()
            Toast.makeText(context, activity?.getString(R.string.bet_saved), Toast.LENGTH_SHORT)
                .show()
        }
        builder.show()
    }

    private fun setDefaults(binding: FragmentRefundCalculatorBinding) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        layCommissionDefault = sharedPreferences?.getString(
            activity?.getString(R.string.key_default_lay_commission),
            ""
        ).toString()
        viewModel.defaultLayCommission = layCommissionDefault.toDouble()

        currency =
            sharedPreferences?.getString(activity?.getString(R.string.key_currency), "").toString()
        viewModel.currencySymbol.value = currency

        binding.exCommission.text = Editable.Factory.getInstance().newEditable(layCommissionDefault)

    }


    override fun onResume() {
        super.onResume()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        currency =
            sharedPreferences?.getString(activity?.getString(R.string.key_currency), "").toString()
        viewModel.currencySymbol.value = currency
        viewModel.calculate()
    }
}