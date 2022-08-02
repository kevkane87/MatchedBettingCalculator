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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentEachWayCalculatorBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EachWayCalculatorFragment : Fragment() {

    private var betName = "Each Way Matched Bet"

    private val viewModel: EachWayCalculatorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, EachWayCalculatorViewModelFactory(activity.application))[EachWayCalculatorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentEachWayCalculatorBinding>(
            inflater,
            R.layout.fragment_each_way_calculator, container, false
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

        binding.eachWayCalculatorViewModel = viewModel

        viewModel.clear()
        viewModel.setRadioButton()

        binding.lifecycleOwner = this


        binding.buttonCopyWin.setOnClickListener {
            var stake = binding.layStakeWin.text
            stake = stake.drop(1)
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("copy_lay_stake", stake)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "$stake copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }

        binding.buttonCopyPlace.setOnClickListener {
            var stake = binding.layStakePlace.text
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
        binding.backBetStakeEw.doAfterTextChanged {
            calculate(binding)
        }
        binding.backBetOdds.doAfterTextChanged {
            calculate(binding)
        }
        binding.placePayout.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetOddsWin.doAfterTextChanged {
            calculate(binding)
        }
        binding.exCommission.doAfterTextChanged {
            calculate(binding)
        }
        binding.exLayBetOddsPlace.doAfterTextChanged {
            calculate(binding)
        }
        binding.exCommissionPlace.doAfterTextChanged {
            calculate(binding)
        }
        binding.backBetCommission.doAfterTextChanged {
            calculate(binding)
        }


        setHasOptionsMenu(true)
        return binding.root
    }

    //function to trigger calculation
    private fun calculate(binding: FragmentEachWayCalculatorBinding) {

        if (binding.backBetStakeEw.text.startsWith('.')) binding.backBetStakeEw.text.insert(0, "0")
        if (binding.backBetStakeEw.text.isNullOrEmpty()) viewModel.backBetStakeEw.value = 0.0
        else viewModel.backBetStakeEw.value = binding.backBetStakeEw.text.toString().toDouble()

        if (binding.backBetOdds.text.startsWith('.')) binding.backBetOdds.text.insert(0, "0")
        if (binding.backBetOdds.text.isNullOrEmpty()) viewModel.backBetOdds.value = 0.0
        else viewModel.backBetOdds.value = binding.backBetOdds.text.toString().toDouble()

        if (binding.placePayout.text.startsWith('.')) binding.placePayout.text.insert(0, "0")
        if (binding.placePayout.text.isNullOrEmpty()) viewModel.placePayout.value = 0
        else viewModel.placePayout.value = binding.placePayout.text.toString().toInt()

        if (binding.exLayBetOddsWin.text.startsWith('.')) binding.exLayBetOddsWin.text.insert(0, "0")
        if (binding.exLayBetOddsWin.text.isNullOrEmpty()) viewModel.layBetOdds.value = 0.0
        else viewModel.layBetOdds.value = binding.exLayBetOddsWin.text.toString().toDouble()

        if (binding.exCommission.text.startsWith('.')) binding.exCommission.text.insert(0, "0")
        if (binding.exCommission.text.isNullOrEmpty()) viewModel.exchangeCommission.value = 0.0
        else viewModel.exchangeCommission.value = binding.exCommission.text.toString().toDouble()

        if (binding.exLayBetOddsPlace.text.startsWith('.')) binding.exLayBetOddsPlace.text.insert(0, "0")
        if (binding.exLayBetOddsPlace.text.isNullOrEmpty()) viewModel.placeLayOdds.value = 0.0
        else viewModel.placeLayOdds.value = binding.exLayBetOddsPlace.text.toString().toDouble()

        if (binding.exCommissionPlace.text.startsWith('.')) binding.exCommissionPlace.text.insert(0, "0")
        if (binding.exCommissionPlace.text.isNullOrEmpty()) viewModel.placeLayComm.value = 0.0
        else viewModel.placeLayComm.value = binding.exCommissionPlace.text.toString().toDouble()

        if (binding.backBetCommission.text.startsWith('.')) binding.backBetCommission.text.insert(0, "0")
        if (binding.backBetCommission.text.isNullOrEmpty()) viewModel.backCommission.value = 0.0
        else viewModel.backCommission.value = binding.backBetCommission.text.toString().toDouble()


        viewModel.setBetType()
        viewModel.calculate()
    }

    private fun clear(binding: FragmentEachWayCalculatorBinding){
        //clear button
            viewModel.clear()
            binding.backBetStakeEw.text = Editable.Factory.getInstance().newEditable("")
            binding.backBetOdds.text = Editable.Factory.getInstance().newEditable("")
            binding.placePayout.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetOddsWin.text = Editable.Factory.getInstance().newEditable("")
            binding.exCommission.text = Editable.Factory.getInstance().newEditable("")
            binding.exLayBetOddsPlace.text = Editable.Factory.getInstance().newEditable("")
            binding.exCommissionPlace.text = Editable.Factory.getInstance().newEditable("")
            binding.backBetCommission.text = Editable.Factory.getInstance().newEditable("")

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
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
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

}