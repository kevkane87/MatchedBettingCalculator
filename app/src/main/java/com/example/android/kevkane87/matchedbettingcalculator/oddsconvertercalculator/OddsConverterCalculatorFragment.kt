package com.example.android.kevkane87.matchedbettingcalculator.oddsconvertercalculator

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentOddsConverterCalculatorBinding
import java.text.DecimalFormat


class OddsConverterCalculatorFragment : Fragment() {

    private val viewModel: OddsConverterCalculatorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            OddsConverterCalculatorViewModelFactory(activity.application)
        )[OddsConverterCalculatorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentOddsConverterCalculatorBinding>(
            inflater,
            R.layout.fragment_odds_converter_calculator, container, false
        )

        binding.oddsConverterCalculatorViewModel = viewModel

        binding.lifecycleOwner = this

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.odds_converter_calculator)

        binding.fractionalOddsNumerator.doAfterTextChanged {
            if (binding.fractionalOddsNumerator.isFocused)
            fractionalCalculations(binding)
        }
        binding.fractionalOddsDenominator.doAfterTextChanged {
            if (binding.fractionalOddsDenominator.isFocused)
            fractionalCalculations(binding)
        }
        binding.decimalOdds.doAfterTextChanged {
            if (binding.decimalOdds.isFocused) {
                if (binding.decimalOdds.text.startsWith('.')) binding.decimalOdds.text.insert(
                    0,
                    "0"
                )
                decimalCalculations(binding)
            }
        }

        binding.americanOddsSign.setOnClickListener {

            if (binding.americanOddsSign.text.toString() == "-")
                binding.americanOddsSign.text = "+"
            else
                binding.americanOddsSign.text = "-"

            americanCalculations(binding)
        }

        binding.americanOdds.doAfterTextChanged {
            if (binding.americanOdds.isFocused) {
                if (binding.americanOdds.text.startsWith('.')) binding.americanOdds.text.insert(
                    0,
                    "0"
                )
                americanCalculations(binding)
            }
        }
        binding.probability.doAfterTextChanged {
            if (binding.probability.isFocused) {
                if (binding.probability.text.startsWith('.')) binding.probability.text.insert(
                    0,
                    "0"
                )
                probabilityCalculations(binding)
            }
        }

        binding.buttonClear.setOnClickListener {

            clear(binding)
        }

        binding.buttonSave.setOnClickListener {

            if (!binding.fractionalOddsNumerator.text.isNullOrEmpty() && !binding.decimalOdds.text.isNullOrEmpty()) {
                saveBet()
            }
            else{
                Toast.makeText(context, R.string.please_enter_bet_input_details, Toast.LENGTH_SHORT)
                    .show()
            }

        }

        return binding.root

    }

    private fun fractionalCalculations(binding: FragmentOddsConverterCalculatorBinding){

        if (!binding.fractionalOddsNumerator.text.isNullOrEmpty() && !binding.fractionalOddsDenominator.text.isNullOrEmpty() && binding.fractionalOddsNumerator.text.toString().toInt() !=0 && binding.fractionalOddsDenominator.text.toString().toInt() !=0 ) {

            val df = DecimalFormat("#.##")

            viewModel.fractionalOddsNumerator.value =
                binding.fractionalOddsNumerator.text.toString().toInt()
            viewModel.fractionalOddsDenominator.value =
                binding.fractionalOddsDenominator.text.toString().toInt()

                viewModel.calculateFromFractional()
                binding.decimalOdds.setText(df.format(viewModel.decimalOdds.value))
                binding.americanOdds.setText(df.format(viewModel.americanOdds.value))
                binding.americanOddsSign.text = viewModel.americanOddsSign.value.toString()
                binding.probability.setText(df.format(viewModel.probability.value))
        }
        else{
            binding.decimalOdds.setText("")
            binding.americanOdds.setText("")
            binding.americanOddsSign.text = "+"
            binding.probability.setText("")
        }
    }

    private fun decimalCalculations(binding: FragmentOddsConverterCalculatorBinding){
        if (!binding.decimalOdds.text.isNullOrEmpty() && binding.decimalOdds.text.toString().toDouble() > 1.0){

            val df = DecimalFormat("#.##")

            viewModel.decimalOdds.value = binding.decimalOdds.text.toString().toDouble()
            viewModel.calculateFromDecimal()

            binding.fractionalOddsNumerator.setText(df.format(viewModel.fractionalOddsNumerator.value))
            binding.fractionalOddsDenominator.setText(df.format(viewModel.fractionalOddsDenominator.value))
            binding.americanOdds.setText(df.format(viewModel.americanOdds.value))
            binding.americanOddsSign.text = viewModel.americanOddsSign.value.toString()
            binding.probability.setText(df.format(viewModel.probability.value))

        }
        else{
            binding.fractionalOddsNumerator.setText("")
            binding.fractionalOddsDenominator.setText("")
            binding.americanOdds.setText("")
            binding.americanOddsSign.text = "+"
            binding.probability.setText("")
        }

    }

    private fun americanCalculations(binding: FragmentOddsConverterCalculatorBinding){
        if (!binding.americanOdds.text.isNullOrEmpty() && binding.americanOdds.text.toString().toDouble() != 0.0) {

            val df = DecimalFormat("#.##")

            viewModel.americanOdds.value = binding.americanOdds.text.toString().toDouble()
            viewModel.americanOddsSign.value = binding.americanOddsSign.text.toString()
            viewModel.calculateFromAmerican()

            binding.fractionalOddsNumerator.setText(df.format(viewModel.fractionalOddsNumerator.value))
            binding.fractionalOddsDenominator.setText(df.format(viewModel.fractionalOddsDenominator.value))
            binding.decimalOdds.setText(df.format(viewModel.decimalOdds.value))
            binding.probability.setText(df.format(viewModel.probability.value))
        }
        else{
            binding.fractionalOddsNumerator.setText("")
            binding.fractionalOddsDenominator.setText("")
            binding.decimalOdds.setText("")
            binding.probability.setText("")
        }
    }

    private fun probabilityCalculations(binding: FragmentOddsConverterCalculatorBinding){

        if (!binding.probability.text.isNullOrEmpty() && binding.probability.text.toString().toDouble() > 0.0 && binding.probability.text.toString().toDouble() < 100.0){

            val df = DecimalFormat("#.##")

            viewModel.probability.value = binding.probability.text.toString().toDouble()
            viewModel.calculateFromProbability()

            binding.fractionalOddsNumerator.setText(df.format(viewModel.fractionalOddsNumerator.value))
            binding.fractionalOddsDenominator.setText(df.format(viewModel.fractionalOddsDenominator.value))
            binding.decimalOdds.setText(df.format(viewModel.decimalOdds.value))
            binding.americanOdds.setText(df.format(viewModel.americanOdds.value))
            binding.americanOddsSign.text = viewModel.americanOddsSign.value.toString()

        }
        else{
            binding.fractionalOddsNumerator.setText("")
            binding.fractionalOddsDenominator.setText("")
            binding.decimalOdds.setText("")
            binding.americanOdds.setText("")
            binding.americanOddsSign.text = "+"
        }
    }

    private fun clear(binding: FragmentOddsConverterCalculatorBinding){

        binding.fractionalOddsNumerator.setText("")
        binding.fractionalOddsDenominator.setText("")
        binding.decimalOdds.setText("")
        binding.americanOdds.setText("")
        binding.americanOddsSign.text = "+"
        binding.probability.setText("")
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
}