package com.example.android.kevkane87.matchedbetapp.savedbets

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.kevkane87.matchedbettingcalculator.MatchedBetDTO
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentRemindersBinding
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentSavedBetsBinding
import com.example.android.kevkane87.matchedbettingcalculator.savedbets.SavedBetsViewModel
import com.example.android.kevkane87.matchedbettingcalculator.savedbets.SavedBetsViewModelFactory
import kotlinx.coroutines.Delay


class SavedBetsFragment : Fragment() {

    private var _binding: FragmentSavedBetsBinding? = null
    private val binding get() = _binding!!

    //create view model
    private val viewModel: SavedBetsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this,
            SavedBetsViewModelFactory(activity.application))[SavedBetsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSavedBetsBinding.inflate(inflater)

        _binding!!.lifecycleOwner = this

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.saved_bets)

        _binding!!.savedBetsViewModel = viewModel

        viewModel.loadBets()

        /*adapter = SavedBetsAdaptor(itemList, this)*/



        //set the recycler view adapter

        binding.savedBetsRecycler.adapter = SavedBetsAdapter(SavedBetsAdapter.OnLongClickListener{ bet, pos ->

            deleteBetDialog(bet, pos)

        })


        // binding.savedBetsRecycler.adapter = SavedBetsAdapter(SavedBetsAdapter.OnLongClickListener(-> onLongClick())

        return binding.root
    }


    private fun deleteBetDialog(bet : MatchedBetDTO, position: Int) {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Bet?")

        builder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
            viewModel.deleteBet(bet.id)
            viewModel.betList.value?.removeAt(position)
            _binding?.savedBetsRecycler?.adapter?.notifyDataSetChanged()
            Toast.makeText(context, "Bet Deleted", Toast.LENGTH_SHORT)
                .show()
        })

        builder.setNeutralButton("Dismiss", DialogInterface.OnClickListener { _, _ ->


        })
        builder.show()
    }
/*
    override fun onLongClick(bet: MatchedBetDTO, pos: Int) {
        deleteBetDialog(bet, pos)
    }*/

}


