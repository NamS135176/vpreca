package com.lifecard.vpreca.ui.listvpreca

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CardUsageHistory
import com.lifecard.vpreca.data.model.VprecaCard
import com.lifecard.vpreca.databinding.CardUsageHistoryItemBinding
import com.lifecard.vpreca.databinding.VprecaCardItemBinding

class ListVprecaAdapter(private var items: List<VprecaCard>) :
    RecyclerView.Adapter<ListVprecaAdapter.ViewHolder>() {
    class ViewHolder(var binding: VprecaCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            VprecaCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card = items[position]
        if(position%3 == 0){
            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_card_cyan)
        }
        else if(position%3 == 1){
            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_card_green)
        }
        else{
            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_card)
        }
    }

    override fun getItemCount() = items.size


}