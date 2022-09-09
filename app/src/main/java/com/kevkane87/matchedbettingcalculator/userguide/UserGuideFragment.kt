package com.kevkane87.matchedbettingcalculator.userguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kevkane87.matchedbettingcalculator.R
import com.kevkane87.matchedbettingcalculator.databinding.FragmentUserGuideBinding

class UserGuideFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentUserGuideBinding>(
            inflater,
            R.layout.fragment_user_guide, container, false
        )

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.user_guide)

        return binding.root

    }
}