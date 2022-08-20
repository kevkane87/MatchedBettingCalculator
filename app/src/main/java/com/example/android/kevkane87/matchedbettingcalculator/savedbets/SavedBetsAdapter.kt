package com.example.android.kevkane87.matchedbetapp.savedbets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.kevkane87.matchedbettingcalculator.MatchedBetDTO
import com.example.android.kevkane87.matchedbettingcalculator.databinding.ItemSavedBetBinding

class SavedBetsAdapter(val onLongClickListener: OnLongClickListener) : ListAdapter<MatchedBetDTO,
            SavedBetsAdapter.ViewHolder>(BetDiffCallback) {


        //viewholder for bet items
        class ViewHolder (private val binding: ItemSavedBetBinding)
            : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: MatchedBetDTO) {
                binding.bet = item
                binding.executePendingBindings()
            }
        }


        //callback for calculating the diff between two non-null items in a list
        companion object BetDiffCallback : DiffUtil.ItemCallback<MatchedBetDTO>() {
            override fun areItemsTheSame(oldItem: MatchedBetDTO, newItem: MatchedBetDTO): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: MatchedBetDTO, newItem: MatchedBetDTO): Boolean {
                return oldItem.id == newItem.id
            }
        }

        //called when RecyclerView needs a new ViewHolder
        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemSavedBetBinding.inflate(
                    LayoutInflater.from(parent.context), parent,
                    false))
        }

        //called by RecyclerView to display the data at the specified position
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val bet = getItem(position)
            holder.itemView.setOnClickListener {
                onLongClickListener.onLongClick(bet)
            }
            holder.bind(bet)
        }


        //click listener class
        class OnLongClickListener(val clickListener: (bet: MatchedBetDTO) -> Unit) {
            fun onLongClick(bet: MatchedBetDTO) = clickListener(bet)
        }
    }
