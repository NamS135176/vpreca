package com.lifecard.vpreca.ui.issuecard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.databinding.CardDesignItemBinding
import com.lifecard.vpreca.databinding.CardUsageHistoryItemBinding
import com.lifecard.vpreca.databinding.VprecaCardItemBinding
import com.lifecard.vpreca.ui.custom.OnItemClickListener

class IssueCardSelectDesignAdapter(
    private var items: List<DesignCard>
) :
    RecyclerView.Adapter<IssueCardSelectDesignAdapter.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardDesignItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mListener)
    }

    class ViewHolder(var binding: CardDesignItemBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardInfo.setOnClickListener { listener.onItemClick(adapterPosition) }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card = items[position]
        holder.binding.cardInfo.setBackgroundResource(items[position].getBackgroundCard())
        if (items[position].isSelected == "1") {
            holder.binding.cardInfo.setBackgroundResource(R.drawable.bg_first_selected)
            holder.binding.cardBorder.setBackgroundResource(items[position].getBackgroundCard())
        }
    }

    override fun getItemCount() = items.size


}