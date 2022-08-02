package com.lifecard.vpreca.ui.listvpreca

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.VprecaCardItemBinding

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
        holder.binding.cardInfo.cardInfo.setBackgroundResource(items[position].getBackgroundCard())
    }

    override fun getItemCount() = items.size


}