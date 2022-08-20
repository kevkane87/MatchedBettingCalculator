package com.example.android.kevkane87.matchedbetapp.savedbets

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentSavedBetsBinding
import com.example.android.kevkane87.matchedbettingcalculator.savedbets.SavedBetsViewModel
import com.example.android.kevkane87.matchedbettingcalculator.savedbets.SavedBetsViewModelFactory

class SavedBetsFragment : Fragment(){

    //create view model
    private val viewModel: SavedBetsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this,
            SavedBetsViewModelFactory(activity.application))[SavedBetsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSavedBetsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.saved_bets)

        binding.savedBetsViewModel = viewModel

        viewModel.loadBets()

        //set the recycler view adapter
        binding.savedBetsRecycler.adapter = SavedBetsAdapter(SavedBetsAdapter.OnLongClickListener {

                viewModel.deleteBet(it.id)

            })


       // setHasOptionsMenu(true)

        return binding.root

    }



    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.calculator -> findNavController().navigate(R.id.action_savedBetsFragment_to_calculatorFragment)
            R.id.find_bets -> findNavController().navigate(R.id.action_savedBetsFragment_to_findBetsFragment)
            R.id.help -> findNavController().navigate(R.id.action_savedBetsFragment_to_helpFragment)

        }
        return true
    }*/

}

private const val TAG = "SavedBetsFragment"

