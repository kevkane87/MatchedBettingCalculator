package com.example.android.kevkane87.matchedbettingcalculator

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.kevkane87.matchedbetapp.savedbets.SavedBetsAdapter
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("doubleToString")
fun doubleToString(textView: TextView, number: Double) {
    val df = DecimalFormat("#.######")
    textView.text = df.format(number)
}

@BindingAdapter("doubleToCurrencyString")
fun doubleToCurrencyString(textView: TextView, number: Double) {

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(textView.context)
    val currency = sharedPreferences?.getString("key_currency", "£").toString()
    val format = NumberFormat.getCurrencyInstance()

    when(currency){
        "£" -> format.currency = Currency.getInstance("GBP")
        "€" -> format.currency = Currency.getInstance("EUR")
        "$" -> format.currency = Currency.getInstance("USD")
    }

    textView.text = format.format(number).replace("US","")
}

@BindingAdapter("listDataSaved")
fun bindRecyclerViewSaved(recyclerView: RecyclerView, data: List<MatchedBetDTO>?) {
    if (!data.isNullOrEmpty()){
        val adapter = recyclerView.adapter as SavedBetsAdapter
        adapter.submitList(data)
    }
}










