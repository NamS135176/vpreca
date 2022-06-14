package com.lifecard.vpreca.ui.listvpreca

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CardUsageHistory
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.VprecaCard
import com.lifecard.vpreca.databinding.CardUsageHistoryItemBinding
import com.lifecard.vpreca.databinding.VprecaCardItemBinding
import com.lifecard.vpreca.ui.custom.OnItemClickListener

class ListVprecaAdapter(private var items: List<CreditCard>) :
    RecyclerView.Adapter<ListVprecaAdapter.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            VprecaCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mListener)
    }

    class ViewHolder(var binding: VprecaCardItemBinding, listener : OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.cardInfo.cardInfo.setOnClickListener { listener.onItemClick(adapterPosition) }
            }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card = items[position]
        holder.binding.cardInfo.card = items[position]
        when{
            items[position].designId == "001" -> holder.binding.cardInfo.cardInfo.setBackgroundResource(R.drawable.first)
            items[position].designId == "002" -> holder.binding.cardInfo.cardInfo.setBackgroundResource(R.drawable.second)
            items[position].designId == "003" -> holder.binding.cardInfo.cardInfo.setBackgroundResource(R.drawable.third)
            else -> holder.binding.cardInfo.cardInfo.setBackgroundResource(R.drawable.first)
        }
//        if (position % 3 == 0) {
//            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_card_cyan)
//        } else if (position % 3 == 1) {
//            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_card_green)
//        } else {
//            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_card)
//        }
    }

    override fun getItemCount() = items.size


}