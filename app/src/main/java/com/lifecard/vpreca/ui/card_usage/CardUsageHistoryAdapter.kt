package com.lifecard.vpreca.ui.card_usage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.data.model.CardUsageHistory
import com.lifecard.vpreca.databinding.CardUsageHistoryItemBinding

class CardUsageHistoryAdapter(private var items: List<CardUsageHistory>) :
    RecyclerView.Adapter<CardUsageHistoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardUsageHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardUsageHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]
    }

    override fun getItemCount() = items.size


}