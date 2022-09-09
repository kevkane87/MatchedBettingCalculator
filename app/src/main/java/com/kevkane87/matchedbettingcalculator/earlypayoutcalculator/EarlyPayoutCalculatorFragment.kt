package com.kevkane87.matchedbettingcalculator.earlypayoutcalculator

import android.content.*
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.kevkane87.matchedbettingcalculator.R
import com.kevkane87.matchedbettingcalculator.databinding.FragmentEarlyPayoutCalculatorBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EarlyPayoutCalculatorFragment : Fragment() {

    private lateinit var layCommissionDefault: String
    private lateinit var currency: String

    private val viewModel: EarlyPayoutCalculatorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            EarlyPayoutCalculatorViewModelFactory(activity.application)
        )[EarlyPayoutCalculatorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentEarlyPayoutCalculatorBinding>(
            inflater,
            R.layout.fragment_early_payout_calculator, container, false
        )


        binding.earlyPayoutCalculatorViewModel = viewModel

        setDefaults(binding)

        viewModel.clear()

        var isFineTune = false

        if (viewModel.canCalculate()) binding.layoutResults.isVisible = true
        else binding.layoutResults.isGone = true

        binding.lifecycleOwner = this

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.early_payout_calculator)


        binding.groupBackBetCommission.isVisible = viewModel.backCommCheckboxSate.value!!
        binding.groupMaxPayout.isVisible = viewModel.maxPayoutCheckboxSate.value!!

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

        binding.checkBoxMaxPayout.setOnClickListener {
            if (binding.checkBoxMaxPayout.isChecked) {
                viewModel.maxPayoutCheckboxSate.value = true
                binding.groupMaxPayout.isVisible = true
            } else {
                viewModel.maxPayoutCheckboxSate.value = false
                binding.groupMaxPayout.isVisible = false
                binding.maxPayout.text = Editable.Factory.getInstance().newEditable("")
                viewModel.maxPayout.value = 0.0
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
            Toast.makeText(context, "$stake" + " " + getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
                .show()
        }

        binding.buttonCopyInPlayBack.setOnClickListener {
            var stake = binding.inPlayBackStake.text
            stake = stake.drop(1)
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("copy_back_stake", stake)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "$stake" + " " + getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
                .show()
        }


        //listeners to calculate for any user input changes

        binding.backBetStake.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }
        binding.backBetOdds.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }
        binding.exLayBetOdds.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }
        binding.exCommission.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }
        binding.backBetCommission.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }
        binding.inPlayBackOdds.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }
        binding.maxPayout.doAfterTextChanged {
            viewModel.isCustomStake.value = false
            calculate(binding)
        }

        binding.customLayStakeEarly.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                if (fromUser || isFineTune) {
                    viewModel.layStakeCustom.value = progress.toDouble() / 100
                    calculate(binding)
                } else {
                    calculate(binding)
                    binding.customLayStakeEarly.setProgress(viewModel.seekMax.value!!)
                }

            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })

        binding.fineTuneMinusEarly.setOnClickListener {

            isFineTune = true
            binding.customLayStakeEarly.setProgress( binding.customLayStakeEarly.progress - 1)
            viewModel.layStakeCustom.value = binding.customLayStakeEarly.progress.toDouble() / 100
            calculate(binding)
            isFineTune = false
        }

        binding.fineTunePlusEarly.setOnClickListener {

            isFineTune = true
            binding.customLayStakeEarly.setProgress( binding.customLayStakeEarly.progress + 1)
            viewModel.layStakeCustom.value = binding.customLayStakeEarly.progress.toDouble() / 100
            calculate(binding)
            isFineTune = false
        }

        return binding.root
    }

    //function to trigger calculation
    private fun calculate(binding: FragmentEarlyPayoutCalculatorBinding) {

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

        if (binding.inPlayBackOdds.text.startsWith('.')) binding.inPlayBackOdds.text.insert(0, "0")
        if (binding.inPlayBackOdds.text.isNullOrEmpty()) viewModel.inPlayBackOdds.value = 0.0
        else viewModel.inPlayBackOdds.value = binding.inPlayBackOdds.text.toString().toDouble()

        if (binding.maxPayout.text.startsWith('.')) binding.maxPayout.text.insert(0, "0")
        if (binding.maxPayout.text.isNullOrEmpty()) viewModel.maxPayout.value = 0.0
        else viewModel.maxPayout.value = binding.maxPayout.text.toString().toDouble()


        if (viewModel.canCalculate()) binding.layoutResults.isVisible = true
        else {
            binding.layoutResults.isGone = true
        }

        viewModel.calculate()
    }

    private fun clear(binding: FragmentEarlyPayoutCalculatorBinding) {
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
        binding.inPlayBackOdds.text = Editable.Factory.getInstance().newEditable("")
        binding.maxPayout.text = Editable.Factory.getInstance().newEditable("")

    }


    private fun saveBet() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle(activity?.getString(R.string.set_bet_title))
        val input = EditText(requireContext())
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

    private fun setDefaults(binding: FragmentEarlyPayoutCalculatorBinding) {
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