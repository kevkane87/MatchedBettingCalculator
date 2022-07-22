package com.example.android.kevkane87.matchedbettingcalculator.eachwaycalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentNormalCalculatorBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EachWayCalculator : Fragment() {

    private var betName = "Normal Matched Bet"

    private val viewModel: NormalCalculatorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, NormalCalculatorViewModelFactory(activity.application))[NormalCalculatorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentNormalCalculatorBinding>(
            inflater,
            R.layout.fragment_normal_calculator, container, false
        )

/*

        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_main, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.saved_bets -> {
                        findNavController().navigate(R.id.action_normalCalculatorFragment_to_savedBetsFragment)
                        true
                    }
                    R.id.settings -> {
                        // loadTasks(true)
                        findNavController().navigate(R.id.action_normalCalculatorFragment_to_savedBetsFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
*/

        //(activity as AppCompatActivity).supportActionBar?.title = "Matched Betting Calculator - Normal"

        binding.normalCalculatorViewModel = viewModel

        viewModel.clear()
        viewModel.setRadioButton()

        binding.lifecycleOwner = this

        viewModel.radioResultChecked.observeForever{

            when(viewModel.radioResultChecked.value) {

                R.id.normal ->{
                    binding.groupProfit2.isGone = true
                }
                R.id.underlay ->{
                    binding.groupProfit2.isVisible = true
                }
                R.id.overlay ->{
                    binding.groupProfit2.isVisible = true
                }
            }
        }

        binding.switchPartialLay.setOnClickListener {
            binding.partialLayout.isVisible = binding.switchPartialLay.isChecked
            if (!binding.switchPartialLay.isChecked){
                binding.exLayBetStakePar1.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetStakePar2.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetOddsPar1.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetOddsPar2.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetCommPar1.text = Editable.Factory.getInstance().newEditable("")
                binding.exLayBetCommPar2.text = Editable.Factory.getInstance().newEditable("")
            }
            else{
                binding.addPartLay2.isVisible = true
                binding.groupPartLay2.isGone = true
            }
        }

        binding.addPartLay2.setOnClickListener {
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

        binding.checkBoxBackComm.setOnClickListener{
            if (binding.checkBoxBackComm.isChecked){
                binding.groupBackBetCommission.isVisible = true
            }
            else{
                binding.groupBackBetCommission.isGone = true
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
            Toast.makeText(context, "$stake copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }

        //listeners to calculate for any user input changes

        binding.qualifier.setOnClickListener {
            calculate(binding)
        }
        binding.snr.setOnClickListener {
            calculate(binding)
        }
        binding.sr.setOnClickListener {
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
        binding.exLayBetStakePar1.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetStakePar2.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetOddsPar1.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetOddsPar2.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetCommPar1.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetCommPar2.doAfterTextChanged {
            calculate(binding)
        }

        setHasOptionsMenu(true)
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

        if (binding.backBetCommission.text.startsWith('.')) binding.backBetCommission.text.insert(0, "0")
        if (binding.backBetCommission.text.isNullOrEmpty()) viewModel.backCommission.value = 0.0
        else viewModel.backCommission.value = binding.backBetCommission.text.toString().toDouble()

        if (binding.exLayBetStakePar1.text.startsWith('.')) binding.exLayBetStakePar1.text.insert(0, "0")
        if (binding.exLayBetStakePar1.text.isNullOrEmpty()) viewModel.parLayStake1.value = 0.0
        else viewModel.parLayStake1.value = binding.exLayBetStakePar1.text.toString().toDouble()

        if (binding.exLayBetStakePar2.text.startsWith('.')) binding.exLayBetStakePar2.text.insert(0, "0")
        if (binding.exLayBetStakePar2.text.isNullOrEmpty()) viewModel.parLayStake2.value = 0.0
        else viewModel.parLayStake2.value = binding.exLayBetStakePar2.text.toString().toDouble()

        if (binding.exLayBetOddsPar1.text.startsWith('.')) binding.exLayBetOddsPar1.text.insert(0, "0")
        if (binding.exLayBetOddsPar1.text.isNullOrEmpty()) viewModel.parLayOdds1.value = 0.0
        else viewModel.parLayOdds1.value = binding.exLayBetOddsPar1.text.toString().toDouble()

        if (binding.exLayBetOddsPar2.text.startsWith('.')) binding.exLayBetOddsPar2.text.insert(0, "0")
        if (binding.exLayBetOddsPar2.text.isNullOrEmpty()) viewModel.parLayOdds2.value = 0.0
        else viewModel.parLayOdds2.value = binding.exLayBetOddsPar2.text.toString().toDouble()

        if (binding.exLayBetCommPar1.text.startsWith('.')) binding.exLayBetCommPar1.text.insert(0, "0")
        if (binding.exLayBetCommPar1.text.isNullOrEmpty()) viewModel.parLayComm1.value = 0.0
        else viewModel.parLayComm1.value = binding.exLayBetCommPar1.text.toString().toDouble()

        if (binding.exLayBetCommPar2.text.startsWith('.')) binding.exLayBetCommPar2.text.insert(0, "0")
        if (binding.exLayBetCommPar2.text.isNullOrEmpty()) viewModel.parLayComm2.value = 0.0
        else viewModel.parLayComm2.value = binding.exLayBetCommPar2.text.toString().toDouble()


        viewModel.setBetType()
        viewModel.setResultType()
        viewModel.calculate()
    }

    private fun clear(binding: FragmentNormalCalculatorBinding){
        //clear button
            viewModel.clear()
            binding.backBetStake.text = Editable.Factory.getInstance().newEditable("")
            binding.backBetOdds.text = Editable.Factory.getInstance().newEditable("")
            binding.exCommission.text = Editable.Factory.getInstance().newEditable("")
            binding.backBetCommission.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetOdds.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetStakePar1.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetStakePar2.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetOddsPar1.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetOddsPar2.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetCommPar1.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetCommPar2.text = Editable.Factory.getInstance().newEditable("")
    }


    fun saveBet(){
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Set Bet Name")
// Set up the input
        val input = EditText(requireContext())
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.hint = "Enter Bet Name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            val name = input.text.toString()
            if (name.isEmpty())viewModel.betName.value = betName else viewModel.betName.value = name
            viewModel.setBetDetails()
            viewModel.saveBet()
            Toast.makeText(context, viewModel.betDetails.value, Toast.LENGTH_SHORT)
                .show()
        })
        //builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saved_bets ->  findNavController().navigate(R.id.action_normalCalculatorFragment_to_savedBetsFragment)
            R.id.settings ->  findNavController().navigate(R.id.action_normalCalculatorFragment_to_savedBetsFragment)
        }
        return true
    }
}