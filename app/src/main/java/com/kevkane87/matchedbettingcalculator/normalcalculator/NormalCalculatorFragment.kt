package com.kevkane87.matchedbettingcalculator.normalcalculator

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
import com.kevkane87.matchedbettingcalculator.databinding.FragmentNormalCalculatorBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NormalCalculatorFragment : Fragment() {

    private lateinit var layCommissionDefault: String
    private lateinit var currency: String

    private val viewModel: NormalCalculatorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            NormalCalculatorViewModelFactory(activity.application)
        )[NormalCalculatorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentNormalCalculatorBinding>(
            inflater,
            R.layout.fragment_normal_calculator, container, false
        )

        var isFineTune = false

        binding.normalCalculatorViewModel = viewModel

        setDefaults(binding)

        viewModel.clear()
        viewModel.setRadioButton()

        if (viewModel.canCalculate()) binding.layoutResults.isVisible = true
        else binding.layoutResults.isGone = true

        binding.lifecycleOwner = this

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.app_name)


        binding.groupBackBetCommission.isVisible = viewModel.backCommCheckboxSate.value!!
        binding.partialLayout.isVisible = viewModel.parLaySwitchState.value!!

        if (viewModel.parLay2Visibility.value!!) {
            binding.groupPartLay2.isVisible = true
            binding.addPartLay2.isGone = true
        } else {
            binding.groupPartLay2.isGone = true
            binding.addPartLay2.isVisible = true
        }


        viewModel.radioResultChecked.observeForever {

            when (viewModel.radioResultChecked.value) {

                R.id.normal -> {

                    binding.groupCustom.isGone = true
                    viewModel.setCustomMaxMin.value = true

                }
                R.id.underlay -> {

                    binding.groupCustom.isGone = true
                    viewModel.setCustomMaxMin.value = true

                }
                R.id.overlay -> {

                    binding.groupCustom.isGone = true
                    viewModel.setCustomMaxMin.value = true

                }
                R.id.custom -> {

                    binding.groupCustom.isVisible = true

                }
            }
        }

        binding.switchPartialLay.setOnClickListener {
            binding.partialLayout.isVisible = binding.switchPartialLay.isChecked

            if (!binding.switchPartialLay.isChecked) {
                viewModel.parLaySwitchState.value = false
                viewModel.parLay2Visibility.value = false
                binding.exLayBetStakePar1.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetStakePar2.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetOddsPar1.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetOddsPar2.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetCommPar1.text =
                    Editable.Factory.getInstance().newEditable(layCommissionDefault)
                binding.exLayBetCommPar2.text =
                    Editable.Factory.getInstance().newEditable(layCommissionDefault)
            } else {
                viewModel.parLaySwitchState.value = true
                viewModel.parLay2Visibility.value = false
                binding.addPartLay2.isVisible = true
                binding.groupPartLay2.isGone = true
            }
        }

        binding.addPartLay2.setOnClickListener {
            viewModel.parLay2Visibility.value = true
            binding.groupPartLay2.isVisible = true
            binding.addPartLay2.isGone = true

        }

        binding.buttonCopy.setOnClickListener {
            var stake = binding.layStake.text
            stake = stake.drop(1)
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("copy_lay_stake", stake)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "$stake copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }


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

        binding.buttonCopy.setOnClickListener {
            var stake = binding.layStake.text
            stake = stake.drop(1)
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("copy_lay_stake", stake)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "$stake" + " " + getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
                .show()
        }

        //listeners to calculate for any user input changes

        binding.qualifier.setOnClickListener {
            calculate(binding)
        }
        binding.snr.setOnClickListener {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.sr.setOnClickListener {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.normal.setOnClickListener {
            calculate(binding)
        }
        binding.overlay.setOnClickListener {
            calculate(binding)
        }
        binding.underlay.setOnClickListener {
            calculate(binding)
        }
        binding.custom.setOnClickListener {
                calculate(binding)
                binding.customMin.setText((viewModel.customMin.value!!.toDouble() / 100.00).toString())
                binding.cusomMax.setText((viewModel.customMax.value!!.toDouble() / 100.00).toString())
                binding.customLayStake.setProgress((viewModel.layStake.value!! * 100).toInt())
                viewModel.setCustomMaxMin.value = false
        }
        binding.backBetStake.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.backBetOdds.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetOdds.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exCommission.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.backBetCommission.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetStakePar1.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetStakePar2.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetOddsPar1.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetOddsPar2.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetCommPar1.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.exLayBetCommPar2.doAfterTextChanged {
            viewModel.radioResultChecked.value = R.id.normal
            viewModel.setResultType()
            calculate(binding)
        }
        binding.customMin.doAfterTextChanged {
            if (!binding.customMin.text.isNullOrEmpty()) {

                viewModel.customMin.value =
                    (binding.customMin.text.toString().toDouble() * 100).toInt()

                if (viewModel.customMax.value!! >= viewModel.customMin.value!!)
                    calculate(binding)
            }
        }
        binding.cusomMax.doAfterTextChanged {

            if (!binding.cusomMax.text.isNullOrEmpty()){

            viewModel.customMax.value = (binding.cusomMax.text.toString().toDouble() * 100).toInt()

            if (viewModel.customMax.value!! >= viewModel.customMin.value!!)
            calculate(binding)
            }
        }

        binding.customLayStake.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                if (fromUser || isFineTune) {
                    viewModel.layStakeCustom.value = progress.toDouble() / 100
                    calculate(binding)
                } else {
                    calculate(binding)
                    binding.customLayStake.setProgress((viewModel.customMin.value!! + viewModel.customMax.value!!) / 2)
                }

            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })

        binding.fineTuneMinus.setOnClickListener {

            isFineTune = true
            binding.customLayStake.setProgress( binding.customLayStake.progress - 1)
            viewModel.layStakeCustom.value = binding.customLayStake.progress.toDouble() / 100
            calculate(binding)
            isFineTune = false
        }

        binding.fineTunePlus.setOnClickListener {

            isFineTune = true
            binding.customLayStake.setProgress( binding.customLayStake.progress + 1)
            viewModel.layStakeCustom.value = binding.customLayStake.progress.toDouble() / 100
            calculate(binding)
            isFineTune = false
        }

        return binding.root
    }

    //function to trigger calculation
    private fun calculate(binding: FragmentNormalCalculatorBinding) {

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

        if (binding.exLayBetStakePar1.text.startsWith('.')) binding.exLayBetStakePar1.text.insert(
            0,
            "0"
        )
        if (binding.exLayBetStakePar1.text.isNullOrEmpty()) viewModel.parLayStake1.value = 0.0
        else viewModel.parLayStake1.value = binding.exLayBetStakePar1.text.toString().toDouble()

        if (binding.exLayBetStakePar2.text.startsWith('.')) binding.exLayBetStakePar2.text.insert(
            0,
            "0"
        )
        if (binding.exLayBetStakePar2.text.isNullOrEmpty()) viewModel.parLayStake2.value = 0.0
        else viewModel.parLayStake2.value = binding.exLayBetStakePar2.text.toString().toDouble()

        if (binding.exLayBetOddsPar1.text.startsWith('.')) binding.exLayBetOddsPar1.text.insert(
            0,
            "0"
        )
        if (binding.exLayBetOddsPar1.text.isNullOrEmpty()) viewModel.parLayOdds1.value = 0.0
        else viewModel.parLayOdds1.value = binding.exLayBetOddsPar1.text.toString().toDouble()

        if (binding.exLayBetOddsPar2.text.startsWith('.')) binding.exLayBetOddsPar2.text.insert(
            0,
            "0"
        )
        if (binding.exLayBetOddsPar2.text.isNullOrEmpty()) viewModel.parLayOdds2.value = 0.0
        else viewModel.parLayOdds2.value = binding.exLayBetOddsPar2.text.toString().toDouble()

        if (binding.exLayBetCommPar1.text.startsWith('.')) binding.exLayBetCommPar1.text.insert(
            0,
            "0"
        )
        if (binding.exLayBetCommPar1.text.isNullOrEmpty()) viewModel.parLayComm1.value = 0.0
        else viewModel.parLayComm1.value = binding.exLayBetCommPar1.text.toString().toDouble()

        if (binding.exLayBetCommPar2.text.startsWith('.')) binding.exLayBetCommPar2.text.insert(
            0,
            "0"
        )
        if (binding.exLayBetCommPar2.text.isNullOrEmpty()) viewModel.parLayComm2.value = 0.0
        else viewModel.parLayComm2.value = binding.exLayBetCommPar2.text.toString().toDouble()

        viewModel.layStakeCustom.value = (binding.customLayStake.progress).toDouble() / 100.00

        if (viewModel.canCalculate()) binding.layoutResults.isVisible = true
        else {
            binding.layoutResults.isGone = true
        }

        viewModel.setBetType()
        viewModel.setResultType()
        viewModel.calculate()

        if (viewModel.layStake.value!! < 0.0){
            binding.layoutResults.isGone = true
        }
    }

    private fun clear(binding: FragmentNormalCalculatorBinding) {
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
        binding.exLayBetStakePar1.text = Editable.Factory.getInstance().newEditable("")
        binding.exLayBetStakePar2.text = Editable.Factory.getInstance().newEditable("")
        binding.exLayBetOddsPar1.text = Editable.Factory.getInstance().newEditable("")
        binding.exLayBetOddsPar2.text = Editable.Factory.getInstance().newEditable("")
        binding.exLayBetCommPar1.text =
            Editable.Factory.getInstance().newEditable(layCommissionDefault)
        binding.exLayBetCommPar2.text =
            Editable.Factory.getInstance().newEditable(layCommissionDefault)
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
            viewModel.setBetDetails(currency)
            viewModel.saveBet()
            Toast.makeText(context, activity?.getString(R.string.bet_saved), Toast.LENGTH_SHORT)
                .show()
        }
        builder.show()
    }

    private fun setDefaults(binding: FragmentNormalCalculatorBinding) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        layCommissionDefault = sharedPreferences?.getString(
            activity?.getString(R.string.key_default_lay_commission),
            ""
        ).toString()
        viewModel.defaultLayCommission = layCommissionDefault.toDouble()

        currency =
            sharedPreferences?.getString(activity?.getString(R.string.key_currency), "").toString()
        viewModel.currencySymbol.value = currency

        binding.exLayBetCommPar1.text =
            Editable.Factory.getInstance().newEditable(layCommissionDefault)
        binding.exLayBetCommPar2.text =
            Editable.Factory.getInstance().newEditable(layCommissionDefault)
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